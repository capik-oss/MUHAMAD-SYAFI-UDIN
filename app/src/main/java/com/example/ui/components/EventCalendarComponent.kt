package com.example.ui.components

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Checkroom
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.EventAvailable
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AnsorEvent
import com.example.data.model.EventCategory
import com.example.viewmodel.AgendaViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventCalendarComponent(
    agendaViewModel: AgendaViewModel,
    modifier: Modifier = Modifier,
    isEmbeddedInHome: Boolean = false,
    onViewAllClick: (() -> Unit)? = null
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    val currentMonth by agendaViewModel.currentMonth.collectAsState()
    val currentYear by agendaViewModel.currentYear.collectAsState()
    val selectedDay by agendaViewModel.selectedDay.collectAsState()
    val selectedCategory by agendaViewModel.selectedCategory.collectAsState()
    val allEvents by agendaViewModel.allEvents.collectAsState()
    val filteredEvents by agendaViewModel.filteredEvents.collectAsState()

    var showAddEventSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    val monthNames = listOf(
        "Januari", "Februari", "Maret", "April", "Mei", "Juni",
        "Juli", "Agustus", "September", "Oktober", "November", "Desember"
    )
    val monthLabel = "${monthNames.getOrElse(currentMonth - 1) { "Bulan" }} $currentYear"

    // Days in current month with events
    val monthEvents = allEvents.filter { it.monthNumber == currentMonth && it.year == currentYear }
    val daysWithEvents = monthEvents.map { it.dayOfMonth }.toSet()

    val dayScrollState = rememberLazyListState()

    // Scroll to active day if selected
    LaunchedEffect(selectedDay) {
        selectedDay?.let { day ->
            if (day > 3) {
                dayScrollState.animateScrollToItem(day - 3)
            }
        }
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .testTag("event_calendar_component")
    ) {
        // Month navigation header
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 2.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.CalendarToday,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = monthLabel,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                        Text(
                            text = "${monthEvents.size} Agenda Kegiatan di PAC Diwek",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(
                            onClick = { agendaViewModel.previousMonth() },
                            modifier = Modifier
                                .size(36.dp)
                                .testTag("btn_prev_month")
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Bulan Sebelumnya",
                                modifier = Modifier.size(18.dp)
                            )
                        }

                        IconButton(
                            onClick = { agendaViewModel.nextMonth() },
                            modifier = Modifier
                                .size(36.dp)
                                .testTag("btn_next_month")
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                contentDescription = "Bulan Berikutnya",
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Date Strip Picker
                LazyRow(
                    state = dayScrollState,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    // "All Days" pill
                    item {
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = if (selectedDay == null) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
                            modifier = Modifier
                                .clickable { agendaViewModel.selectDay(null) }
                                .testTag("day_filter_all")
                        ) {
                            Column(
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "Semua",
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = if (selectedDay == null) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Text(
                                    text = "Hari",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = if (selectedDay == null) MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f) else MaterialTheme.colorScheme.outline
                                )
                            }
                        }
                    }

                    // 1 to 30 days
                    items(30) { index ->
                        val day = index + 1
                        val isSelected = selectedDay == day
                        val hasEvent = daysWithEvents.contains(day)

                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = when {
                                isSelected -> MaterialTheme.colorScheme.primary
                                hasEvent -> MaterialTheme.colorScheme.primaryContainer
                                else -> MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)
                            },
                            border = if (hasEvent && !isSelected) {
                                androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.primary)
                            } else null,
                            modifier = Modifier
                                .clickable {
                                    if (selectedDay == day) agendaViewModel.selectDay(null)
                                    else agendaViewModel.selectDay(day)
                                }
                                .testTag("day_filter_$day")
                        ) {
                            Column(
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "Tgl",
                                    style = MaterialTheme.typography.labelSmall,
                                    fontSize = 10.sp,
                                    color = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.outline
                                )
                                Text(
                                    text = "$day",
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = when {
                                        isSelected -> MaterialTheme.colorScheme.onPrimary
                                        hasEvent -> MaterialTheme.colorScheme.onPrimaryContainer
                                        else -> MaterialTheme.colorScheme.onSurfaceVariant
                                    }
                                )
                                if (hasEvent) {
                                    Box(
                                        modifier = Modifier
                                            .size(5.dp)
                                            .clip(CircleShape)
                                            .background(if (isSelected) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.primary)
                                    )
                                } else {
                                    Spacer(modifier = Modifier.size(5.dp))
                                }
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Category Filter Chips
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            item {
                FilterChip(
                    selected = selectedCategory == null,
                    onClick = { agendaViewModel.selectCategory(null) },
                    label = { Text("Semua Kategori") },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = MaterialTheme.colorScheme.primary,
                        selectedLabelColor = MaterialTheme.colorScheme.onPrimary
                    ),
                    modifier = Modifier.testTag("filter_cat_all")
                )
            }

            items(EventCategory.values()) { category ->
                val isSelected = selectedCategory == category
                FilterChip(
                    selected = isSelected,
                    onClick = {
                        if (isSelected) agendaViewModel.selectCategory(null)
                        else agendaViewModel.selectCategory(category)
                    },
                    label = { Text(category.label) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = Color(category.badgeColor),
                        selectedLabelColor = Color.White
                    ),
                    modifier = Modifier.testTag("filter_cat_${category.name}")
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Header for Action and List count
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = if (selectedDay != null) "Agenda Tgl $selectedDay $monthLabel" else "Daftar Kegiatan Terdekat",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Row {
                Button(
                    onClick = { showAddEventSheet = true },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary),
                    shape = RoundedCornerShape(10.dp),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                    modifier = Modifier.testTag("btn_open_add_event")
                ) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = "Tambah", style = MaterialTheme.typography.labelSmall)
                }

                if (isEmbeddedInHome && onViewAllClick != null) {
                    Spacer(modifier = Modifier.width(6.dp))
                    OutlinedButton(
                        onClick = onViewAllClick,
                        shape = RoundedCornerShape(10.dp),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text(text = "Kalender", style = MaterialTheme.typography.labelSmall)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Events List Cards
        if (filteredEvents.isEmpty()) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Default.EventAvailable,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.outline,
                        modifier = Modifier.size(36.dp)
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Tidak Ada Agenda pada Filter Ini",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Gunakan tombol Tambah untuk menjadwalkan majelis/rapat baru.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.outline
                    )
                }
            }
        } else {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                filteredEvents.forEach { event ->
                    EventCardItem(
                        event = event,
                        onAddToCalendar = {
                            agendaViewModel.addToDeviceCalendar(context, event)
                            Toast.makeText(context, "Membuka kalender HP...", Toast.LENGTH_SHORT).show()
                        },
                        onShare = {
                            val shareText = buildString {
                                appendLine("📅 *${event.title}*")
                                appendLine("Kategori: ${event.category.label}")
                                appendLine("Hari/Tgl: ${event.dayName}, ${event.dayOfMonth} $monthLabel")
                                appendLine("Waktu: ${event.timeDisplay}")
                                appendLine("Lokasi: 📍 ${event.location}")
                                appendLine("Penyelenggara: ${event.organizer}")
                                appendLine("Dress Code: ${event.dressCode}")
                                appendLine()
                                appendLine(event.description)
                                appendLine()
                                appendLine("_(Diinformasikan melalui Aplikasi Sahabat Ansor Diwek)_")
                            }
                            val intent = Intent(Intent.ACTION_SEND).apply {
                                type = "text/plain"
                                putExtra(Intent.EXTRA_TEXT, shareText)
                            }
                            context.startActivity(Intent.createChooser(intent, "Bagikan Undangan Kegiatan"))
                        }
                    )
                }
            }
        }
    }

    // Modal Sheet to Add Event
    if (showAddEventSheet) {
        ModalBottomSheet(
            onDismissRequest = { showAddEventSheet = false },
            sheetState = sheetState,
            shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
        ) {
            AddEventForm(
                initialMonth = currentMonth,
                initialYear = currentYear,
                onDismiss = {
                    coroutineScope.launch {
                        sheetState.hide()
                        showAddEventSheet = false
                    }
                },
                onSubmit = { title, cat, day, month, yr, dayName, time, loc, org, desc, dress, vil ->
                    agendaViewModel.addEvent(title, cat, day, month, yr, dayName, time, loc, org, desc, dress, vil)
                    Toast.makeText(context, "Agenda berhasil ditambahkan ke kalender!", Toast.LENGTH_SHORT).show()
                    coroutineScope.launch {
                        sheetState.hide()
                        showAddEventSheet = false
                    }
                }
            )
        }
    }
}

@Composable
fun EventCardItem(
    event: AnsorEvent,
    onAddToCalendar: () -> Unit,
    onShare: () -> Unit
) {
    var isExpanded by remember { mutableStateOf(false) }

    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("card_event_${event.id}"),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 1.5.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            // Top Row: Date Badge, Category Pill, and Calendar Icon
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Date box
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = MaterialTheme.colorScheme.primaryContainer,
                        modifier = Modifier.padding(end = 8.dp)
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = "${event.dayOfMonth}",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.ExtraBold,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                            Text(
                                text = "NOV",
                                style = MaterialTheme.typography.labelSmall,
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }

                    Column {
                        // Category pill
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = Color(event.category.badgeColor)
                        ) {
                            Text(
                                text = event.category.label,
                                style = MaterialTheme.typography.labelSmall,
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = event.dayName,
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }

                Row {
                    IconButton(
                        onClick = onAddToCalendar,
                        modifier = Modifier
                            .size(36.dp)
                            .testTag("btn_add_to_cal_${event.id}")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Event,
                            contentDescription = "Simpan ke Kalender HP",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    IconButton(
                        onClick = onShare,
                        modifier = Modifier
                            .size(36.dp)
                            .testTag("btn_share_event_${event.id}")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = "Bagikan",
                            tint = MaterialTheme.colorScheme.outline,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Title
            Text(
                text = event.title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Time & Location
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Schedule,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.outline,
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = event.timeDisplay,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.height(2.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.outline,
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = event.location,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // Expandable details
            AnimatedVisibility(visible = isExpanded) {
                Column(modifier = Modifier.padding(top = 8.dp)) {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Groups, contentDescription = null, modifier = Modifier.size(14.dp), tint = MaterialTheme.colorScheme.primary)
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "Penyelenggara: ${event.organizer}",
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            Spacer(modifier = Modifier.height(4.dp))

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Checkroom, contentDescription = null, modifier = Modifier.size(14.dp), tint = MaterialTheme.colorScheme.secondary)
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "Dress Code: ${event.dressCode}",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }

                            Spacer(modifier = Modifier.height(6.dp))

                            Text(
                                text = event.description,
                                style = MaterialTheme.typography.bodySmall,
                                lineHeight = 18.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            // Expand / collapse trigger
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { isExpanded = !isExpanded },
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (isExpanded) "Sembunyikan Rincian" else "Lihat Rincian Agenda",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.SemiBold
                )
                Icon(
                    imageVector = if (isExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEventForm(
    initialMonth: Int,
    initialYear: Int,
    onDismiss: () -> Unit,
    onSubmit: (
        title: String,
        category: EventCategory,
        dayOfMonth: Int,
        monthNumber: Int,
        year: Int,
        dayName: String,
        timeDisplay: String,
        location: String,
        organizer: String,
        description: String,
        dressCode: String,
        village: String
    ) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf(EventCategory.MAJLIS) }
    var dayOfMonth by remember { mutableIntStateOf(10) }
    var dayName by remember { mutableStateOf("Kamis Malam Jum'at") }
    var timeDisplay by remember { mutableStateOf("19.30 - 22.00 WIB") }
    var location by remember { mutableStateOf("") }
    var organizer by remember { mutableStateOf("MDS Rijalul Ansor PAC Diwek") }
    var description by remember { mutableStateOf("") }
    var dressCode by remember { mutableStateOf("Baju Koko / Batik NU / Sarung") }
    var village by remember { mutableStateOf("Kecamatan Diwek") }

    var categoryExpanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 12.dp)
            .verticalScroll(rememberScrollState())
            .testTag("form_add_event")
    ) {
        Text(
            text = "Jadwalkan Agenda Kegiatan",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = "Rapat lokal, majelis dzikir (study circle), dan aktivitas sosial anggota",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(14.dp))

        OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("Nama Kegiatan") },
            placeholder = { Text("Contoh: Rutinan Majlis Rijalul Ansor") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth().testTag("input_event_title")
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Category dropdown
        ExposedDropdownMenuBox(
            expanded = categoryExpanded,
            onExpandedChange = { categoryExpanded = it },
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedTextField(
                value = selectedCategory.label,
                onValueChange = {},
                readOnly = true,
                label = { Text("Kategori Kegiatan") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = categoryExpanded) },
                modifier = Modifier.menuAnchor().fillMaxWidth()
            )
            ExposedDropdownMenu(
                expanded = categoryExpanded,
                onDismissRequest = { categoryExpanded = false }
            ) {
                EventCategory.values().forEach { cat ->
                    DropdownMenuItem(
                        text = { Text(cat.label) },
                        onClick = {
                            selectedCategory = cat
                            categoryExpanded = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            OutlinedTextField(
                value = "$dayOfMonth",
                onValueChange = { dayOfMonth = it.toIntOrNull()?.coerceIn(1, 31) ?: 1 },
                label = { Text("Tanggal") },
                singleLine = true,
                modifier = Modifier.weight(1f).testTag("input_event_day")
            )

            OutlinedTextField(
                value = dayName,
                onValueChange = { dayName = it },
                label = { Text("Hari") },
                placeholder = { Text("Ahad Pagi") },
                singleLine = true,
                modifier = Modifier.weight(2f).testTag("input_event_dayname")
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = timeDisplay,
            onValueChange = { timeDisplay = it },
            label = { Text("Waktu Pelaksanaan") },
            placeholder = { Text("19.30 - 22.00 WIB") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth().testTag("input_event_time")
        )

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = location,
            onValueChange = { location = it },
            label = { Text("Tempat / Lokasi") },
            placeholder = { Text("Masjid Ranting Cukir / Gedung MWC Diwek") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth().testTag("input_event_loc")
        )

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = organizer,
            onValueChange = { organizer = it },
            label = { Text("Penyelenggara / Ranting") },
            placeholder = { Text("MDS Rijalul Ansor / Banser / Ranting...") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth().testTag("input_event_org")
        )

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = dressCode,
            onValueChange = { dressCode = it },
            label = { Text("Dress Code / Seragam") },
            placeholder = { Text("Baju Koko / PDL Banser / Batik") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth().testTag("input_event_dress")
        )

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            label = { Text("Deskripsi & Susunan Acara") },
            placeholder = { Text("Tuliskan agenda pembahasan, susunan acara, narasumber...") },
            minLines = 3,
            modifier = Modifier.fillMaxWidth().testTag("input_event_desc")
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            OutlinedButton(
                onClick = onDismiss,
                modifier = Modifier.weight(1f)
            ) {
                Text("Batal")
            }

            Button(
                onClick = {
                    if (title.isNotBlank() && location.isNotBlank()) {
                        onSubmit(
                            title, selectedCategory, dayOfMonth, initialMonth, initialYear,
                            dayName, timeDisplay, location, organizer, description, dressCode, village
                        )
                    }
                },
                enabled = title.isNotBlank() && location.isNotBlank(),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                modifier = Modifier.weight(1f).testTag("btn_submit_event")
            ) {
                Text("Simpan Agenda")
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}
