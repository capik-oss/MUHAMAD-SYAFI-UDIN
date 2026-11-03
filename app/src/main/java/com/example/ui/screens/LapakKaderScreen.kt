package com.example.ui.screens

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Agriculture
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Checkroom
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.HealthAndSafety
import androidx.compose.material.icons.filled.NearMe
import androidx.compose.material.icons.filled.Print
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.CadreProduct
import com.example.viewmodel.LapakViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LapakKaderScreen(
    lapakViewModel: LapakViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val products by lapakViewModel.filteredProducts.collectAsState()
    val selectedCategory by lapakViewModel.selectedCategory.collectAsState()
    val searchQuery by lapakViewModel.searchQuery.collectAsState()

    var showAddDialog by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    val categories = listOf("Semua", "Kuliner", "Fashion & Atribut", "Jasa & Percetakan", "Agribisnis", "Toko/Retail")

    Box(
        modifier = modifier
            .fillMaxSize()
            .testTag("lapak_kader_screen")
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Header
            Surface(
                color = MaterialTheme.colorScheme.surface,
                shadowElevation = 2.dp
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Lapak Kader Diwek",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.ExtraBold,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Text(
                                text = "Pemberdayaan Ekonomi & UMKM Sahabat Ansor",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = MaterialTheme.colorScheme.secondaryContainer
                        ) {
                            Text(
                                text = "${products.size} Produk",
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSecondaryContainer,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Search input
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { lapakViewModel.setSearchQuery(it) },
                        placeholder = { Text("Cari produk, desa di Diwek, atau kader...") },
                        leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Cari") },
                        trailingIcon = {
                            if (searchQuery.isNotEmpty()) {
                                IconButton(onClick = { lapakViewModel.setSearchQuery("") }) {
                                    Icon(Icons.Default.Clear, contentDescription = "Hapus")
                                }
                            }
                        },
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("input_search_lapak")
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Categories horizontal scroll
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        items(categories) { category ->
                            val isSelected = selectedCategory == category
                            FilterChip(
                                selected = isSelected,
                                onClick = { lapakViewModel.setCategory(category) },
                                label = { Text(category) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = MaterialTheme.colorScheme.primary,
                                    selectedLabelColor = MaterialTheme.colorScheme.onPrimary
                                ),
                                modifier = Modifier.testTag("chip_cat_$category")
                            )
                        }
                    }
                }
            }

            // Products List
            if (products.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Default.Storefront,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.outline,
                            modifier = Modifier.size(56.dp)
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = "Belum Ada Produk di Kategori Ini",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Daftarkan usaha Anda dengan tombol di bawah!",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.outline
                        )
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 12.dp, bottom = 120.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    items(products, key = { it.id }) { product ->
                        ProductCard(
                            product = product,
                            onContactWhatsApp = {
                                val message = "Assalamu'alaikum Sahabat ${product.ownerName}, saya melihat produk '${product.name}' di Lapak Kader Ansor Diwek. Mau tanya info pemesanan."
                                val url = "https://wa.me/${product.phone}?text=${Uri.encode(message)}"
                                try {
                                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                                    context.startActivity(intent)
                                } catch (e: Exception) {
                                    Toast.makeText(context, "Tidak dapat membuka WhatsApp", Toast.LENGTH_SHORT).show()
                                }
                            },
                            onShare = {
                                val shareText = "🛍️ Lapak Kader Ansor Diwek:\n\n*${product.name}*\nUsaha: ${product.businessName}\nHarga: ${product.price}\nDesa: ${product.village}\nPemilik: Sahabat ${product.ownerName}\n\nHubungi via WA: https://wa.me/${product.phone}"
                                val intent = Intent(Intent.ACTION_SEND).apply {
                                    type = "text/plain"
                                    putExtra(Intent.EXTRA_TEXT, shareText)
                                }
                                context.startActivity(Intent.createChooser(intent, "Bagikan Produk"))
                            }
                        )
                    }
                }
            }
        }

        // Floating Action Button
        ExtendedFloatingActionButton(
            onClick = { showAddDialog = true },
            icon = { Icon(Icons.Default.Add, contentDescription = null) },
            text = { Text("Daftarkan Usaha") },
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 16.dp, bottom = 96.dp)
                .testTag("fab_add_product")
        )

        // Add Product Bottom Sheet
        if (showAddDialog) {
            ModalBottomSheet(
                onDismissRequest = { showAddDialog = false },
                sheetState = sheetState,
                shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
            ) {
                AddProductForm(
                    onDismiss = {
                        coroutineScope.launch {
                            sheetState.hide()
                            showAddDialog = false
                        }
                    },
                    onSubmit = { name, businessName, category, price, owner, village, phone, desc ->
                        lapakViewModel.addProduct(
                            name = name,
                            businessName = businessName,
                            category = category,
                            price = price,
                            ownerName = owner,
                            village = village,
                            phone = phone,
                            description = desc
                        )
                        Toast.makeText(context, "Produk berhasil didaftarkan di Lapak Kader!", Toast.LENGTH_SHORT).show()
                        coroutineScope.launch {
                            sheetState.hide()
                            showAddDialog = false
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun ProductCard(
    product: CadreProduct,
    onContactWhatsApp: () -> Unit,
    onShare: () -> Unit
) {
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("card_product_${product.id}"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Category & Badge Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = MaterialTheme.colorScheme.primaryContainer
                ) {
                    Text(
                        text = product.category,
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }

                if (product.isVerifiedKader) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = "Terverifikasi",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Kader Ansor Diwek",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Title & Business
            Text(
                text = product.name,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Text(
                text = "${product.businessName} • Sahabat ${product.ownerName}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(6.dp))

            // Price tag
            Text(
                text = product.price,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.secondary
            )

            Spacer(modifier = Modifier.height(6.dp))

            // Description
            Text(
                text = product.description,
                style = MaterialTheme.typography.bodyMedium,
                lineHeight = 20.sp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.85f)
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Location
            Surface(
                shape = RoundedCornerShape(6.dp),
                color = MaterialTheme.colorScheme.surfaceVariant
            ) {
                Text(
                    text = "📍 ${product.village}",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Contact WA Action
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Button(
                    onClick = onContactWhatsApp,
                    modifier = Modifier
                        .weight(1f)
                        .testTag("btn_wa_${product.id}"),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    ),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.NearMe,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Hubungi via WA",
                        fontWeight = FontWeight.Bold
                    )
                }

                OutlinedButton(
                    onClick = onShare,
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.testTag("btn_share_${product.id}")
                ) {
                    Icon(
                        imageVector = Icons.Default.Share,
                        contentDescription = "Bagikan",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddProductForm(
    onDismiss: () -> Unit,
    onSubmit: (name: String, businessName: String, category: String, price: String, owner: String, village: String, phone: String, desc: String) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var businessName by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("Kuliner") }
    var price by remember { mutableStateOf("") }
    var ownerName by remember { mutableStateOf("") }
    var village by remember { mutableStateOf("Ranting Cukir, Diwek") }
    var phone by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }

    var categoryExpanded by remember { mutableStateOf(false) }
    var villageExpanded by remember { mutableStateOf(false) }

    val categories = listOf("Kuliner", "Fashion & Atribut", "Jasa & Percetakan", "Agribisnis", "Toko/Retail")
    val villages = listOf(
        "Ranting Cukir, Diwek",
        "Ranting Kwaron, Diwek",
        "Ranting Diwek Pusat",
        "Ranting Brambang, Diwek",
        "Ranting Bulurejo, Diwek",
        "Ranting Jatipelem, Diwek",
        "Ranting Kayangan, Diwek",
        "Ranting Bandung, Diwek",
        "Ranting Balongbesuk, Diwek",
        "Ranting Pandanwangi, Diwek",
        "Ranting Puton, Diwek",
        "Ranting Keras, Diwek",
        "Ranting Kedawong, Diwek",
        "Ranting Jatirejo, Diwek",
        "Ranting Ngudirejo, Diwek",
        "Ranting Watugaluh, Diwek",
        "Ranting Bendet, Diwek",
        "Ranting Godong, Diwek"
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 12.dp)
            .verticalScroll(rememberScrollState())
            .testTag("form_add_product")
    ) {
        Text(
            text = "Daftarkan Lapak Usaha Kader",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = "Pemberdayaan ekonomi sesama sahabat Gerakan Pemuda Ansor Diwek",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Nama Produk / Jasa") },
            placeholder = { Text("Contoh: Kopi Bubuk Santri Robusta") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth().testTag("input_form_name")
        )

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = businessName,
            onValueChange = { businessName = it },
            label = { Text("Nama Toko / Usaha") },
            placeholder = { Text("Contoh: Kedai Barokah Ansor") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth().testTag("input_form_biz")
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Category Dropdown
        ExposedDropdownMenuBox(
            expanded = categoryExpanded,
            onExpandedChange = { categoryExpanded = it },
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedTextField(
                value = category,
                onValueChange = {},
                readOnly = true,
                label = { Text("Kategori Produk") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = categoryExpanded) },
                modifier = Modifier.menuAnchor().fillMaxWidth()
            )
            ExposedDropdownMenu(
                expanded = categoryExpanded,
                onDismissRequest = { categoryExpanded = false }
            ) {
                categories.forEach { cat ->
                    DropdownMenuItem(
                        text = { Text(cat) },
                        onClick = {
                            category = cat
                            categoryExpanded = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = price,
            onValueChange = { price = it },
            label = { Text("Estimasi Harga") },
            placeholder = { Text("Contoh: Rp 25.000") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth().testTag("input_form_price")
        )

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = ownerName,
            onValueChange = { ownerName = it },
            label = { Text("Nama Kader Pemilik") },
            placeholder = { Text("Contoh: Sahabat Ahmad") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth().testTag("input_form_owner")
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Village / Ranting Dropdown
        ExposedDropdownMenuBox(
            expanded = villageExpanded,
            onExpandedChange = { villageExpanded = it },
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedTextField(
                value = village,
                onValueChange = {},
                readOnly = true,
                label = { Text("Ranting / Desa di Diwek") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = villageExpanded) },
                modifier = Modifier.menuAnchor().fillMaxWidth()
            )
            ExposedDropdownMenu(
                expanded = villageExpanded,
                onDismissRequest = { villageExpanded = false }
            ) {
                villages.forEach { vil ->
                    DropdownMenuItem(
                        text = { Text(vil) },
                        onClick = {
                            village = vil
                            villageExpanded = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = phone,
            onValueChange = { phone = it },
            label = { Text("Nomor WhatsApp") },
            placeholder = { Text("Contoh: 08123456789") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth().testTag("input_form_phone")
        )

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            label = { Text("Deskripsi Singkat Usaha") },
            placeholder = { Text("Jelaskan keunggulan produk atau layanan Anda...") },
            minLines = 3,
            modifier = Modifier.fillMaxWidth().testTag("input_form_desc")
        )

        Spacer(modifier = Modifier.height(18.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedButton(
                onClick = onDismiss,
                modifier = Modifier.weight(1f)
            ) {
                Text("Batal")
            }

            Button(
                onClick = {
                    if (name.isBlank() || price.isBlank() || ownerName.isBlank() || phone.isBlank()) {
                        // validation handled via required fields
                    } else {
                        onSubmit(name, businessName, category, price, ownerName, village, phone, description)
                    }
                },
                enabled = name.isNotBlank() && price.isNotBlank() && ownerName.isNotBlank() && phone.isNotBlank(),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                modifier = Modifier
                    .weight(1f)
                    .testTag("btn_submit_product")
            ) {
                Text("Simpan Lapak")
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}
