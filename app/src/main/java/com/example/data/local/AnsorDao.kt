package com.example.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.model.AnsorEvent
import com.example.data.model.CadreProduct
import com.example.data.model.EventCategory
import com.example.data.model.SavedBookmark
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductDao {
    @Query("SELECT * FROM cadre_products ORDER BY id DESC")
    fun getAllProducts(): Flow<List<CadreProduct>>

    @Query("SELECT * FROM cadre_products WHERE category = :category ORDER BY id DESC")
    fun getProductsByCategory(category: String): Flow<List<CadreProduct>>

    @Query("SELECT * FROM cadre_products WHERE name LIKE '%' || :query || '%' OR businessName LIKE '%' || :query || '%' OR village LIKE '%' || :query || '%' OR ownerName LIKE '%' || :query || '%'")
    fun searchProducts(query: String): Flow<List<CadreProduct>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProduct(product: CadreProduct): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(products: List<CadreProduct>)

    @Delete
    suspend fun deleteProduct(product: CadreProduct)
}

@Dao
interface BookmarkDao {
    @Query("SELECT * FROM saved_bookmarks ORDER BY dateSaved DESC")
    fun getAllBookmarks(): Flow<List<SavedBookmark>>

    @Query("SELECT EXISTS(SELECT 1 FROM saved_bookmarks WHERE itemId = :id)")
    fun isBookmarked(id: String): Flow<Boolean>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addBookmark(bookmark: SavedBookmark)

    @Query("DELETE FROM saved_bookmarks WHERE itemId = :id")
    suspend fun removeBookmark(id: String)
}

@Dao
interface EventDao {
    @Query("SELECT * FROM ansor_events ORDER BY year ASC, monthNumber ASC, dayOfMonth ASC, id ASC")
    fun getAllEvents(): Flow<List<AnsorEvent>>

    @Query("SELECT * FROM ansor_events WHERE category = :category ORDER BY year ASC, monthNumber ASC, dayOfMonth ASC")
    fun getEventsByCategory(category: EventCategory): Flow<List<AnsorEvent>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEvent(event: AnsorEvent): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(events: List<AnsorEvent>)

    @Delete
    suspend fun deleteEvent(event: AnsorEvent)
}
