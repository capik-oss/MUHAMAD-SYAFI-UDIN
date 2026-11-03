package com.example.data.model

enum class UserRole(val label: String, val badge: String) {
    GUEST("Tamu / Pengunjung", "Tamu"),
    KADER("Kader Ansor / Banser", "Kader Resmi"),
    ADMIN("Pengurus Harian PAC", "Admin PAC")
}

data class AuthUser(
    val id: String,
    val name: String,
    val role: UserRole,
    val identifier: String, // KTA or Username
    val village: String = "Kecamatan Diwek",
    val unit: String = "GP Ansor", // GP Ansor, Banser, MDS Rijalul Ansor, Pengurus Harian
    val ktaNumber: String = "35.17.05.001",
    val isVerified: Boolean = true,
    val loginTime: Long = System.currentTimeMillis()
)
