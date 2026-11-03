package com.example.viewmodel

import android.app.Application
import android.content.Context
import android.content.Intent
import android.provider.CalendarContract
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.AnsorDatabase
import com.example.data.model.AnsorEvent
import com.example.data.model.EventCategory
import com.example.data.model.FirestoreEvent
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
import java.util.Calendar
import java.util.UUID

class AgendaViewModel(application: Application) : AndroidViewModel(application) {
    private val database = AnsorDatabase.getDatabase(application)
    private val eventDao = database.eventDao()
    private val firestoreRepo = AnsorFirestoreRepository(application)

    private val _selectedCategory = MutableStateFlow<EventCategory?>(null)
    val selectedCategory: StateFlow<EventCategory?> = _selectedCategory.asStateFlow()

    private val _selectedDay = MutableStateFlow<Int?>(null) // null = all days in month
    val selectedDay: StateFlow<Int?> = _selectedDay.asStateFlow()

    private val _currentMonth = MutableStateFlow(11) // November
    val currentMonth: StateFlow<Int> = _currentMonth.asStateFlow()

    private val _currentYear = MutableStateFlow(2026)
    val currentYear: StateFlow<Int> = _currentYear.asStateFlow()

    val allEvents: StateFlow<List<AnsorEvent>> = eventDao.getAllEvents()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val filteredEvents: StateFlow<List<AnsorEvent>> = combine(
        allEvents,
        _selectedCategory,
        _selectedDay,
        _currentMonth,
        _currentYear
    ) { events, category, day, month, year ->
        events.filter { event ->
            val matchMonth = event.monthNumber == month && event.year == year
            val matchCategory = category == null || event.category == category
            val matchDay = day == null || event.dayOfMonth == day
            matchMonth && matchCategory && matchDay
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    init {
        // Observe cloud events if authenticated
        if (Firebase.auth.currentUser != null) {
            viewModelScope.launch {
                firestoreRepo.observeAnsorEvents()
                    .catch { /* Handled in repo */ }
                    .collect { cloudEvents ->
                        cloudEvents.forEach { ce ->
                            val cat = try {
                                EventCategory.valueOf(ce.category.uppercase())
                            } catch (e: Exception) {
                                EventCategory.MAJLIS
                            }
                            val local = AnsorEvent(
                                title = ce.title,
                                category = cat,
                                dayOfMonth = ce.dayOfMonth.toInt(),
                                monthNumber = ce.monthNumber.toInt(),
                                year = ce.year.toInt(),
                                dayName = ce.dayName ?: "Hari Kegiatan",
                                timeDisplay = ce.timeDisplay ?: "19.30 WIB",
                                location = ce.location,
                                organizer = ce.organizer,
                                description = ce.description ?: "",
                                dressCode = ce.dressCode ?: "Baju Koko / Batik NU",
                                isRantingEvent = ce.isRantingEvent,
                                village = ce.village ?: "Kecamatan Diwek"
                            )
                            eventDao.insertEvent(local)
                        }
                    }
            }
        }
    }

    fun selectCategory(category: EventCategory?) {
        _selectedCategory.value = category
    }

    fun selectDay(day: Int?) {
        _selectedDay.value = day
    }

    fun nextMonth() {
        if (_currentMonth.value == 12) {
            _currentMonth.value = 1
            _currentYear.value += 1
        } else {
            _currentMonth.value += 1
        }
        _selectedDay.value = null
    }

    fun previousMonth() {
        if (_currentMonth.value == 1) {
            _currentMonth.value = 12
            _currentYear.value -= 1
        } else {
            _currentMonth.value -= 1
        }
        _selectedDay.value = null
    }

    fun addEvent(
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
    ) {
        viewModelScope.launch {
            val newEvent = AnsorEvent(
                title = title.trim(),
                category = category,
                dayOfMonth = dayOfMonth,
                monthNumber = monthNumber,
                year = year,
                dayName = dayName.trim(),
                timeDisplay = timeDisplay.trim(),
                location = location.trim(),
                organizer = organizer.trim(),
                description = description.trim(),
                dressCode = dressCode.trim().ifBlank { "Bebas Rapi / Berbusana Muslim" },
                village = village.trim()
            )
            eventDao.insertEvent(newEvent)

            // Cloud sync if authenticated
            val currentUser = Firebase.auth.currentUser
            if (currentUser != null) {
                val cloudId = "event_" + UUID.randomUUID().toString().replace("-", "")
                val cloudEvent = FirestoreEvent(
                    id = cloudId,
                    title = title.trim(),
                    category = category.name,
                    dayOfMonth = dayOfMonth.toLong(),
                    monthNumber = monthNumber.toLong(),
                    year = year.toLong(),
                    dayName = dayName.trim(),
                    timeDisplay = timeDisplay.trim(),
                    location = location.trim(),
                    organizer = organizer.trim(),
                    description = description.trim(),
                    dressCode = dressCode.trim(),
                    village = village.trim(),
                    creatorId = currentUser.uid
                )
                firestoreRepo.addAnsorEvent(cloudEvent)
            }
        }
    }

    fun addToDeviceCalendar(context: Context, event: AnsorEvent) {
        try {
            val cal = Calendar.getInstance().apply {
                set(Calendar.YEAR, event.year)
                set(Calendar.MONTH, event.monthNumber - 1)
                set(Calendar.DAY_OF_MONTH, event.dayOfMonth)
                set(Calendar.HOUR_OF_DAY, 19)
                set(Calendar.MINUTE, 30)
            }

            val intent = Intent(Intent.ACTION_INSERT).apply {
                data = CalendarContract.Events.CONTENT_URI
                putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, cal.timeInMillis)
                putExtra(CalendarContract.EXTRA_EVENT_END_TIME, cal.timeInMillis + (2 * 60 * 60 * 1000))
                putExtra(CalendarContract.Events.TITLE, event.title)
                putExtra(CalendarContract.Events.DESCRIPTION, "${event.description}\n\nPenyelenggara: ${event.organizer}\nDress Code: ${event.dressCode}\n(Sahabat Ansor Diwek)")
                putExtra(CalendarContract.Events.EVENT_LOCATION, event.location)
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
        } catch (e: Exception) {
            // fallback
        }
    }
}
