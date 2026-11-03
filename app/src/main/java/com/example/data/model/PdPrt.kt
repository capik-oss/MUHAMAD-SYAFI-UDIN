package com.example.data.model

data class PdPrtItem(
    val id: String,
    val type: DocumentType, // PD or PRT
    val bab: String,
    val babTitle: String,
    val pasal: String,
    val pasalTitle: String,
    val content: String,
    val highlights: List<String> = emptyList()
)

enum class DocumentType(val title: String) {
    PD("Peraturan Dasar (PD)"),
    PRT("Peraturan Rumah Tangga (PRT)")
}

data class SongItem(
    val id: String,
    val title: String,
    val category: String, // Mars, Hymne, Sholawat
    val creator: String,
    val description: String,
    val durationSeconds: Int,
    val verses: List<Verse>
)

data class Verse(
    val stanzaNumber: Int,
    val text: String,
    val chords: String? = null
)
