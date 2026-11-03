package com.example.data.repository

import com.example.data.model.DocumentType
import com.example.data.model.PdPrtItem

object PdPrtRepository {
    val items: List<PdPrtItem> = listOf(
        PdPrtItem(
            id = "pd_mukaddimah",
            type = DocumentType.PD,
            bab = "Mukaddimah",
            babTitle = "Pembukaan / Mukaddimah",
            pasal = "Mukaddimah",
            pasalTitle = "Khittah & Landasan Perjuangan Ansor",
            content = """
                Bahwa sesungguhnya generasi muda Islam Indonesia sebagai bagian mutlak dari tunas-tunas bangsa memiliki peranan strategis dalam mewujudkan cita-cita proklamasi kemerdekaan Republik Indonesia.
                
                Menyadari bahwa kelangsungan perjuangan Nahdlatul Ulama yang berakidah Islam Ahlussunnah wal Jama'ah an-Nahdliyyah menuntut kesiapan pemuda yang tangguh, berilmu, bertakwa, dan berakhlakul karimah;
                
                Maka didirikanlah Gerakan Pemuda Ansor sebagai wadah perjuangan pemuda Nahdlatul Ulama yang senantiasa menjaga keutuhan Negara Kesatuan Republik Indonesia (NKRI), mengawal para ulama, serta menegakkan amar ma'ruf nahi munkar berlandaskan Pancasila dan UUD 1945.
            """.trimIndent(),
            highlights = listOf("Khittah Perjuangan", "Ahlussunnah wal Jama'ah", "NKRI Harga Mati", "Bela Ulama")
        ),
        PdPrtItem(
            id = "pd_bab_1_pasal_1",
            type = DocumentType.PD,
            bab = "BAB I",
            babTitle = "Nama, Waktu dan Kedudukan",
            pasal = "Pasal 1",
            pasalTitle = "Nama Organisasi",
            content = """
                (1) Organisasi ini bernama Gerakan Pemuda Ansor, disingkat GP Ansor.
                (2) Gerakan Pemuda Ansor didirikan pada tanggal 10 Muharram 1353 Hijriyah bertepatan dengan tanggal 24 April 1934 Masehi di Banyuwangi, Jawa Timur.
                (3) Gerakan Pemuda Ansor didirikan untuk jangka waktu yang tidak ditentukan.
            """.trimIndent(),
            highlights = listOf("24 April 1934", "10 Muharram 1353 H", "Banyuwangi")
        ),
        PdPrtItem(
            id = "pd_bab_1_pasal_2",
            type = DocumentType.PD,
            bab = "BAB I",
            babTitle = "Nama, Waktu dan Kedudukan",
            pasal = "Pasal 2",
            pasalTitle = "Kedudukan Organisasi",
            content = """
                (1) Pimpinan Pusat GP Ansor berkedudukan di Ibukota Negara Republik Indonesia.
                (2) Struktur organisasi GP Ansor berjenjang dari Pusat (Pimpinan Pusat), Wilayah (Tingkat Provinsi), Cabang (Tingkat Kabupaten/Kota), Pimpinan Anak Cabang (Tingkat Kecamatan, seperti PAC Diwek), dan Pimpinan Ranting (Tingkat Desa/Kelurahan).
            """.trimIndent(),
            highlights = listOf("Struktur Berjenjang", "PAC Tingkat Kecamatan", "Ranting Tingkat Desa")
        ),
        PdPrtItem(
            id = "pd_bab_2_pasal_3",
            type = DocumentType.PD,
            bab = "BAB II",
            babTitle = "Akidah, Asas dan Prinsip",
            pasal = "Pasal 3",
            pasalTitle = "Akidah Islam Aswaja",
            content = """
                Gerakan Pemuda Ansor beraqidah Islam Ahlussunnah wal Jama'ah an-Nahdliyyah dengan mengikuti:
                a. Dalam bidang akidah mengikuti madzhab Imam Abu Hasan Al-Asy'ari dan Imam Abu Mansur Al-Maturidi;
                b. Dalam bidang fiqih mengikuti salah satu dari empat madzhab: Imam Hanafi, Imam Maliki, Imam Syafi'i, dan Imam Hambali;
                c. Dalam bidang tasawuf mengikuti madzhab Imam Junaid Al-Baghdadi dan Imam Al-Ghazali.
            """.trimIndent(),
            highlights = listOf("Al-Asy'ari & Al-Maturidi", "Madzhab Empat", "Al-Junaid & Al-Ghazali")
        ),
        PdPrtItem(
            id = "pd_bab_2_pasal_4",
            type = DocumentType.PD,
            bab = "BAB II",
            babTitle = "Akidah, Asas dan Prinsip",
            pasal = "Pasal 4",
            pasalTitle = "Asas Berbangsa dan Bernegara",
            content = """
                Dalam kehidupan berbangsa dan bernegara di wilayah Negara Kesatuan Republik Indonesia, Gerakan Pemuda Ansor berasaskan kepada Pancasila dan Undang-Undang Dasar Negara Republik Indonesia Tahun 1945.
            """.trimIndent(),
            highlights = listOf("Pancasila", "UUD 1945", "Bhinneka Tunggal Ika")
        ),
        PdPrtItem(
            id = "pd_bab_2_pasal_5",
            type = DocumentType.PD,
            bab = "BAB II",
            babTitle = "Akidah, Asas dan Prinsip",
            pasal = "Pasal 5",
            pasalTitle = "Prinsip Perjuangan Sikap Kemasyarakatan",
            content = """
                Sikap kemasyarakatan Gerakan Pemuda Ansor berpegang teguh pada prinsip-prinsip:
                1. At-Tawasuth (sikap moderat dan tidak ekstrem);
                2. At-Tawazun (sikap seimbang dalam segala hal);
                3. Al-I'tidal (sikap tegak lurus dan adil);
                4. At-Tasamuh (sikap toleran dan menghargai perbedaan);
                5. Amar Ma'ruf Nahi Munkar (mengajak kebaikan dan mencegah kemungkaran dengan cara ma'ruf).
            """.trimIndent(),
            highlights = listOf("Tawasuth", "Tawazun", "I'tidal", "Tasamuh")
        ),
        PdPrtItem(
            id = "pd_bab_3_pasal_6",
            type = DocumentType.PD,
            bab = "BAB III",
            babTitle = "Sifat, Status dan Fungsi",
            pasal = "Pasal 6",
            pasalTitle = "Status Banom NU",
            content = """
                (1) Gerakan Pemuda Ansor berstatus sebagai Badan Otonom (Banom) dari Jam'iyyah Nahdlatul Ulama.
                (2) GP Ansor berfungsi sebagai wadah kaderisasi kepemudaan Nahdlatul Ulama dalam rangka mempersiapkan calon-calon pemimpin bangsa dan persyarikatan.
                (3) GP Ansor merupakan gerakan keagamaan, kemasyarakatan, kepemudaan, dan kebangsaan.
            """.trimIndent(),
            highlights = listOf("Badan Otonom NU", "Kawah Candradimuka Pemuda", "Kader Inti NU")
        ),
        PdPrtItem(
            id = "pd_bab_4_pasal_7",
            type = DocumentType.PD,
            bab = "BAB IV",
            babTitle = "Tujuan dan Usaha",
            pasal = "Pasal 7",
            pasalTitle = "Tujuan GP Ansor",
            content = """
                Tujuan didirikannya Gerakan Pemuda Ansor adalah:
                1. Membentuk dan mengembangkan generasi muda Indonesia yang beriman, bertaqwa kepada Allah SWT, berbudi luhur, berilmu, cakap, berakhlakul karimah, dan mandiri;
                2. Menjaga, memelihara, dan mengembangkan ajaran Islam Ahlussunnah wal Jama'ah an-Nahdliyyah;
                3. Mempertahankan dan mengawal kedaulatan Negara Kesatuan Republik Indonesia;
                4. Memajukan kesejahteraan dan kemandirian ekonomi umat serta memperjuangkan keadilan sosial.
            """.trimIndent(),
            highlights = listOf("Generasi Berilmu & Mandiri", "Kedaulatan NKRI", "Kemandirian Ekonomi Kader")
        ),
        PdPrtItem(
            id = "pd_bab_5_pasal_8",
            type = DocumentType.PD,
            bab = "BAB V",
            babTitle = "Satuan BANSER",
            pasal = "Pasal 8",
            pasalTitle = "Barisan Ansor Serbaguna (BANSER)",
            content = """
                (1) Barisan Ansor Serbaguna (BANSER) merupakan tenaga inti Gerakan Pemuda Ansor sebagai penggerak, pengemban, dan pengaman program-program GP Ansor dan Nahdlatul Ulama.
                (2) BANSER bersifat semi-militer, sukarela, berdisiplin tinggi, dan berdedikasi membela ulama, agama, bangsa, serta kemanusiaan.
                (3) BANSER memiliki kesatuan khusus seperti Balantas (Lalu Lintas), Bagana (Tanggap Bencana), Basada (Husada/Kesehatan), Baritim (Maritim), Densus 99, dan Banser Protokoler.
            """.trimIndent(),
            highlights = listOf("Tenaga Inti GP Ansor", "Pengawal Ulama & NKRI", "Satuan Khusus BANSER")
        ),
        PdPrtItem(
            id = "prt_bab_1_pasal_1",
            type = DocumentType.PRT,
            bab = "BAB I PRT",
            babTitle = "Keanggotaan",
            pasal = "Pasal 1",
            pasalTitle = "Syarat Menjadi Anggota",
            content = """
                (1) Anggota Gerakan Pemuda Ansor adalah pemuda warga negara Indonesia beragama Islam yang berakidah Ahlussunnah wal Jama'ah an-Nahdliyyah, berusia antara 20 (dua puluh) sampai dengan 40 (empat puluh) tahun.
                (2) Untuk menjadi anggota GP Ansor, seorang calon wajib mendaftarkan diri secara sah dan mengikuti serta lulus jenjang kaderisasi awal:
                    a. Pelatihan Kepemimpinan Dasar (PKD) untuk kader GP Ansor;
                    b. Pendidikan dan Pelatihan Dasar (Diklatsar) untuk kader Barisan Ansor Serbaguna (BANSER).
            """.trimIndent(),
            highlights = listOf("Usia 20 - 40 Tahun", "Wajib Lulus PKD / Diklatsar")
        ),
        PdPrtItem(
            id = "prt_bab_2_pasal_2",
            type = DocumentType.PRT,
            bab = "BAB II PRT",
            babTitle = "Jenjang Kaderisasi",
            pasal = "Pasal 2",
            pasalTitle = "Tingkatan Pengkaderan",
            content = """
                (1) Jenjang kaderisasi formal GP Ansor terdiri atas:
                    a. Pelatihan Kepemimpinan Dasar (PKD) diselenggarakan oleh PAC (seperti PAC Diwek);
                    b. Pelatihan Kepemimpinan Lanjutan (PKL) diselenggarakan oleh PC (Kabupaten);
                    c. Pelatihan Kepemimpinan Nasional (PKN) diselenggarakan oleh PP (Pusat).
                (2) Jenjang kaderisasi formal Banser terdiri atas:
                    a. Diklatsar (Pendidikan dan Pelatihan Dasar) diselenggarakan oleh Satkoryon (Kecamatan);
                    b. Susbalan (Kursus Banser Lanjutan) diselenggarakan oleh Satkorcab (Kabupaten);
                    c. Susbanpim (Kursus Banser Pimpinan) diselenggarakan oleh Satkornas (Pusat).
            """.trimIndent(),
            highlights = listOf("PKD & Diklatsar Tingkat Kecamatan", "PKL & Susbalan", "PKN & Susbanpim")
        ),
        PdPrtItem(
            id = "prt_bab_3_pasal_3",
            type = DocumentType.PRT,
            bab = "BAB III PRT",
            babTitle = "Hak dan Kewajiban Anggota",
            pasal = "Pasal 3",
            pasalTitle = "Kewajiban Kader",
            content = """
                Setiap anggota GP Ansor berkewajiban:
                1. Menjaga nama baik dan marwah Jam'iyyah Nahdlatul Ulama serta GP Ansor;
                2. Menaati PD/PRT dan Peraturan Organisasi (PO) yang berlaku;
                3. Berpartisipasi aktif dalam kegiatan sosial, keagamaan, dan pemberdayaan masyarakat;
                4. Mendukung sesama kader dalam pengembangan ekonomi dan kemandirian usaha (seperti Lapak Kader);
                5. Sigap membela kehormatan ulama dan keutuhan Negara Kesatuan Republik Indonesia.
            """.trimIndent(),
            highlights = listOf("Jaga Marwah NU", "Gotong Royong Kader", "Taat Instruksi Organisasi")
        ),
        PdPrtItem(
            id = "prt_bab_4_pasal_4",
            type = DocumentType.PRT,
            bab = "BAB IV PRT",
            babTitle = "Majelis Dzikir dan Sholawat",
            pasal = "Pasal 4",
            pasalTitle = "MDS Rijalul Ansor",
            content = """
                (1) Untuk menjaga dan menghidupkan tradisi keagamaan amaliyah Ahlussunnah wal Jama'ah an-Nahdliyyah, dibentuk Majelis Dzikir dan Sholawat Rijalul Ansor (MDS RA).
                (2) MDS Rijalul Ansor berkedudukan di semua tingkatan kepengurusan dari Pimpinan Pusat hingga Pimpinan Ranting.
                (3) Kegiatan rutin MDS Rijalul Ansor meliputi pembacaan Ratibul Haddad, Maulid Simtudduror/Al-Barzanji, Tahlil, Manaqib, dan kajian kitab kuning.
            """.trimIndent(),
            highlights = listOf("MDS Rijalul Ansor", "Amaliyah Aswaja", "Tradisi Sholawat & Dzikir")
        )
    )
}
