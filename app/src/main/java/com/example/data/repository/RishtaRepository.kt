package com.example.data.repository

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.example.R
import com.example.data.model.AppLanguage
import com.example.data.model.ContactRequest
import com.example.data.model.Profile
import com.example.data.model.UserAccount
import com.google.firebase.Firebase
import com.google.firebase.Timestamp
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.json.JSONArray
import org.json.JSONObject

class RishtaRepository(private val context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences("sindhi_rishta_prefs", Context.MODE_PRIVATE)

    // Explicitly initialize Firestore using the provisioned database ID
    private val firestore: FirebaseFirestore by lazy {
        val dbId = context.getString(R.string.firestore_database_id)
        FirebaseFirestore.getInstance(dbId)
    }

    private val auth by lazy { Firebase.auth }

    private val _profiles = MutableStateFlow<List<Profile>>(emptyList())
    val profiles: StateFlow<List<Profile>> = _profiles.asStateFlow()

    private val _favorites = MutableStateFlow<Set<String>>(emptySet())
    val favorites: StateFlow<Set<String>> = _favorites.asStateFlow()

    private val _blocked = MutableStateFlow<Set<String>>(emptySet())
    val blocked: StateFlow<Set<String>> = _blocked.asStateFlow()

    private val _contactRequests = MutableStateFlow<List<ContactRequest>>(emptyList())
    val contactRequests: StateFlow<List<ContactRequest>> = _contactRequests.asStateFlow()

    private val _userAccount = MutableStateFlow(UserAccount())
    val userAccount: StateFlow<UserAccount> = _userAccount.asStateFlow()

    private val _currentLanguage = MutableStateFlow(AppLanguage.SINDHI)
    val currentLanguage: StateFlow<AppLanguage> = _currentLanguage.asStateFlow()

    init {
        loadData()
        startFirestoreSync()
    }

    private fun loadData() {
        val langCode = prefs.getString("app_language", AppLanguage.SINDHI.code)
        _currentLanguage.value = AppLanguage.values().firstOrNull { it.code == langCode } ?: AppLanguage.SINDHI

        val favSet = prefs.getStringSet("favorites", emptySet()) ?: emptySet()
        _favorites.value = favSet

        val blockedSet = prefs.getStringSet("blocked", emptySet()) ?: emptySet()
        _blocked.value = blockedSet

        val isRegistered = prefs.getBoolean("user_registered", false)
        val userPhone = prefs.getString("user_phone", "") ?: ""
        val userName = prefs.getString("user_name", "معزز واهپيدار") ?: "معزز واهپيدار"
        val isPremium = prefs.getBoolean("user_is_premium", false)
        val premiumPlan = prefs.getString("user_premium_plan", "") ?: ""
        val blurEnabled = prefs.getBoolean("photo_blur_enabled", true)
        val hideSearch = prefs.getBoolean("hide_from_search", false)

        _userAccount.value = UserAccount(
            phone = userPhone,
            name = userName,
            isRegistered = isRegistered,
            isPremium = isPremium,
            premiumPlan = premiumPlan,
            photoBlurEnabled = blurEnabled,
            hideProfileFromSearch = hideSearch
        )

        // Load local profiles
        val savedProfilesJson = prefs.getString("custom_profiles", null)
        val list = mutableListOf<Profile>()

        if (!savedProfilesJson.isNullOrEmpty()) {
            try {
                val array = JSONArray(savedProfilesJson)
                for (i in 0 until array.length()) {
                    val obj = array.getJSONObject(i)
                    list.add(
                        Profile(
                            id = obj.getString("id"),
                            userId = obj.optString("userId", ""),
                            name = obj.getString("name"),
                            age = obj.getInt("age"),
                            gender = obj.getString("gender"),
                            city = obj.getString("city"),
                            caste = obj.optString("caste", "سنڌي"),
                            education = obj.getString("education"),
                            height = obj.optString("height", "5' 6\""),
                            marital = obj.getString("marital"),
                            bio = obj.optString("bio", ""),
                            verified = obj.optBoolean("verified", false),
                            photo = obj.getString("photo"),
                            photoBlurred = obj.optBoolean("photoBlurred", false),
                            isCustomUser = obj.optBoolean("isCustomUser", false),
                            createdAt = obj.optLong("createdAt", System.currentTimeMillis())
                        )
                    )
                }
            } catch (e: Exception) {
                list.clear()
            }
        }

        if (list.isEmpty()) {
            list.addAll(getInitialSeedProfiles())
            saveProfilesInternal(list)
        }

        _profiles.value = list
    }

    private fun startFirestoreSync() {
        try {
            firestore.collection("profiles")
                .addSnapshotListener { snapshot, error ->
                    if (error != null) {
                        Log.w("FirestoreSync", "Listen failed: ${error.message}")
                        return@addSnapshotListener
                    }
                    if (snapshot != null && !snapshot.isEmpty) {
                        val cloudProfiles = mutableListOf<Profile>()
                        for (doc in snapshot.documents) {
                            try {
                                val p = Profile(
                                    id = doc.id,
                                    userId = doc.getString("userId") ?: "",
                                    name = doc.getString("name") ?: "",
                                    age = (doc.getLong("age") ?: 18).toInt(),
                                    gender = doc.getString("gender") ?: "مرد",
                                    city = doc.getString("city") ?: "",
                                    caste = doc.getString("caste") ?: "",
                                    education = doc.getString("education") ?: "",
                                    height = doc.getString("height") ?: "5' 6\"",
                                    marital = doc.getString("marital") ?: "اڻ پرڻيل",
                                    bio = doc.getString("bio") ?: "",
                                    verified = doc.getBoolean("verified") ?: false,
                                    photo = doc.getString("photo") ?: "",
                                    photoBlurred = doc.getBoolean("photoBlurred") ?: false,
                                    isCustomUser = true
                                )
                                cloudProfiles.add(p)
                            } catch (e: Exception) {
                                Log.e("FirestoreSync", "Error parsing doc ${doc.id}", e)
                            }
                        }

                        // Merge cloud profiles with local profiles (cloud takes precedence for same ID)
                        val cloudIds = cloudProfiles.map { it.id }.toSet()
                        val nonDuplicateLocal = _profiles.value.filter { !cloudIds.contains(it.id) }
                        _profiles.value = cloudProfiles + nonDuplicateLocal
                    }
                }
        } catch (e: Exception) {
            Log.e("FirestoreSync", "Failed to start snapshot listener", e)
        }
    }

    private fun getInitialSeedProfiles(): List<Profile> {
        return listOf(
            Profile(
                id = "p1",
                userId = "seed_1",
                name = "راول شاهه بخاري",
                age = 28,
                gender = "مرد",
                city = "حيدرآباد",
                caste = "بخاري (سيد)",
                education = "انجنيئرنگ / آءِ ٽي (BS-SE)",
                height = "5' 10\"",
                marital = "اڻ پرڻيل",
                bio = "سيد گهراڻي سان واسطو. 5 وقت جو نمازي، شريف النفس ۽ پڙهيل لکيل خاندان. شادي صرف باوقار سنڌي خاندان مان گهربل آهي.",
                verified = false,
                photo = "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?auto=format&fit=crop&w=400&q=80",
                photoBlurred = false
            ),
            Profile(
                id = "p2",
                userId = "seed_2",
                name = "زيب النساء ٽالپر",
                age = 25,
                gender = "عورت",
                city = "ڪراچي",
                caste = "ٽالپر",
                education = "ماسٽرس (MA سنڌي ۽ تعليم)",
                height = "5' 4\"",
                marital = "اڻ پرڻيل",
                bio = "مذهبي، سليقه‌مند ۽ ادبي خاندان سان تعلق. گهر سنڀالڻ ۽ نيڪ اخلاق کي اوليت. عمر 26 کان 32 تائين سٺو اخلاق رکندڙ ناتو گهربل آهي.",
                verified = false,
                photo = "https://images.unsplash.com/photo-1573496359142-b8d87734a5a2?auto=format&fit=crop&w=400&q=80",
                photoBlurred = true
            ),
            Profile(
                id = "p3",
                userId = "seed_3",
                name = "علي گوهر سومرو",
                age = 31,
                gender = "مرد",
                city = "سکر",
                caste = "سومرو",
                education = "ماسٽرس (MBA فنانس)",
                height = "5' 9\"",
                marital = "اڻ پرڻيل",
                bio = "سکر جو رهواسي، پنهنجو ذاتي گهر ۽ باوقار نوڪري. ماءُ پيءُ سان گڏ رهندڙ. بااخلاق، پڙهيل لکيل سنڌي نياڻي گهربل آهي.",
                verified = false,
                photo = "https://images.unsplash.com/photo-1500648767791-00dcc994a43e?auto=format&fit=crop&w=400&q=80",
                photoBlurred = false
            ),
            Profile(
                id = "p4",
                userId = "seed_4",
                name = "ڊاڪٽر دعا ميمڻ",
                age = 26,
                gender = "عورت",
                city = "لاڙڪاڻو",
                caste = "ميمڻ",
                education = "ميڊيڪل (MBBS)",
                height = "5' 3\"",
                marital = "اڻ پرڻيل",
                bio = "سنجيده ۽ معزز فيملي. پاڻ گورنمينٽ اسپتال ۾ ڊاڪٽر آهي. ڪراچي، حيدرآباد يا لاڙڪاڻي مان ڊاڪٽر يا اعليٰ آفيسر رشتو گهربل آهي.",
                verified = false,
                photo = "https://images.unsplash.com/photo-1594824813583-b714b648ffef?auto=format&fit=crop&w=400&q=80",
                photoBlurred = true
            ),
            Profile(
                id = "p5",
                userId = "seed_5",
                name = "ذوالفقار علي خاصخيلي",
                age = 34,
                gender = "مرد",
                city = "ميرپورخاص",
                caste = "خاصخيلي",
                education = "گريجوئيشن (BSc ايگريڪلچر)",
                height = "5' 11\"",
                marital = "اڻ پرڻيل",
                bio = "ميرپورخاص ۾ پنهنجي زمينداري ۽ ڪاروبار. سادي طبيعت، سچائي ۽ گهرو خوشين کي پسند ڪندڙ. خاندان جي رضامندي سان رشتو ڳولجي ٿو.",
                verified = false,
                photo = "https://images.unsplash.com/photo-1506794778202-cad84cf45f1d?auto=format&fit=crop&w=400&q=80",
                photoBlurred = false
            ),
            Profile(
                id = "p6",
                userId = "seed_6",
                name = "مهوش ناريجو",
                age = 29,
                gender = "عورت",
                city = "بينظير آباد (نوابشاهه)",
                caste = "ناريجو",
                education = "ماسٽرس (MSc بوٽني)",
                height = "5' 5\"",
                marital = "طلاق يافته",
                bio = "ماضيءَ جي شرعي طلاق بعد نئين پروقار زندگيءَ جي اميد سان. ڪو ٻار ناهي. سچو، ايماندار ۽ سهارو ڏيڻ وارو نيڪ مرد گهربل آهي.",
                verified = false,
                photo = "https://images.unsplash.com/photo-1567532939604-b6b5b0db2604?auto=format&fit=crop&w=400&q=80",
                photoBlurred = true
            ),
            Profile(
                id = "p7",
                userId = "seed_7",
                name = "مير اسدالله ڪلهوڙو",
                age = 32,
                gender = "مرد",
                city = "خيرپور",
                caste = "ڪلهوڙو",
                education = "سول سروسز (CSS/PMS)",
                height = "5' 11\"",
                marital = "اڻ پرڻيل",
                bio = "سرڪاري ملازم، اعليٰ خانداني تعلق. ماءُ پيءُ جي عزت ۽ خانداني قدرن جي پابند تعليم يافته سنڌي گهرڀاتي جي خواهش.",
                verified = true,
                photo = "https://images.unsplash.com/photo-1492562080023-ab3db95bfbce?auto=format&fit=crop&w=400&q=80",
                photoBlurred = false
            ),
            Profile(
                id = "p8",
                userId = "seed_8",
                name = "ثانيا چانڊيو",
                age = 24,
                gender = "عورت",
                city = "دادو",
                caste = "چانڊيو",
                education = "بي ايڊ / تعليم (B.Ed)",
                height = "5' 2\"",
                marital = "اڻ پرڻيل",
                bio = "سادي سنڌي ثقافت ۽ مذهبي روايتن جي پابند. قرآن ۽ سنت جي پيروي ڪندڙ باوقار ۽ نيڪ عمل رکندڙ خاندان مان ناتو گهربل آهي.",
                verified = false,
                photo = "https://images.unsplash.com/photo-1544005313-94ddf0286df2?auto=format&fit=crop&w=400&q=80",
                photoBlurred = true
            )
        )
    }

    private fun saveProfilesInternal(list: List<Profile>) {
        val array = JSONArray()
        for (p in list) {
            val obj = JSONObject()
            obj.put("id", p.id)
            obj.put("userId", p.userId)
            obj.put("name", p.name)
            obj.put("age", p.age)
            obj.put("gender", p.gender)
            obj.put("city", p.city)
            obj.put("caste", p.caste)
            obj.put("education", p.education)
            obj.put("height", p.height)
            obj.put("marital", p.marital)
            obj.put("bio", p.bio)
            obj.put("verified", p.verified)
            obj.put("photo", p.photo)
            obj.put("photoBlurred", p.photoBlurred)
            obj.put("isCustomUser", p.isCustomUser)
            obj.put("createdAt", p.createdAt)
            array.put(obj)
        }
        prefs.edit().putString("custom_profiles", array.toString()).apply()
    }

    fun addProfile(newProfile: Profile, onComplete: (Boolean) -> Unit = {}) {
        val currentUserId = auth.currentUser?.uid ?: "user_${System.currentTimeMillis()}"
        val profileWithUser = newProfile.copy(userId = currentUserId)

        // 1. Update local reactive state & SharedPreferences instantly
        val updated = listOf(profileWithUser) + _profiles.value
        _profiles.value = updated
        saveProfilesInternal(updated)

        // 2. Persist to Firebase Firestore
        val profileMap = hashMapOf(
            "userId" to currentUserId,
            "name" to profileWithUser.name,
            "age" to profileWithUser.age,
            "gender" to profileWithUser.gender,
            "city" to profileWithUser.city,
            "caste" to profileWithUser.caste,
            "education" to profileWithUser.education,
            "height" to profileWithUser.height,
            "marital" to profileWithUser.marital,
            "bio" to profileWithUser.bio,
            "photo" to profileWithUser.photo,
            "photoBlurred" to profileWithUser.photoBlurred,
            "verified" to profileWithUser.verified,
            "createdAt" to Timestamp.now(),
            "updatedAt" to Timestamp.now()
        )

        try {
            firestore.collection("profiles").document(profileWithUser.id).set(profileMap)
                .addOnSuccessListener {
                    Log.d("Firestore", "Profile successfully saved to Firestore: ${profileWithUser.id}")
                    onComplete(true)
                }
                .addOnFailureListener { e ->
                    Log.e("Firestore", "Failed to save profile to Firestore", e)
                    // Even if network is offline or unauthenticated, local is saved
                    onComplete(true)
                }
        } catch (e: Exception) {
            Log.e("Firestore", "Error calling Firestore", e)
            onComplete(true)
        }
    }

    fun toggleFavorite(profileId: String) {
        val current = _favorites.value.toMutableSet()
        if (current.contains(profileId)) {
            current.remove(profileId)
        } else {
            current.add(profileId)
        }
        _favorites.value = current
        prefs.edit().putStringSet("favorites", current).apply()
    }

    fun blockProfile(profileId: String) {
        val current = _blocked.value.toMutableSet()
        current.add(profileId)
        _blocked.value = current
        prefs.edit().putStringSet("blocked", current).apply()

        if (_favorites.value.contains(profileId)) {
            val favs = _favorites.value.toMutableSet()
            favs.remove(profileId)
            _favorites.value = favs
            prefs.edit().putStringSet("favorites", favs).apply()
        }
    }

    fun unblockProfile(profileId: String) {
        val current = _blocked.value.toMutableSet()
        current.remove(profileId)
        _blocked.value = current
        prefs.edit().putStringSet("blocked", current).apply()
    }

    fun sendContactRequest(profileId: String, profileName: String, guardianNote: String) {
        val req = ContactRequest(
            id = "req_${System.currentTimeMillis()}",
            profileId = profileId,
            profileName = profileName,
            guardianNote = guardianNote
        )
        _contactRequests.value = listOf(req) + _contactRequests.value
    }

    fun updateUserAccount(account: UserAccount) {
        _userAccount.value = account
        prefs.edit()
            .putBoolean("user_registered", account.isRegistered)
            .putString("user_phone", account.phone)
            .putString("user_name", account.name)
            .putBoolean("user_is_premium", account.isPremium)
            .putString("user_premium_plan", account.premiumPlan)
            .putBoolean("photo_blur_enabled", account.photoBlurEnabled)
            .putBoolean("hide_from_search", account.hideProfileFromSearch)
            .apply()
    }

    fun setLanguage(language: AppLanguage) {
        _currentLanguage.value = language
        prefs.edit().putString("app_language", language.code).apply()
    }

    fun clearAllUserData() {
        prefs.edit()
            .remove("favorites")
            .remove("blocked")
            .remove("user_registered")
            .remove("user_phone")
            .remove("user_name")
            .remove("user_is_premium")
            .remove("user_premium_plan")
            .remove("custom_profiles")
            .apply()

        _favorites.value = emptySet()
        _blocked.value = emptySet()
        _contactRequests.value = emptyList()
        _userAccount.value = UserAccount()
        _profiles.value = getInitialSeedProfiles()
        saveProfilesInternal(_profiles.value)
    }
}
