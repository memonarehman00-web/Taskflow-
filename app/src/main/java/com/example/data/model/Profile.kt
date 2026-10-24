package com.example.data.model

import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.IgnoreExtraProperties

@IgnoreExtraProperties
data class Profile(
    @DocumentId val id: String = "",
    val userId: String = "",
    val name: String = "",
    val age: Int = 18,
    val gender: String = "مرد", // "مرد" or "عورت"
    val city: String = "",
    val caste: String = "", // ذات
    val education: String = "",
    val profession: String = "", // پيشو / ملازمت
    val height: String = "5' 6\"", // قد
    val marital: String = "اڻ پرڻيل", // "اڻ پرڻيل", "طلاق يافته", "بيواهه"
    val bio: String = "",
    val verified: Boolean = false,
    val photo: String = "",
    val photoBlurred: Boolean = false,
    val isCustomUser: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)

data class FilterCriteria(
    val minAge: Int = 18,
    val maxAge: Int = 60,
    val city: String = "",
    val caste: String = "",
    val gender: String = "",
    val education: String = "",
    val marital: String = ""
)

enum class AppLanguage(val label: String, val code: String) {
    SINDHI("سنڌي", "sd"),
    URDU("اردو", "ur"),
    ENGLISH("English", "en")
}

data class ContactRequest(
    val id: String = "",
    val profileId: String = "",
    val profileName: String = "",
    val guardianNote: String = "",
    val timestamp: Long = System.currentTimeMillis(),
    val status: String = "زيرِ غور" // Pending
)

data class UserAccount(
    val phone: String = "",
    val name: String = "معزز واهپيدار",
    val isRegistered: Boolean = false,
    val isPremium: Boolean = false,
    val premiumPlan: String = "",
    val photoBlurEnabled: Boolean = true,
    val hideProfileFromSearch: Boolean = false
)
