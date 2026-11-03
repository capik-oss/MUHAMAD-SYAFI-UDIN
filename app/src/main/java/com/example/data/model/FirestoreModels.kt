package com.example.data.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.IgnoreExtraProperties
import com.google.firebase.firestore.ServerTimestamp

@IgnoreExtraProperties
data class FirestoreUserProfile(
    @DocumentId val id: String = "",
    val userId: String = "",
    val name: String = "",
    val email: String? = null,
    val role: String = "kader",
    val ktaNumber: String? = null,
    val village: String? = null,
    val unit: String? = null,
    @ServerTimestamp val createdAt: Timestamp? = null,
    @ServerTimestamp val updatedAt: Timestamp? = null
)

@IgnoreExtraProperties
data class FirestoreProduct(
    @DocumentId val docId: String = "",
    val id: String = "",
    val userId: String = "",
    val name: String = "",
    val businessName: String = "",
    val category: String = "",
    val price: String = "",
    val ownerName: String = "",
    val village: String? = null,
    val phone: String = "",
    val description: String? = null,
    val isVerifiedKader: Boolean = true,
    val status: String = "approved",
    @ServerTimestamp val createdAt: Timestamp? = null,
    @ServerTimestamp val updatedAt: Timestamp? = null
)

@IgnoreExtraProperties
data class FirestoreEvent(
    @DocumentId val docId: String = "",
    val id: String = "",
    val title: String = "",
    val category: String = "",
    val dayOfMonth: Long = 1L,
    val monthNumber: Long = 1L,
    val year: Long = 2026L,
    val dayName: String? = null,
    val timeDisplay: String? = null,
    val location: String = "",
    val organizer: String = "",
    val description: String? = null,
    val dressCode: String? = null,
    val isRantingEvent: Boolean = false,
    val village: String? = null,
    val creatorId: String = "",
    @ServerTimestamp val createdAt: Timestamp? = null,
    @ServerTimestamp val updatedAt: Timestamp? = null
)
