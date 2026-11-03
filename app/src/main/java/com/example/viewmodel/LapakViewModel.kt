package com.example.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.AnsorDatabase
import com.example.data.model.CadreProduct
import com.example.data.model.FirestoreProduct
import com.example.data.model.SavedBookmark
import com.example.data.repository.AnsorFirestoreRepository
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.UUID

class LapakViewModel(application: Application) : AndroidViewModel(application) {
    private val database = AnsorDatabase.getDatabase(application)
    private val productDao = database.productDao()
    private val bookmarkDao = database.bookmarkDao()
    private val firestoreRepo = AnsorFirestoreRepository(application)

    private val _selectedCategory = MutableStateFlow("Semua")
    val selectedCategory: StateFlow<String> = _selectedCategory.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _rawProducts = productDao.getAllProducts()

    val filteredProducts: StateFlow<List<CadreProduct>> = combine(
        _rawProducts,
        _selectedCategory,
        _searchQuery
    ) { products, category, query ->
        products.filter { product ->
            val matchCategory = category == "Semua" || product.category.equals(category, ignoreCase = true)
            val matchQuery = query.isBlank() ||
                    product.name.contains(query, ignoreCase = true) ||
                    product.businessName.contains(query, ignoreCase = true) ||
                    product.village.contains(query, ignoreCase = true) ||
                    product.ownerName.contains(query, ignoreCase = true)
            matchCategory && matchQuery
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val bookmarks: StateFlow<List<SavedBookmark>> = bookmarkDao.getAllBookmarks()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    init {
        // Observe cloud products if signed in
        if (Firebase.auth.currentUser != null) {
            viewModelScope.launch {
                firestoreRepo.observeApprovedProducts()
                    .catch { /* Handled in repository */ }
                    .collect { cloudProducts ->
                        cloudProducts.forEach { cp ->
                            val localProduct = CadreProduct(
                                name = cp.name,
                                businessName = cp.businessName,
                                category = cp.category,
                                price = cp.price,
                                ownerName = cp.ownerName,
                                village = cp.village ?: "Kecamatan Diwek",
                                phone = cp.phone,
                                description = cp.description ?: "",
                                isVerifiedKader = cp.isVerifiedKader
                            )
                            productDao.insertProduct(localProduct)
                        }
                    }
            }
        }
    }

    fun setCategory(category: String) {
        _selectedCategory.value = category
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun addProduct(
        name: String,
        businessName: String,
        category: String,
        price: String,
        ownerName: String,
        village: String,
        phone: String,
        description: String
    ) {
        viewModelScope.launch {
            var formattedPhone = phone.trim().replace("+", "").replace("-", "").replace(" ", "")
            if (formattedPhone.startsWith("0")) {
                formattedPhone = "62" + formattedPhone.substring(1)
            } else if (!formattedPhone.startsWith("62")) {
                formattedPhone = "62$formattedPhone"
            }

            val formattedPrice = if (price.trim().startsWith("Rp", ignoreCase = true)) price.trim() else "Rp ${price.trim()}"

            val newProduct = CadreProduct(
                name = name.trim(),
                businessName = businessName.trim(),
                category = category,
                price = formattedPrice,
                ownerName = ownerName.trim(),
                village = village.trim(),
                phone = formattedPhone,
                description = description.trim(),
                isVerifiedKader = true
            )
            productDao.insertProduct(newProduct)

            // Cloud sync to Firestore if user is authenticated
            val currentUser = Firebase.auth.currentUser
            if (currentUser != null) {
                val cloudId = "prod_" + UUID.randomUUID().toString().replace("-", "")
                val cloudProduct = FirestoreProduct(
                    id = cloudId,
                    userId = currentUser.uid,
                    name = name.trim(),
                    businessName = businessName.trim(),
                    category = category,
                    price = formattedPrice,
                    ownerName = ownerName.trim(),
                    village = village.trim(),
                    phone = formattedPhone,
                    description = description.trim(),
                    isVerifiedKader = true,
                    status = "approved"
                )
                firestoreRepo.addCadreProduct(cloudProduct)
            }
        }
    }

    fun deleteProduct(product: CadreProduct) {
        viewModelScope.launch {
            productDao.deleteProduct(product)
        }
    }

    fun toggleBookmark(id: String, title: String, type: String, isBookmarked: Boolean) {
        viewModelScope.launch {
            if (isBookmarked) {
                bookmarkDao.removeBookmark(id)
            } else {
                bookmarkDao.addBookmark(
                    SavedBookmark(
                        itemId = id,
                        title = title,
                        type = type
                    )
                )
            }
        }
    }
}
