package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.model.AppLanguage
import com.example.data.model.FilterCriteria
import com.example.data.model.Profile
import com.example.data.model.UserAccount
import com.example.data.repository.RishtaRepository
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

sealed class Screen(val titleSindhi: String, val titleUrdu: String, val titleEnglish: String) {
    object Welcome : Screen("ڀلي ڪري آيا", "خوش آمدید", "Welcome")
    object Auth : Screen("لاگ ان ۽ تصديق", "لاگ ان اور تصدیق", "Sign In & Auth")
    object ProfileCreationPage : Screen("پروفائل ٺاهيو", "پروفائل بنائیں", "Profile Creation Page")
    object Matches : Screen("رشتا ۽ تجويزون", "رشتے اور تجاویز", "Matches")
    object Search : Screen("ڳولا ۽ فلٽر", "تلاش اور فلٹرز", "Search & Filters")
    object Favorites : Screen("محفوظ رشتا", "پسندیدہ رشتے", "Favorites")
    object Premium : Screen("پريميئم پلان", "پریمیم پلان", "Premium")
    object Settings : Screen("منهنجو اڪائونٽ", "میرا اکاؤنٹ", "Settings & Safety")
}

class RishtaViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = RishtaRepository(application)

    private val _currentScreen = MutableStateFlow<Screen>(Screen.Welcome)
    val currentScreen: StateFlow<Screen> = _currentScreen.asStateFlow()

    private val _navigationStack = mutableListOf<Screen>(Screen.Welcome)

    val currentLanguage: StateFlow<AppLanguage> = repository.currentLanguage
    val userAccount: StateFlow<UserAccount> = repository.userAccount
    val favorites: StateFlow<Set<String>> = repository.favorites
    val blocked: StateFlow<Set<String>> = repository.blocked
    val contactRequests = repository.contactRequests

    private val _filterCriteria = MutableStateFlow(FilterCriteria())
    val filterCriteria: StateFlow<FilterCriteria> = _filterCriteria.asStateFlow()

    private val _selectedProfile = MutableStateFlow<Profile?>(null)
    val selectedProfile: StateFlow<Profile?> = _selectedProfile.asStateFlow()

    private val _reportingProfile = MutableStateFlow<Profile?>(null)
    val reportingProfile: StateFlow<Profile?> = _reportingProfile.asStateFlow()

    private val _contactDialogProfile = MutableStateFlow<Profile?>(null)
    val contactDialogProfile: StateFlow<Profile?> = _contactDialogProfile.asStateFlow()

    private val _showSafetyModal = MutableStateFlow(false)
    val showSafetyModal: StateFlow<Boolean> = _showSafetyModal.asStateFlow()

    private val _showDeleteConfirm = MutableStateFlow(false)
    val showDeleteConfirm: StateFlow<Boolean> = _showDeleteConfirm.asStateFlow()

    private val _showBlockedUsersModal = MutableStateFlow(false)
    val showBlockedUsersModal: StateFlow<Boolean> = _showBlockedUsersModal.asStateFlow()

    private val _isSavingProfile = MutableStateFlow(false)
    val isSavingProfile: StateFlow<Boolean> = _isSavingProfile.asStateFlow()

    private val _toastEvent = MutableSharedFlow<String>()
    val toastEvent: SharedFlow<String> = _toastEvent.asSharedFlow()

    // Filtered matches stream
    val matches: StateFlow<List<Profile>> = combine(
        repository.profiles,
        repository.blocked,
        _filterCriteria
    ) { profiles, blockedSet, filter ->
        profiles.filter { profile ->
            !blockedSet.contains(profile.id) &&
                    profile.age >= filter.minAge &&
                    (filter.city.isBlank() || profile.city.contains(filter.city, ignoreCase = true)) &&
                    (filter.caste.isBlank() || profile.caste.contains(filter.caste, ignoreCase = true)) &&
                    (filter.gender.isBlank() || profile.gender == filter.gender) &&
                    (filter.education.isBlank() || profile.education.contains(filter.education, ignoreCase = true)) &&
                    (filter.marital.isBlank() || profile.marital == filter.marital)
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Favorite profiles stream
    val favoriteProfiles: StateFlow<List<Profile>> = combine(
        repository.profiles,
        repository.favorites,
        repository.blocked
    ) { profiles, favs, blockedSet ->
        profiles.filter { favs.contains(it.id) && !blockedSet.contains(it.id) }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun navigateTo(screen: Screen) {
        if (_currentScreen.value != screen) {
            _navigationStack.add(screen)
            _currentScreen.value = screen
        }
    }

    fun navigateBack(): Boolean {
        if (_navigationStack.size > 1) {
            _navigationStack.removeAt(_navigationStack.size - 1)
            _currentScreen.value = _navigationStack.last()
            return true
        }
        return false
    }

    fun switchBottomTab(screen: Screen) {
        _navigationStack.clear()
        _navigationStack.add(screen)
        _currentScreen.value = screen
    }

    fun setLanguage(language: AppLanguage) {
        repository.setLanguage(language)
    }

    fun updateFilters(newFilter: FilterCriteria) {
        _filterCriteria.value = newFilter
    }

    fun resetFilters() {
        _filterCriteria.value = FilterCriteria()
    }

    fun openProfileDetails(profile: Profile) {
        _selectedProfile.value = profile
    }

    fun closeProfileDetails() {
        _selectedProfile.value = null
    }

    fun openReportDialog(profile: Profile) {
        _reportingProfile.value = profile
    }

    fun closeReportDialog() {
        _reportingProfile.value = null
    }

    fun openContactDialog(profile: Profile) {
        _contactDialogProfile.value = profile
    }

    fun closeContactDialog() {
        _contactDialogProfile.value = null
    }

    fun toggleSafetyModal(show: Boolean) {
        _showSafetyModal.value = show
    }

    fun toggleDeleteModal(show: Boolean) {
        _showDeleteConfirm.value = show
    }

    fun toggleBlockedModal(show: Boolean) {
        _showBlockedUsersModal.value = show
    }

    fun toggleFavorite(profileId: String) {
        repository.toggleFavorite(profileId)
        val isFav = repository.favorites.value.contains(profileId)
        viewModelScope.launch {
            _toastEvent.emit(if (isFav) "محفوظ رشتن ۾ شامل ٿي ويو" else "پسنديده مان ڪڍيو ويو")
        }
    }

    fun blockUser(profileId: String, reason: String = "") {
        repository.blockProfile(profileId)
        closeReportDialog()
        closeProfileDetails()
        viewModelScope.launch {
            _toastEvent.emit("واهپيدار کي بلاڪ ڪيو ويو ۽ شڪايت سسٽم ۾ داخل ڪئي وئي")
        }
    }

    fun unblockUser(profileId: String) {
        repository.unblockProfile(profileId)
        viewModelScope.launch {
            _toastEvent.emit("واهپيدار ان بلاڪ ڪيو ويو")
        }
    }

    fun sendContactRequest(profile: Profile, guardianNote: String) {
        repository.sendContactRequest(profile.id, profile.name, guardianNote)
        closeContactDialog()
        viewModelScope.launch {
            _toastEvent.emit("توهان جي رابطي جي باضابطه درخواست والدين ڏانهن موڪلي وئي آهي!")
        }
    }

    // Save Profile to Firebase & Local Storage
    fun saveProfileToFirebase(
        name: String,
        age: Int,
        gender: String,
        city: String,
        caste: String,
        education: String,
        height: String,
        marital: String,
        photoUrl: String,
        photoBlurred: Boolean
    ) {
        _isSavingProfile.value = true
        val currentUserId = Firebase.auth.currentUser?.uid ?: "user_${System.currentTimeMillis()}"

        val newProfile = Profile(
            id = "p_${System.currentTimeMillis()}",
            userId = currentUserId,
            name = name,
            age = age,
            gender = gender,
            city = city,
            caste = caste,
            education = education,
            height = height,
            marital = marital,
            bio = "خاندان: $caste • شهر: $city • قد: $height • تعليم: $education",
            photo = photoUrl.ifBlank {
                if (gender == "عورت") "https://images.unsplash.com/photo-1573496359142-b8d87734a5a2?auto=format&fit=crop&w=400&q=80"
                else "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?auto=format&fit=crop&w=400&q=80"
            },
            photoBlurred = photoBlurred,
            isCustomUser = true
        )

        repository.addProfile(newProfile) { success ->
            _isSavingProfile.value = false
            repository.updateUserAccount(
                userAccount.value.copy(
                    name = name,
                    isRegistered = true
                )
            )
            viewModelScope.launch {
                _toastEvent.emit("پروفائل ڪاميابي سان فائر بيس (Firebase) تي محفوظ ٿي ويو!")
            }
            navigateTo(Screen.Matches)
        }
    }

    fun login(phone: String, isAgeConsentGranted: Boolean): Boolean {
        if (!isAgeConsentGranted) {
            viewModelScope.launch {
                _toastEvent.emit("سنڌي رشتو رڳو 18 سال يا ان کان مٿي عمر وارن لاءِ آهي!")
            }
            return false
        }
        val currentAccount = userAccount.value
        repository.updateUserAccount(
            currentAccount.copy(
                phone = phone,
                isRegistered = true
            )
        )
        viewModelScope.launch {
            _toastEvent.emit("لاگ ان ڪامياب! مهرباني ڪري پنهنجو پروفائل ٺاهيو.")
        }
        // Direct transition after login to "Profile Creation Page"
        navigateTo(Screen.ProfileCreationPage)
        return true
    }

    fun upgradeToPremium(plan: String) {
        val currentAccount = userAccount.value
        repository.updateUserAccount(
            currentAccount.copy(
                isPremium = true,
                premiumPlan = plan
            )
        )
        viewModelScope.launch {
            _toastEvent.emit("مبارڪون! پريميئم رڪنيت چالو ڪئي وئي (JazzCash / EasyPaisa)")
        }
        navigateTo(Screen.Matches)
    }

    fun togglePhotoBlurSetting(enabled: Boolean) {
        val current = userAccount.value
        repository.updateUserAccount(current.copy(photoBlurEnabled = enabled))
        viewModelScope.launch {
            _toastEvent.emit(if (enabled) "تصويرن جو رازداري بلر چالو ڪيو ويو" else "تصويرن جو بلر هٽايو ويو")
        }
    }

    fun toggleHideFromSearch(hidden: Boolean) {
        val current = userAccount.value
        repository.updateUserAccount(current.copy(hideProfileFromSearch = hidden))
        viewModelScope.launch {
            _toastEvent.emit(if (hidden) "توهان جو پروفائل ڳولا مان لڪايو ويو آهي" else "پروفائل ٻيهر ڳولا ۾ پڌرو آهي")
        }
    }

    fun deleteAccount() {
        repository.clearAllUserData()
        toggleDeleteModal(false)
        viewModelScope.launch {
            _toastEvent.emit("توهان جو اڪائونٽ ۽ ذاتي ڊيٽا ڪاميابي سان مٽايو ويو آهي")
        }
        _navigationStack.clear()
        _navigationStack.add(Screen.Welcome)
        _currentScreen.value = Screen.Welcome
    }
}
