package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.data.model.AnsorEvent
import com.example.data.model.CadreProduct
import com.example.data.model.EventCategory
import com.example.data.model.SavedBookmark
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [CadreProduct::class, SavedBookmark::class, AnsorEvent::class], version = 2, exportSchema = false)
abstract class AnsorDatabase : RoomDatabase() {
    abstract fun productDao(): ProductDao
    abstract fun bookmarkDao(): BookmarkDao
    abstract fun eventDao(): EventDao

    companion object {
        @Volatile
        private var INSTANCE: AnsorDatabase? = null

        fun getDatabase(context: Context): AnsorDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AnsorDatabase::class.java,
                    "ansor_diwek_db"
                )
                    .fallbackToDestructiveMigration()
                    .addCallback(DatabaseCallback())
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }

    private class DatabaseCallback : Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                CoroutineScope(Dispatchers.IO).launch {
                    populateInitialData(database.productDao(), database.eventDao())
                }
            }
        }

        private suspend fun populateInitialData(productDao: ProductDao, eventDao: EventDao) {
            val initialProducts = listOf(
                CadreProduct(
                    name = "Kopi Santri Robusta Wonosalam",
                    businessName = "Kedai Kopi Barokah Kader",
                    category = "Kuliner",
                    price = "Rp 25.000 / 250gr",
                    ownerName = "Sahabat Ahmad Fadhil",
                    village = "Ranting Cukir, Diwek",
                    phone = "6281234567801",
                    description = "Kopi robusta asli lereng Wonosalam, diroasting khusus oleh kader Ansor Diwek. Rasa mantap, cocok untuk menemani tahlilan & jagongan malam.",
                    badgeIconName = "restaurant"
                ),
                CadreProduct(
                    name = "Bordir Atribut & Jas Resmi GP Ansor",
                    businessName = "Konveksi Ansor Mandiri",
                    category = "Fashion & Atribut",
                    price = "Rp 175.000",
                    ownerName = "Sahabat M. Rifqi",
                    village = "Ranting Kwaron, Diwek",
                    phone = "6281234567802",
                    description = "Menerima pesanan jas Ansor, seragam PDL/PDH Banser, bet bordir komputer berkualitas tinggi, rapi dan sesuai standar PO Ansor.",
                    badgeIconName = "checkroom"
                ),
                CadreProduct(
                    name = "Tahu Pong Renyah Khas Diwek",
                    businessName = "Tahu Sahabat Barokah",
                    category = "Kuliner",
                    price = "Rp 12.000 / porsi",
                    ownerName = "Sahabat Sholihin",
                    village = "Ranting Diwek Pusat",
                    phone = "6281234567803",
                    description = "Tahu pong gurih hangat dengan petis khas Jombang. Menyediakan pesanan untuk acara selamatan, majelis dzikir, dan rapat ranting.",
                    badgeIconName = "restaurant"
                ),
                CadreProduct(
                    name = "Cetak Banner, Sablon & Merchandise Acara",
                    businessName = "Diwek Creative Studio",
                    category = "Jasa & Percetakan",
                    price = "Rp 18.000 / meter",
                    ownerName = "Sahabat Wahyu Hidayat",
                    village = "Ranting Brambang, Diwek",
                    phone = "6281234567804",
                    description = "Jasa cetak banner kilat, sablon kaos kader, cetak ID card PKD/Diklatsar, stiker & mug souvenir. Diskon khusus sesama pengurus Ansor-Banser.",
                    badgeIconName = "print"
                ),
                CadreProduct(
                    name = "Beras Organik Sehat Mentik Wangi",
                    businessName = "Lumbung Pangan Kader NU",
                    category = "Agribisnis",
                    price = "Rp 68.000 / 5kg",
                    ownerName = "Sahabat Nur Hasan",
                    village = "Ranting Bulurejo, Diwek",
                    phone = "6281234567805",
                    description = "Beras hasil tani mitra kader Ansor Diwek tanpa pestisida kimia sintetis. Pulen, wangi, dan mendukung ekonomi petani santri.",
                    badgeIconName = "agriculture"
                ),
                CadreProduct(
                    name = "Servis AC & Elektronik Rumah Tangga",
                    businessName = "Banser Tehnik Mandiri",
                    category = "Jasa & Percetakan",
                    price = "Mulai Rp 50.000",
                    ownerName = "Ndan Joko Purnomo (Banser)",
                    village = "Ranting Jatipelem, Diwek",
                    phone = "6281234567806",
                    description = "Cuci AC, perbaikan kulkas, mesin cuci, instalasi listrik rumah. Pengerjaan jujur, bergaransi, dan siap dipanggil ke seluruh Diwek.",
                    badgeIconName = "build"
                ),
                CadreProduct(
                    name = "Songkok NU Eksklusif Berlogo Emas",
                    businessName = "Koleksi Santri Tebuireng",
                    category = "Fashion & Atribut",
                    price = "Rp 65.000",
                    ownerName = "Sahabat Zainal Arifin",
                    village = "Ranting Kayangan, Diwek",
                    phone = "6281234567807",
                    description = "Peci hitam beludru halus dengan bordir logo NU/Ansor benang emas timbul. Nyaman dipakai sholat, acara resmi organisasi, dan sowan kyai.",
                    badgeIconName = "checkroom"
                ),
                CadreProduct(
                    name = "Madu Randu Asli Hutan Jawa",
                    businessName = "Herbal Barokah Diwek",
                    category = "Toko/Retail",
                    price = "Rp 85.000 / botol",
                    ownerName = "Sahabat Bahrul Ulum",
                    village = "Ranting Bandung, Diwek",
                    phone = "6281234567808",
                    description = "Madu murni dari lebah ternak nektar bunga randu. Bagus untuk menjaga stamina kader saat bertugas pam pengamanan Banser.",
                    badgeIconName = "health_and_safety"
                )
            )
            productDao.insertAll(initialProducts)

            val initialEvents = listOf(
                AnsorEvent(
                    title = "Rutinan Majlis Dzikir & Sholawat Rijalul Ansor",
                    category = EventCategory.MAJLIS,
                    dayOfMonth = 6,
                    monthNumber = 11,
                    year = 2026,
                    dayName = "Kamis Malam Jum'at Legi",
                    timeDisplay = "19.30 - 22.00 WIB",
                    location = "Masjid Ranting Cukir (Kompleks Tebuireng)",
                    organizer = "MDS Rijalul Ansor PAC Diwek",
                    description = "Kajian Kitab Fathul Qorib, Pembacaan Ratib Al-Haddad, Maulid Simtudduror, serta Mau'idhoh Khasanah oleh Rois Syuriah MWC NU Diwek.",
                    dressCode = "Baju Koko Putih, Peci Hitam / Sarung Santri",
                    village = "Ranting Cukir"
                ),
                AnsorEvent(
                    title = "Rapat Koordinasi & Pleno Pengurus Harian PAC",
                    category = EventCategory.RAPAT,
                    dayOfMonth = 9,
                    monthNumber = 11,
                    year = 2026,
                    dayName = "Senin Malam",
                    timeDisplay = "20.00 - 22.30 WIB",
                    location = "Gedung MWC NU Diwek, Jl. Irian Jaya",
                    organizer = "Sekretariat PAC GP Ansor Diwek",
                    description = "Konsolidasi organisasi persiapan Diklatsar Banser angkatan baru, evaluasi progres Lapak Kader, serta penataan KTA Digital.",
                    dressCode = "Batik Ansor / Kemeja Rapi",
                    village = "Kecamatan Diwek"
                ),
                AnsorEvent(
                    title = "Apel Komando & Latihan Gabungan Banser Diwek",
                    category = EventCategory.BANSER,
                    dayOfMonth = 15,
                    monthNumber = 11,
                    year = 2026,
                    dayName = "Ahad Pagi",
                    timeDisplay = "06.00 - 09.30 WIB",
                    location = "Lapangan Olahraga Desa Cukir",
                    organizer = "Satkoryon Banser Diwek",
                    description = "Penyegaran PBB, simulasi tanggap bencana (Bagana), serta apel siaga pengamanan pengajian akbar peringatan haul ulama.",
                    dressCode = "Seragam PDL Banser Lengkap / Baret",
                    village = "Ranting Cukir"
                ),
                AnsorEvent(
                    title = "Bakti Sosial & Santunan Yatim Dhuafa",
                    category = EventCategory.SOSIAL,
                    dayOfMonth = 20,
                    monthNumber = 11,
                    year = 2026,
                    dayName = "Jum'at Pagi",
                    timeDisplay = "08.30 - 11.30 WIB",
                    location = "Balai Desa Bulurejo, Diwek",
                    organizer = "Departemen Sosial PAC Ansor & LAZISNU",
                    description = "Penyaluran 100 paket sembako beras dari Lumbung Pangan Kader serta santunan tunai kepada keluarga binaan di Ranting Bulurejo.",
                    dressCode = "Kaos Kader / Rompi Ansor Peduli",
                    village = "Ranting Bulurejo"
                ),
                AnsorEvent(
                    title = "Kajian Turats & Fiqih Ibadah Praktis (Study Circle)",
                    category = EventCategory.MAJLIS,
                    dayOfMonth = 22,
                    monthNumber = 11,
                    year = 2026,
                    dayName = "Ahad Sore Ba'da Ashar",
                    timeDisplay = "16.00 - 17.30 WIB",
                    location = "Musholla Pondok Ranting Kayangan",
                    organizer = "Biro Kaderisasi & Litbang PAC",
                    description = "Halaqoh ilmiah mingguan bedah risalah Ahlussunnah wal Jama'ah dan Fiqih Muamalah praktis untuk pelaku usaha UMKM kader.",
                    dressCode = "Busana Muslim Santri",
                    village = "Ranting Kayangan"
                ),
                AnsorEvent(
                    title = "Rapat Konsolidasi Ranting GP Ansor Jatipelem",
                    category = EventCategory.RAPAT,
                    dayOfMonth = 26,
                    monthNumber = 11,
                    year = 2026,
                    dayName = "Kamis Malam",
                    timeDisplay = "19.45 - 21.30 WIB",
                    location = "Kediaman Sahabat Ketua Ranting Jatipelem",
                    organizer = "Pimpinan Ranting Ansor Jatipelem",
                    description = "Musyawarah pembentukan posko tanggap bencana ranting dan pendataan potensi ekonomi kader lokal.",
                    dressCode = "Bebas Rapi / Berpeci",
                    isRantingEvent = true,
                    village = "Ranting Jatipelem"
                )
            )
            eventDao.insertAll(initialEvents)
        }
    }
}
