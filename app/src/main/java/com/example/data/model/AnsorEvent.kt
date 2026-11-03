package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class EventCategory(val label: String, val badgeColor: Long) {
    MAJLIS("Majlis & Dzikir", 0xFF0A5C36),       // Emerald Ansor Green
    RAPAT("Rapat & Koordinasi", 0xFFD97706),     // Gold/Amber
    BANSER("Apel & Diklat Banser", 0xFF059669),   // Pine Green
    SOSIAL("Aktivitas Sosial", 0xFF2563EB)       // Royal Blue
}

@Entity(tableName = "ansor_events")
data class AnsorEvent(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val category: EventCategory,
    val dayOfMonth: Int,
    val monthNumber: Int, // 1-12
    val year: Int = 2026,
    val dayName: String, // e.g. "Kamis Malam Jum'at", "Ahad Pagi"
    val timeDisplay: String, // e.g. "19.30 - 22.00 WIB"
    val location: String, // e.g. "Masjid Ranting Cukir, Diwek"
    val organizer: String, // e.g. "MDS Rijalul Ansor PAC Diwek"
    val description: String,
    val dressCode: String = "Baju Koko / Batik NU / Seragam Ansor",
    val isRantingEvent: Boolean = false,
    val village: String = "Kecamatan Diwek"
)
