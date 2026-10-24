package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.model.AppLanguage
import com.example.ui.components.AppBottomNav
import com.example.ui.components.AppTopBar
import com.example.ui.components.BlockedUsersDialog
import com.example.ui.components.ContactRequestDialog
import com.example.ui.components.DeleteAccountDialog
import com.example.ui.components.ProfileDetailDialog
import com.example.ui.components.ReportBlockDialog
import com.example.ui.components.SafetyGuidelinesDialog
import com.example.ui.screens.AuthScreen
import com.example.ui.screens.FavoritesScreen
import com.example.ui.screens.MatchesFeedScreen
import com.example.ui.screens.PremiumScreen
import com.example.ui.screens.ProfileCreationPage
import com.example.ui.screens.SearchScreen
import com.example.ui.screens.SettingsPrivacyScreen
import com.example.ui.screens.WelcomeScreen
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.viewmodel.RishtaViewModel
import com.example.ui.viewmodel.Screen
import kotlinx.coroutines.flow.collectLatest

class MainActivity : ComponentActivity() {

    private val viewModel: RishtaViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                RishtaApp(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun RishtaApp(viewModel: RishtaViewModel) {
    val currentScreen by viewModel.currentScreen.collectAsStateWithLifecycle()
    val language by viewModel.currentLanguage.collectAsStateWithLifecycle()
    val userAccount by viewModel.userAccount.collectAsStateWithLifecycle()
    val favorites by viewModel.favorites.collectAsStateWithLifecycle()
    val blocked by viewModel.blocked.collectAsStateWithLifecycle()
    val matches by viewModel.matches.collectAsStateWithLifecycle()
    val favoriteProfiles by viewModel.favoriteProfiles.collectAsStateWithLifecycle()
    val filterCriteria by viewModel.filterCriteria.collectAsStateWithLifecycle()
    val isSavingProfile by viewModel.isSavingProfile.collectAsStateWithLifecycle()

    val selectedProfile by viewModel.selectedProfile.collectAsStateWithLifecycle()
    val reportingProfile by viewModel.reportingProfile.collectAsStateWithLifecycle()
    val contactDialogProfile by viewModel.contactDialogProfile.collectAsStateWithLifecycle()
    val showSafetyModal by viewModel.showSafetyModal.collectAsStateWithLifecycle()
    val showDeleteConfirm by viewModel.showDeleteConfirm.collectAsStateWithLifecycle()
    val showBlockedUsersModal by viewModel.showBlockedUsersModal.collectAsStateWithLifecycle()

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.toastEvent.collectLatest { msg ->
            snackbarHostState.showSnackbar(msg)
        }
    }

    val canGoBack = currentScreen != Screen.Welcome
    BackHandler(enabled = canGoBack) {
        val didPop = viewModel.navigateBack()
        if (!didPop && currentScreen != Screen.Welcome) {
            viewModel.switchBottomTab(Screen.Matches)
        }
    }

    val isMainTabScreen = currentScreen in listOf(
        Screen.Matches,
        Screen.Search,
        Screen.Favorites,
        Screen.Premium,
        Screen.Settings
    )

    val layoutDirection = if (language == AppLanguage.ENGLISH) LayoutDirection.Ltr else LayoutDirection.Rtl

    CompositionLocalProvider(LocalLayoutDirection provides layoutDirection) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
                if (currentScreen != Screen.Welcome) {
                    AppTopBar(
                        currentScreen = currentScreen,
                        language = language,
                        onLanguageChange = { viewModel.setLanguage(it) },
                        canNavigateBack = !isMainTabScreen,
                        onBackClick = { viewModel.navigateBack() },
                        onSafetyPolicyClick = { viewModel.toggleSafetyModal(true) }
                    )
                }
            },
            bottomBar = {
                if (isMainTabScreen) {
                    AppBottomNav(
                        currentScreen = currentScreen,
                        favoriteCount = favorites.size,
                        language = language,
                        onTabSelected = { viewModel.switchBottomTab(it) }
                    )
                }
            },
            snackbarHost = { SnackbarHost(snackbarHostState) }
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                when (currentScreen) {
                    Screen.Welcome -> {
                        WelcomeScreen(
                            language = language,
                            onStartAuth = { viewModel.navigateTo(Screen.Auth) },
                            onBrowseAsGuest = { viewModel.switchBottomTab(Screen.Matches) }
                        )
                    }

                    Screen.Auth -> {
                        AuthScreen(
                            onLoginSuccess = { phone, isConsent ->
                                viewModel.login(phone, isConsent)
                            },
                            onBrowseAsGuest = { viewModel.switchBottomTab(Screen.Matches) }
                        )
                    }

                    Screen.ProfileCreationPage -> {
                        ProfileCreationPage(
                            isSaving = isSavingProfile,
                            onSaveProfileToFirebase = { name, age, gender, city, caste, edu, height, mar, photo, blur ->
                                viewModel.saveProfileToFirebase(name, age, gender, city, caste, edu, height, mar, photo, blur)
                            }
                        )
                    }

                    Screen.Matches -> {
                        MatchesFeedScreen(
                            profiles = matches,
                            favorites = favorites,
                            globalBlurEnabled = userAccount.photoBlurEnabled,
                            currentFilter = filterCriteria,
                            onFilterChange = { viewModel.updateFilters(it) },
                            onToggleFavorite = { viewModel.toggleFavorite(it) },
                            onViewDetails = { viewModel.openProfileDetails(it) },
                            onRequestContact = { viewModel.openContactDialog(it) },
                            onReport = { viewModel.openReportDialog(it) },
                            onOpenSearchTab = { viewModel.switchBottomTab(Screen.Search) }
                        )
                    }

                    Screen.Search -> {
                        SearchScreen(
                            currentFilter = filterCriteria,
                            matchingProfiles = matches,
                            favorites = favorites,
                            globalBlurEnabled = userAccount.photoBlurEnabled,
                            onFilterChange = { viewModel.updateFilters(it) },
                            onResetFilters = { viewModel.resetFilters() },
                            onToggleFavorite = { viewModel.toggleFavorite(it) },
                            onViewDetails = { viewModel.openProfileDetails(it) },
                            onRequestContact = { viewModel.openContactDialog(it) },
                            onReport = { viewModel.openReportDialog(it) }
                        )
                    }

                    Screen.Favorites -> {
                        FavoritesScreen(
                            favoriteProfiles = favoriteProfiles,
                            globalBlurEnabled = userAccount.photoBlurEnabled,
                            onToggleFavorite = { viewModel.toggleFavorite(it) },
                            onViewDetails = { viewModel.openProfileDetails(it) },
                            onRequestContact = { viewModel.openContactDialog(it) },
                            onReport = { viewModel.openReportDialog(it) },
                            onExploreMatches = { viewModel.switchBottomTab(Screen.Matches) }
                        )
                    }

                    Screen.Premium -> {
                        PremiumScreen(
                            userAccount = userAccount,
                            onUpgrade = { plan -> viewModel.upgradeToPremium(plan) }
                        )
                    }

                    Screen.Settings -> {
                        SettingsPrivacyScreen(
                            userAccount = userAccount,
                            blockedCount = blocked.size,
                            onTogglePhotoBlur = { viewModel.togglePhotoBlurSetting(it) },
                            onToggleHideProfile = { viewModel.toggleHideFromSearch(it) },
                            onOpenSafetyGuidelines = { viewModel.toggleSafetyModal(true) },
                            onManageBlockedUsers = { viewModel.toggleBlockedModal(true) },
                            onAddNewProfile = { viewModel.navigateTo(Screen.ProfileCreationPage) },
                            onOpenDeleteAccount = { viewModel.toggleDeleteModal(true) }
                        )
                    }
                }

                // Modals
                selectedProfile?.let { profile ->
                    ProfileDetailDialog(
                        profile = profile,
                        isFavorite = favorites.contains(profile.id),
                        globalBlurEnabled = userAccount.photoBlurEnabled,
                        onDismiss = { viewModel.closeProfileDetails() },
                        onToggleFavorite = { viewModel.toggleFavorite(profile.id) },
                        onRequestContact = { viewModel.openContactDialog(profile) },
                        onReport = { viewModel.openReportDialog(profile) }
                    )
                }

                reportingProfile?.let { profile ->
                    ReportBlockDialog(
                        profile = profile,
                        onDismiss = { viewModel.closeReportDialog() },
                        onConfirmBlock = { reason -> viewModel.blockUser(profile.id, reason) }
                    )
                }

                contactDialogProfile?.let { profile ->
                    ContactRequestDialog(
                        profile = profile,
                        onDismiss = { viewModel.closeContactDialog() },
                        onSendRequest = { note -> viewModel.sendContactRequest(profile, note) }
                    )
                }

                if (showSafetyModal) {
                    SafetyGuidelinesDialog(
                        onDismiss = { viewModel.toggleSafetyModal(false) }
                    )
                }

                if (showDeleteConfirm) {
                    DeleteAccountDialog(
                        onDismiss = { viewModel.toggleDeleteModal(false) },
                        onConfirmDelete = { viewModel.deleteAccount() }
                    )
                }

                if (showBlockedUsersModal) {
                    BlockedUsersDialog(
                        blockedIds = blocked,
                        onDismiss = { viewModel.toggleBlockedModal(false) },
                        onUnblock = { viewModel.unblockUser(it) }
                    )
                }
            }
        }
    }
}
