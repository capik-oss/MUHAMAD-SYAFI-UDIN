package com.example.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.model.AuthUser
import com.example.data.model.FirestoreUserProfile
import com.example.data.model.UserRole
import com.example.data.repository.AnsorFirestoreRepository
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AuthViewModel(application: Application) : AndroidViewModel(application) {
    private val prefs = application.getSharedPreferences("ansor_auth_prefs", Context.MODE_PRIVATE)
    private val repository = AnsorFirestoreRepository(application)
    private val auth = Firebase.auth

    private val _currentUser = MutableStateFlow<AuthUser?>(loadUserFromPrefs())
    val currentUser: StateFlow<AuthUser?> = _currentUser.asStateFlow()

    private val _firebaseUser = MutableStateFlow<FirebaseUser?>(auth.currentUser)
    val firebaseUser: StateFlow<FirebaseUser?> = _firebaseUser.asStateFlow()

    private val _loginError = MutableStateFlow<String?>(null)
    val loginError: StateFlow<String?> = _loginError.asStateFlow()

    private val authListener = FirebaseAuth.AuthStateListener { fbAuth ->
        val user = fbAuth.currentUser
        _firebaseUser.value = user
        if (user != null) {
            handleFirebaseUserSignedIn(user)
        }
    }

    init {
        auth.addAuthStateListener(authListener)
    }

    override fun onCleared() {
        super.onCleared()
        auth.removeAuthStateListener(authListener)
    }

    private fun handleFirebaseUserSignedIn(user: FirebaseUser) {
        viewModelScope.launch {
            val existing = repository.getUserProfile(user.uid).getOrNull()
            if (existing != null) {
                val role = if (existing.role.equals("admin", ignoreCase = true)) UserRole.ADMIN else UserRole.KADER
                val authUser = AuthUser(
                    id = user.uid,
                    name = existing.name.ifBlank { user.displayName ?: "Sahabat Ansor" },
                    role = role,
                    identifier = existing.email ?: user.email ?: "",
                    village = existing.village ?: "Kecamatan Diwek",
                    unit = existing.unit ?: "GP Ansor",
                    ktaNumber = existing.ktaNumber ?: "35.17.05.001",
                    isVerified = true
                )
                saveUserToPrefs(authUser)
                _currentUser.value = authUser
            } else {
                val displayName = user.displayName ?: "Sahabat Ansor"
                val profile = FirestoreUserProfile(
                    userId = user.uid,
                    name = displayName,
                    email = user.email,
                    role = "kader",
                    ktaNumber = "35.17.05.${(100..999).random()}",
                    village = "Kecamatan Diwek",
                    unit = "GP Ansor"
                )
                repository.saveUserProfile(profile)
                val authUser = AuthUser(
                    id = user.uid,
                    name = displayName,
                    role = UserRole.KADER,
                    identifier = user.email ?: "",
                    village = "Kecamatan Diwek",
                    unit = "GP Ansor",
                    ktaNumber = profile.ktaNumber ?: "35.17.05.001",
                    isVerified = true
                )
                saveUserToPrefs(authUser)
                _currentUser.value = authUser
            }
        }
    }

    private fun loadUserFromPrefs(): AuthUser? {
        val isLoggedIn = prefs.getBoolean("is_logged_in", false)
        if (!isLoggedIn) return null

        val roleStr = prefs.getString("user_role", UserRole.GUEST.name) ?: UserRole.GUEST.name
        val role = try {
            UserRole.valueOf(roleStr)
        } catch (e: Exception) {
            UserRole.GUEST
        }

        return AuthUser(
            id = prefs.getString("user_id", "user_1") ?: "user_1",
            name = prefs.getString("user_name", "Sahabat Kader") ?: "Sahabat Kader",
            role = role,
            identifier = prefs.getString("user_identifier", "") ?: "",
            village = prefs.getString("user_village", "Kecamatan Diwek") ?: "Kecamatan Diwek",
            unit = prefs.getString("user_unit", "GP Ansor") ?: "GP Ansor",
            ktaNumber = prefs.getString("user_kta", "35.17.05.001") ?: "35.17.05.001",
            isVerified = true
        )
    }

    private fun saveUserToPrefs(user: AuthUser) {
        prefs.edit()
            .putBoolean("is_logged_in", true)
            .putString("user_id", user.id)
            .putString("user_name", user.name)
            .putString("user_role", user.role.name)
            .putString("user_identifier", user.identifier)
            .putString("user_village", user.village)
            .putString("user_unit", user.unit)
            .putString("user_kta", user.ktaNumber)
            .apply()
    }

    fun loginKader(
        name: String,
        phoneOrKta: String,
        village: String,
        unit: String
    ): Boolean {
        if (name.isBlank() || phoneOrKta.isBlank()) {
            _loginError.value = "Nama dan No. KTA/Telepon wajib diisi."
            return false
        }

        val generatedKta = if (phoneOrKta.contains("35.17")) phoneOrKta else "35.17.05.${(100..999).random()}"
        val userUid = auth.currentUser?.uid ?: "kader_${System.currentTimeMillis()}"
        val user = AuthUser(
            id = userUid,
            name = name.trim(),
            role = UserRole.KADER,
            identifier = phoneOrKta.trim(),
            village = village.trim(),
            unit = unit.trim(),
            ktaNumber = generatedKta,
            isVerified = true
        )

        saveUserToPrefs(user)
        _currentUser.value = user
        _loginError.value = null

        // Sync to cloud Firestore if authenticated
        if (auth.currentUser != null) {
            viewModelScope.launch {
                repository.saveUserProfile(
                    FirestoreUserProfile(
                        userId = userUid,
                        name = name.trim(),
                        email = auth.currentUser?.email,
                        role = "kader",
                        ktaNumber = generatedKta,
                        village = village.trim(),
                        unit = unit.trim()
                    )
                )
            }
        }
        return true
    }

    fun loginAdmin(
        username: String,
        passPin: String
    ): Boolean {
        val trimmedUser = username.trim()
        val trimmedPin = passPin.trim()

        if (trimmedUser.isBlank() || trimmedPin.isBlank()) {
            _loginError.value = "Username dan PIN Pengurus wajib diisi."
            return false
        }

        val isValidPin = trimmedPin == "1934" || trimmedPin.equals("admin123", ignoreCase = true) || trimmedPin.equals("ansor1934", ignoreCase = true)

        if (!isValidPin) {
            _loginError.value = "PIN Keamanan Pengurus salah. (Petunjuk: tahun pendirian Ansor '1934')"
            return false
        }

        val userUid = auth.currentUser?.uid ?: "admin_pac_diwek"
        val user = AuthUser(
            id = userUid,
            name = if (trimmedUser.equals("admin", ignoreCase = true)) "Pengurus Harian PAC Diwek" else trimmedUser,
            role = UserRole.ADMIN,
            identifier = trimmedUser,
            village = "Pusat PAC Diwek (MWC NU)",
            unit = "Pimpinan Harian PAC",
            ktaNumber = "PAC-DIWEK-001",
            isVerified = true
        )

        saveUserToPrefs(user)
        _currentUser.value = user
        _loginError.value = null

        if (auth.currentUser != null) {
            viewModelScope.launch {
                repository.saveUserProfile(
                    FirestoreUserProfile(
                        userId = userUid,
                        name = user.name,
                        email = auth.currentUser?.email,
                        role = "admin",
                        ktaNumber = "PAC-DIWEK-001",
                        village = "Pusat PAC Diwek",
                        unit = "Pimpinan Harian PAC"
                    )
                )
            }
        }
        return true
    }

    fun loginDemoKader() {
        loginKader(
            name = "Sahabat Fajar Shodiq",
            phoneOrKta = "35.17.05.2026.042",
            village = "Ranting Cukir, Diwek",
            unit = "GP Ansor & Banser"
        )
    }

    fun loginDemoAdmin() {
        loginAdmin(
            username = "Admin PAC Diwek",
            passPin = "1934"
        )
    }

    fun logout() {
        auth.signOut()
        prefs.edit().clear().apply()
        _currentUser.value = null
        _loginError.value = null
    }

    fun clearError() {
        _loginError.value = null
    }
}
