package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.credentials.CredentialManager
import com.example.util.GoogleAuthHelper

import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.screens.AdminPanelScreen
import com.example.ui.screens.AgendaCalendarScreen
import com.example.ui.screens.AiConsultantScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.LapakKaderScreen
import com.example.ui.screens.LoginScreen
import com.example.ui.screens.MarsScreen
import com.example.ui.screens.PdPrtScreen
import com.example.ui.theme.SahabatAnsorTheme
import com.example.util.configureAppCheck
import com.example.viewmodel.AgendaViewModel
import com.example.viewmodel.AiViewModel
import com.example.viewmodel.AuthViewModel
import com.example.viewmodel.LapakViewModel

enum class AnsorTab(val label: String, val icon: ImageVector, val testTag: String) {
    HOME("Beranda", Icons.Default.Home, "nav_item_home"),
    AGENDA("Agenda", Icons.Default.CalendarMonth, "nav_item_agenda"),
    PDPRT("PD/PRT", Icons.AutoMirrored.Filled.MenuBook, "nav_item_pdprt"),
    LAPAK("Lapak", Icons.Default.Storefront, "nav_item_lapak"),
    AI("Tanya AI", Icons.Default.AutoAwesome, "nav_item_ai"),
    AKUN("Akun", Icons.Default.AccountCircle, "nav_item_akun"),
    MARS("Mars", Icons.Default.MusicNote, "nav_item_mars") // Sub-destination accessible from Home
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // STEP 1: Mandatory App Check configuration with launch intent before any UI or Firebase operations
        configureAppCheck(this, intent)

        enableEdgeToEdge()

        setContent {
            SahabatAnsorTheme {
                MainAppContainer()
            }
        }
    }
}

@Composable
fun MainAppContainer() {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val authViewModel: AuthViewModel = viewModel()
    val lapakViewModel: LapakViewModel = viewModel()
    val agendaViewModel: AgendaViewModel = viewModel()
    val aiViewModel: AiViewModel = viewModel()

    LaunchedEffect(Unit) {
        val credentialManager = CredentialManager.create(context)
        GoogleAuthHelper.attemptAutoSignIn(
            context = context,
            credentialManager = credentialManager,
            onAuthSuccess = { },
            onUnauthenticated = { },
            scope = coroutineScope
        )
    }

    var currentTab by rememberSaveable { mutableStateOf(AnsorTab.HOME) }
    var showAdminPanel by rememberSaveable { mutableStateOf(false) }


    // Android Back button handling
    if (showAdminPanel) {
        BackHandler {
            showAdminPanel = false
        }
    } else if (currentTab != AnsorTab.HOME) {
        BackHandler {
            currentTab = AnsorTab.HOME
        }
    }

    // Main navigation tabs visible on bottom bar (excluding secondary sub-destination MARS)
    val bottomNavTabs = listOf(
        AnsorTab.HOME,
        AnsorTab.AGENDA,
        AnsorTab.PDPRT,
        AnsorTab.LAPAK,
        AnsorTab.AI,
        AnsorTab.AKUN
    )

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            if (!showAdminPanel) {
                NavigationBar(
                    containerColor = MaterialTheme.colorScheme.surface,
                    modifier = Modifier.testTag("bottom_nav_bar")
                ) {
                    bottomNavTabs.forEach { tab ->
                        val isSelected = currentTab == tab
                        NavigationBarItem(
                            selected = isSelected,
                            onClick = { currentTab = tab },
                            icon = {
                                Icon(
                                    imageVector = tab.icon,
                                    contentDescription = tab.label
                                )
                            },
                            label = {
                                Text(
                                    text = tab.label,
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                )
                            },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = MaterialTheme.colorScheme.onPrimaryContainer,
                                indicatorColor = MaterialTheme.colorScheme.primaryContainer,
                                selectedTextColor = MaterialTheme.colorScheme.primary
                            ),
                            modifier = Modifier.testTag(tab.testTag)
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            if (showAdminPanel) {
                AdminPanelScreen(
                    authViewModel = authViewModel,
                    lapakViewModel = lapakViewModel,
                    agendaViewModel = agendaViewModel,
                    onBackToHome = { showAdminPanel = false }
                )
            } else {
                when (currentTab) {
                    AnsorTab.HOME -> {
                        HomeScreen(
                            lapakViewModel = lapakViewModel,
                            agendaViewModel = agendaViewModel,
                            authViewModel = authViewModel,
                            onNavigateToLogin = { currentTab = AnsorTab.AKUN },
                            onNavigateToAdminPanel = { showAdminPanel = true },
                            onNavigateToAgenda = { currentTab = AnsorTab.AGENDA },
                            onNavigateToPdPrt = { currentTab = AnsorTab.PDPRT },
                            onNavigateToMars = { currentTab = AnsorTab.MARS },
                            onNavigateToLapak = { currentTab = AnsorTab.LAPAK },
                            onNavigateToAi = { currentTab = AnsorTab.AI }
                        )
                    }
                    AnsorTab.AGENDA -> {
                        AgendaCalendarScreen(agendaViewModel = agendaViewModel)
                    }
                    AnsorTab.PDPRT -> {
                        PdPrtScreen(lapakViewModel = lapakViewModel)
                    }
                    AnsorTab.MARS -> {
                        MarsScreen()
                    }
                    AnsorTab.LAPAK -> {
                        LapakKaderScreen(lapakViewModel = lapakViewModel)
                    }
                    AnsorTab.AI -> {
                        AiConsultantScreen(aiViewModel = aiViewModel)
                    }
                    AnsorTab.AKUN -> {
                        LoginScreen(
                            authViewModel = authViewModel,
                            onNavigateToAdminPanel = { showAdminPanel = true }
                        )
                    }
                }
            }
        }
    }
}
