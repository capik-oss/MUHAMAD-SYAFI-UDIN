package com.example.data.repository

import com.example.data.model.SongItem
import com.example.data.model.Verse

object SongRepository {
    val songs: List<SongItem> = listOf(
        SongItem(
            id = "mars_ansor",
            title = "Mars GP Ansor",
            category = "Mars Organisasi",
            creator = "H. Mahbub Djunaidi",
            description = "Lagu mars resmi Gerakan Pemuda Ansor yang mengobarkan semangat perjuangan, keikhlasan berkorban, dan kesetiaan menjaga panji Islam Ahlussunnah wal Jama'ah.",
            durationSeconds = 120,
            verses = listOf(
                Verse(
                    stanzaNumber = 1,
                    text = "Darah dan nyawa telah kuberikan\nSyahid di jalan Allah kupersembahkan\nBela agama bangsa dan negara\nDemi cita-cita mulia bahagia",
                    chords = "C - G - Am - Em - F - C - G"
                ),
                Verse(
                    stanzaNumber = 2,
                    text = "Gerakan Pemuda Ansor jaya\nPenerus pahlawan bangsa\nMaju serentak mengemban amanat\nMenuju cita-cita rakyat",
                    chords = "F - G - Em - Am - Dm - G - C"
                ),
                Verse(
                    stanzaNumber = 3,
                    text = "Tegakkan keadilan kebenaran\nSingkirkan kepalsuan kezaliman\nDengan bimbingan taufiq Ilahi\nGP Ansor maju berbakti",
                    chords = "C - Em - F - G - Am - D - G"
                ),
                Verse(
                    stanzaNumber = 4,
                    text = "Bela agama dan nusa bangsa\nPasti jaya!",
                    chords = "F - G - C"
                )
            )
        ),
        SongItem(
            id = "mars_banser",
            title = "Mars Barisan Ansor Serbaguna (BANSER)",
            category = "Mars Pasukan",
            creator = "Keluarga Besar Banser Nasional",
            description = "Mars kebanggaan Banser yang dinyanyikan penuh khidmat dan derap langkah serempak saat apel, upacara, maupun pengamanan kegiatan ulama.",
            durationSeconds = 110,
            verses = listOf(
                Verse(
                    stanzaNumber = 1,
                    text = "Izinkan ayah izinkan ibu\nIzinkan kami pergi berjuang\nDibawah kibaran panji Ansor\nMajulah ayo maju pantang mundur",
                    chords = "G - D - Em - C - G - D - G"
                ),
                Verse(
                    stanzaNumber = 2,
                    text = "Tidak kembali pulang\nSebelum kita yang menang\nWalau mayat terdampar di medan perang\nDemi Islam demi NU kami berjuang",
                    chords = "C - G - D - G - C - G - D - G"
                ),
                Verse(
                    stanzaNumber = 3,
                    text = "Maju ayo maju ayo terus maju\nSingkirkanlah dia dia dia\nKikis habislah musuh agama\nWahai Barisan Ansor Serbaguna",
                    chords = "G - C - D - G - Em - C - D - G"
                ),
                Verse(
                    stanzaNumber = 4,
                    text = "Di mana engkau berada\nDi sana kami berada\nUntuk mengawal para ulama\nDan membela nusa dan bangsa!",
                    chords = "C - G - D - G"
                )
            )
        ),
        SongItem(
            id = "ya_lal_wathon",
            title = "Subbanul Wathon (Ya Lal Wathon)",
            category = "Lagu Kebangsaan Nahdlatul Ulama",
            creator = "KH. Abdul Wahab Chasbullah (1916)",
            description = "Lagu penyemangat cinta tanah air karya salah satu pendiri Nahdlatul Ulama dari Pondok Pesantren Tambakberas Jombang. 'Hubbul wathon minal iman' (cinta tanah air adalah sebagian dari iman).",
            durationSeconds = 135,
            verses = listOf(
                Verse(
                    stanzaNumber = 1,
                    text = "Ya Lal Wathon Ya Lal Wathon Ya Lal Wathon\nHubbul Wathon minal Iman\nWala Takun minal Hirman\nInhadlu Alal Wathon",
                    chords = "D - A - Bm - F#m - G - D - A"
                ),
                Verse(
                    stanzaNumber = 2,
                    text = "Indonesia Biladi\nAnta 'Unwanul Fakhoma\nKullu May Ya'tika Yauma\nThomihay Yalqa Himama",
                    chords = "G - A - F#m - Bm - Em - A - D"
                ),
                Verse(
                    stanzaNumber = 3,
                    text = "Pusaka Hati Wahai Tanah Airku\nCintaku dalam Imanku\nJangan Halangkan Nasibmu\nBangkitlah Hai Bangsaku!",
                    chords = "D - F#m - G - A - Bm - G - A"
                ),
                Verse(
                    stanzaNumber = 4,
                    text = "Indonesia Negeriku\nEngkau Panji Martabatku\nSiapa Datang Mengancammu\n'Kan Binasa di Bawah Dulimu!",
                    chords = "G - A - D - Bm - Em - A - D"
                )
            )
        ),
        SongItem(
            id = "hymne_ansor",
            title = "Hymne GP Ansor",
            category = "Hymne Perenungan",
            creator = "H. Sambas Mangundikarta",
            description = "Hymne refleksi komitmen kader Ansor kepada sang Khalik, pengabdian suci tanpa pamrih, dan kerelaan berkorban demi tegaknya panji kebenaran.",
            durationSeconds = 100,
            verses = listOf(
                Verse(
                    stanzaNumber = 1,
                    text = "Tuhan Yang Maha Esa\nBimbinglah langkah kami\nMenuju jalan ridho-Mu\nDengan panji Ansor suci",
                    chords = "Am - Dm - G - C - F - E - Am"
                ),
                Verse(
                    stanzaNumber = 2,
                    text = "Satukan tekad jiwa raga\nBakti tiada tara\nTegakkan marwah agama\nDamai sentosa Indonesia",
                    chords = "Dm - Am - E - Am - Dm - Am - E - Am"
                )
            )
        ),
        SongItem(
            id = "sholawat_asyghil",
            title = "Sholawat Asyghil",
            category = "Amaliyah & Doa Pejuang",
            creator = "Habib Ali bin Abi Bakar bin Abdurrahman As-Segaf",
            description = "Sholawat perlindungan umat Islam dari kezaliman orang-orang zalim, sering dibaca kader Ansor & Banser sebelum memulai tugas pengamanan dan apel komando.",
            durationSeconds = 115,
            verses = listOf(
                Verse(
                    stanzaNumber = 1,
                    text = "اللَّهُمَّ صَلِّ عَلَى سَيِّدِنَا مُحَمَّدٍ\nوَأَشْغِلِ الظَّالِمِينَ بِالظَّالِمِينَ\n\nAllahumma sholli 'ala sayyidina Muhammad\nWa asyghilizh-zholimina bizh-zholimin",
                    chords = "Dm - Gm - C - F - Bb - A - Dm"
                ),
                Verse(
                    stanzaNumber = 2,
                    text = "وَأَخْرِجْنَا مِنْ بَيْنِهِمْ سَالِمِينَ\nوَعَلَى آلِهِ وَصَحْبِهِ أَجْمَعِينَ\n\nWa akhrijna min bainihim salimin\nWa 'ala alihi wa shohbihi ajma'in",
                    chords = "Gm - C - F - Bb - Gm - A - Dm"
                )
            )
        ),
        SongItem(
            id = "sholawat_badar",
            title = "Sholawat Badar",
            category = "Amaliyah & Sholawat Kebangsaan NU",
            creator = "KH. Ali Manshur Shiddiq (Banyuwangi, 1962)",
            description = "Sholawat agung yang digubah ulama NU dalam masa-masa genting perjuangan bangsa Indonesia, memohon pertolongan Allah berwasilah ahlul Badar.",
            durationSeconds = 140,
            verses = listOf(
                Verse(
                    stanzaNumber = 1,
                    text = "صَلاَةُ اللهِ سَلاَمُ اللهِ * عَلَى طٰهَ رَسُوْلِ اللهِ\nصَلاَةُ اللهِ سَلاَمُ اللهِ * عَلَى يٰسٓ حَبِيْبِ اللهِ\n\nSholatullah salamullah * 'Ala Thoha rosulillah\nSholatullah salamullah * 'Ala Yasin habibillah",
                    chords = "C - F - G - C - Am - Dm - G - C"
                ),
                Verse(
                    stanzaNumber = 2,
                    text = "تَوَسَّلْنَا بِبِسْمِ اللهِ * وَبِالهَادِي رَسُوْلِ اللهِ\nوَكُلِّ مُجَاهِدٍ لِلّٰهِ * بِأَهْلِ البَدْرِ يَا أَللهُ\n\nTawassalna bibismillah * Wa bil hadi rosulillah\nWa kulli mujahidin lillah * Bi ahlil badri Ya Allah",
                    chords = "F - C - G - C - F - C - G - C"
                )
            )
        )
    )
}
