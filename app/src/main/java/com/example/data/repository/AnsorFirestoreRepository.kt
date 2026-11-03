package com.example.data.repository

import android.content.Context
import com.example.R
import com.example.data.model.FirestoreEvent
import com.example.data.model.FirestoreProduct
import com.example.data.model.FirestoreUserProfile
import com.example.util.OperationType
import com.example.util.handleFirestoreError
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.snapshots
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await

class AnsorFirestoreRepository(private val db: FirebaseFirestore) {

    constructor(context: Context) : this(
        FirebaseFirestore.getInstance(
            context.applicationContext.getString(R.string.firestore_database_id)
        )
    )

    private val auth = Firebase.auth

    private fun requireUserId(): String {
        return auth.currentUser?.uid
            ?: throw IllegalStateException("User must be signed in before accessing Firestore.")
    }

    suspend fun saveUserProfile(profile: FirestoreUserProfile): Result<Unit> {
        return try {
            val uid = requireUserId()
            val docRef = db.collection("users").document(uid)
            val payload = mutableMapOf<String, Any>(
                "userId" to uid,
                "name" to profile.name,
                "role" to profile.role,
                "createdAt" to FieldValue.serverTimestamp()
            )
            profile.email?.let { payload["email"] = it }
            profile.ktaNumber?.let { payload["ktaNumber"] = it }
            profile.village?.let { payload["village"] = it }
            profile.unit?.let { payload["unit"] = it }
            docRef.set(payload).await()
            Result.success(Unit)
        } catch (e: Exception) {
            handleFirestoreError(e, OperationType.WRITE, "users/${auth.currentUser?.uid}")
            Result.failure(e)
        }
    }

    suspend fun getUserProfile(userId: String): Result<FirestoreUserProfile?> {
        val path = "users/$userId"
        return try {
            val snapshot = db.document(path).get().await()
            Result.success(snapshot.toObject(FirestoreUserProfile::class.java))
        } catch (e: Exception) {
            handleFirestoreError(e, OperationType.GET, path)
            Result.failure(e)
        }
    }

    fun observeApprovedProducts(): Flow<List<FirestoreProduct>> = flow {
        val path = "lapak_kader"
        emitAll(
            db.collection(path)
                .whereEqualTo("status", "approved")
                .snapshots()
                .map { snapshot -> snapshot.toObjects(FirestoreProduct::class.java) }
                .catch { error ->
                    if (error is Exception) handleFirestoreError(error, OperationType.LIST, path)
                    throw error
                }
        )
    }

    suspend fun addCadreProduct(product: FirestoreProduct): Result<String> {
        val path = "lapak_kader/${product.id}"
        return try {
            val uid = requireUserId()
            val docRef = db.collection("lapak_kader").document(product.id)
            val payload = mutableMapOf<String, Any>(
                "id" to product.id,
                "userId" to uid,
                "name" to product.name,
                "businessName" to product.businessName,
                "category" to product.category,
                "price" to product.price,
                "ownerName" to product.ownerName,
                "phone" to product.phone,
                "isVerifiedKader" to product.isVerifiedKader,
                "status" to product.status,
                "createdAt" to FieldValue.serverTimestamp()
            )
            product.village?.let { payload["village"] = it }
            product.description?.let { payload["description"] = it }
            docRef.set(payload).await()
            Result.success(product.id)
        } catch (e: Exception) {
            handleFirestoreError(e, OperationType.CREATE, path)
            Result.failure(e)
        }
    }

    fun observeAnsorEvents(): Flow<List<FirestoreEvent>> = flow {
        val path = "ansor_events"
        emitAll(
            db.collection(path)
                .snapshots()
                .map { snapshot -> snapshot.toObjects(FirestoreEvent::class.java) }
                .catch { error ->
                    if (error is Exception) handleFirestoreError(error, OperationType.LIST, path)
                    throw error
                }
        )
    }

    suspend fun addAnsorEvent(event: FirestoreEvent): Result<String> {
        val path = "ansor_events/${event.id}"
        return try {
            val uid = requireUserId()
            val docRef = db.collection("ansor_events").document(event.id)
            val payload = mutableMapOf<String, Any>(
                "id" to event.id,
                "title" to event.title,
                "category" to event.category,
                "dayOfMonth" to event.dayOfMonth,
                "monthNumber" to event.monthNumber,
                "year" to event.year,
                "location" to event.location,
                "organizer" to event.organizer,
                "creatorId" to uid,
                "isRantingEvent" to event.isRantingEvent,
                "createdAt" to FieldValue.serverTimestamp()
            )
            event.dayName?.let { payload["dayName"] = it }
            event.timeDisplay?.let { payload["timeDisplay"] = it }
            event.description?.let { payload["description"] = it }
            event.dressCode?.let { payload["dressCode"] = it }
            event.village?.let { payload["village"] = it }
            docRef.set(payload).await()
            Result.success(event.id)
        } catch (e: Exception) {
            handleFirestoreError(e, OperationType.CREATE, path)
            Result.failure(e)
        }
    }
}
