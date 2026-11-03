package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cadre_products")
data class CadreProduct(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val businessName: String,
    val category: String, // Kuliner, Fashion & Atribut, Jasa & Percetakan, Agribisnis, Toko/Retail
    val price: String,
    val ownerName: String,
    val village: String, // Ranting / Desa di Diwek
    val phone: String, // WhatsApp contact without spaces e.g. "628123456789"
    val description: String,
    val isVerifiedKader: Boolean = true,
    val badgeIconName: String = "store"
)

@Entity(tableName = "saved_bookmarks")
data class SavedBookmark(
    @PrimaryKey
    val itemId: String,
    val title: String,
    val type: String, // PD, PRT, SONG
    val dateSaved: Long = System.currentTimeMillis()
)
