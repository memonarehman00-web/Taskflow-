package com.example.ui.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material.icons.filled.CloudDone
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.Upload
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage

private val ThemeRed = Color(0xFFDC2626)       // Primary Red
private val ThemeDarkRed = Color(0xFF991B1B)   // Deep Red
private val ThemeRedBg = Color(0xFFFEF2F2)     // Soft Red/Pink Tint
private val ThemeRedBorder = Color(0xFFFCA5A5) // Light Red Border
private val SurfaceWhite = Color(0xFFFFFFFF)

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ProfileCreationPage(
    isSaving: Boolean = false,
    onSaveProfileToFirebase: (
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
    ) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var ageStr by remember { mutableStateOf("26") }
    var gender by remember { mutableStateOf("مرد") }
    var city by remember { mutableStateOf("حيدرآباد") }
    var caste by remember { mutableStateOf("سومرو") }
    var education by remember { mutableStateOf("انجنيئرنگ (BS)") }
    var height by remember { mutableStateOf("5 فوٽ 8 انچ") }
    var marital by remember { mutableStateOf("اڻ پرڻيل") }
    var photoUrl by remember {
        mutableStateOf("https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?auto=format&fit=crop&w=400&q=80")
    }
    var photoBlurred by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    // Zero-permission Android Photo Picker for user photo upload
    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            photoUrl = uri.toString()
        }
    }

    val commonCastes = listOf("سومرو", "ميمڻ", "ٽالپر", "بخاري", "چانڊيو", "ڪلهوڙو", "خاصخيلي", "ناريجو", "عباسي", "مهر", "جمالي", "شاهه")
    val commonCities = listOf("حيدرآباد", "ڪراچي", "سکر", "لاڙڪاڻو", "ميرپورخاص", "بينظير آباد", "خيرپور", "دادو", "شڪارپور", "ٺٽو", "بدين")
    val commonHeights = listOf("5 فوٽ 2 انچ", "5 فوٽ 4 انچ", "5 فوٽ 6 انچ", "5 فوٽ 8 انچ", "5 فوٽ 10 انچ", "6 فوٽ")
    val maritalStatuses = listOf("اڻ پرڻيل", "طلاق يافته", "بيواهه")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SurfaceWhite)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Red Header Banner
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(ThemeDarkRed, ThemeRed)
                    )
                )
                .padding(top = 20.dp, bottom = 22.dp, start = 16.dp, end = 16.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Title on top: "پروفائل ٺاهيو"
                Text(
                    text = "پروفائل ٺاهيو",
                    style = MaterialTheme.typography.displayMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        fontSize = 26.sp
                    ),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "سنڌي رشتو پاڪستان • پنهنجي رشتي بابت سچي ۽ مڪمل معلومات داخل ڪريو",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = Color(0xFFFFE4E6),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    ),
                    textAlign = TextAlign.Center
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Red and White Form Container
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
            border = CardDefaults.outlinedCardBorder().copy(
                brush = Brush.verticalGradient(listOf(ThemeRedBorder, Color(0xFFE2E8F0)))
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {

                // Circular profile photo upload with text "فوٽو شامل ڪريو" and button
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(116.dp)
                            .clip(CircleShape)
                            .border(3.5.dp, ThemeRed, CircleShape)
                            .background(ThemeRedBg)
                            .clickable {
                                photoPickerLauncher.launch(
                                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                )
                            }
                            .testTag("circular_photo_upload"),
                        contentAlignment = Alignment.Center
                    ) {
                        AsyncImage(
                            model = photoUrl,
                            contentDescription = "فوٽو شامل ڪريو",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )

                        // Camera icon overlay
                        Box(
                            modifier = Modifier
                                .align(Alignment.BottomCenter)
                                .fillMaxWidth()
                                .background(Color(0xCC000000))
                                .padding(vertical = 3.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.PhotoCamera,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(15.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Text: "فوٽو شامل ڪريو"
                    Text(
                        text = "فوٽو شامل ڪريو",
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = ThemeRed,
                            fontSize = 15.sp
                        ),
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Dedicated button to upload profile photo (Requirement 4)
                    Button(
                        onClick = {
                            photoPickerLauncher.launch(
                                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                            )
                        },
                        modifier = Modifier
                            .height(42.dp)
                            .testTag("upload_profile_photo_button"),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = ThemeRed,
                            contentColor = Color.White
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Default.Upload,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(17.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "فوٽو اپلوڊ ڪريو",
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Avatar presets selection
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        SindhiPresetPill("مردانو اوتار 1", "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?auto=format&fit=crop&w=400&q=80", photoUrl) {
                            photoUrl = it
                            gender = "مرد"
                        }
                        SindhiPresetPill("مردانو اوتار 2", "https://images.unsplash.com/photo-1500648767791-00dcc994a43e?auto=format&fit=crop&w=400&q=80", photoUrl) {
                            photoUrl = it
                            gender = "مرد"
                        }
                        SindhiPresetPill("نياڻي اوتار 1", "https://images.unsplash.com/photo-1573496359142-b8d87734a5a2?auto=format&fit=crop&w=400&q=80", photoUrl) {
                            photoUrl = it
                            gender = "عورت"
                            photoBlurred = true
                        }
                        SindhiPresetPill("نياڻي اوتار 2", "https://images.unsplash.com/photo-1594824813583-b714b648ffef?auto=format&fit=crop&w=400&q=80", photoUrl) {
                            photoUrl = it
                            gender = "عورت"
                            photoBlurred = true
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Privacy Blur Checkbox
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(ThemeRedBg)
                            .clickable { photoBlurred = !photoBlurred }
                            .padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = photoBlurred,
                            onCheckedChange = { photoBlurred = it },
                            modifier = Modifier.testTag("profile_photo_blur_toggle"),
                            colors = CheckboxDefaults.colors(
                                checkedColor = ThemeRed,
                                checkmarkColor = Color.White
                            )
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = null,
                            tint = ThemeRed,
                            modifier = Modifier.size(15.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "تصوير کي رازداري بلر (ڌنڌلو) رکو",
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = ThemeDarkRed,
                                fontSize = 12.sp
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                // 1. نالو (Name)
                SindhiFieldLabel(label = "نالو:")
                OutlinedTextField(
                    value = name,
                    onValueChange = {
                        name = it
                        errorMessage = null
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("input_profile_name"),
                    shape = RoundedCornerShape(12.dp),
                    placeholder = { Text("پنهنجو پورو نالو هتي لکو (مثال: راول شاهه بخاري)") },
                    colors = redTextFieldColors(),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(14.dp))

                // 2. عمر (Age) & 3. جنس (مرد، عورت)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        SindhiFieldLabel(label = "عمر:")
                        OutlinedTextField(
                            value = ageStr,
                            onValueChange = { ageStr = it },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("input_profile_age"),
                            shape = RoundedCornerShape(12.dp),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            placeholder = { Text("عمر سالن ۾ (گهٽ ۾ گهٽ 18)") },
                            colors = redTextFieldColors(),
                            singleLine = true
                        )
                    }

                    Column(modifier = Modifier.weight(1.2f)) {
                        SindhiFieldLabel(label = "جنس (مرد، عورت):")
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            SindhiSelectablePill(
                                label = "مرد",
                                isSelected = gender == "مرد",
                                modifier = Modifier.weight(1f),
                                onClick = {
                                    gender = "مرد"
                                    photoUrl = "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?auto=format&fit=crop&w=400&q=80"
                                }
                            )
                            SindhiSelectablePill(
                                label = "عورت",
                                isSelected = gender == "عورت",
                                modifier = Modifier.weight(1f),
                                onClick = {
                                    gender = "عورت"
                                    photoUrl = "https://images.unsplash.com/photo-1573496359142-b8d87734a5a2?auto=format&fit=crop&w=400&q=80"
                                    photoBlurred = true
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // 4. شهر (City)
                SindhiFieldLabel(label = "شهر:")
                OutlinedTextField(
                    value = city,
                    onValueChange = { city = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("input_profile_city"),
                    shape = RoundedCornerShape(12.dp),
                    placeholder = { Text("پنهنجو شهر لکو يا هيٺان چونڊيو") },
                    colors = redTextFieldColors(),
                    singleLine = true
                )

                // Quick City Chips in Sindhi
                FlowRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 6.dp),
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    commonCities.take(6).forEach { c ->
                        SindhiChipPill(c, city == c) { city = c }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // 5. ذات (Caste)
                SindhiFieldLabel(label = "ذات:")
                OutlinedTextField(
                    value = caste,
                    onValueChange = { caste = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("input_profile_caste"),
                    shape = RoundedCornerShape(12.dp),
                    placeholder = { Text("پنهنجي ذات يا قبيلو لکو (مثال: سومرو، ميمڻ، ٽالپر، بخاري...)") },
                    colors = redTextFieldColors(),
                    singleLine = true
                )

                // Quick Caste Chips in Sindhi
                FlowRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 6.dp),
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    commonCastes.take(8).forEach { k ->
                        SindhiChipPill(k, caste == k) { caste = k }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // 6. تعليم (Education)
                SindhiFieldLabel(label = "تعليم:")
                OutlinedTextField(
                    value = education,
                    onValueChange = { education = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("input_profile_education"),
                    shape = RoundedCornerShape(12.dp),
                    placeholder = { Text("مثال: ميڊيڪل (MBBS)، انجنيئرنگ (BS)، ماسٽرس، گريجوئيشن...") },
                    colors = redTextFieldColors(),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(14.dp))

                // 7. قد (Height)
                SindhiFieldLabel(label = "قد:")
                OutlinedTextField(
                    value = height,
                    onValueChange = { height = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("input_profile_height"),
                    shape = RoundedCornerShape(12.dp),
                    placeholder = { Text("قد فوٽن ۽ انچن ۾ (مثال: 5 فوٽ 8 انچ)") },
                    colors = redTextFieldColors(),
                    singleLine = true
                )

                // Quick Height Chips in Sindhi
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 6.dp),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    commonHeights.forEach { h ->
                        SindhiChipPill(h, height == h, modifier = Modifier.weight(1f)) { height = h }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // 8. ازدواجي حيثيت (Marital status)
                SindhiFieldLabel(label = "ازدواجي حيثيت:")
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    maritalStatuses.forEach { m ->
                        SindhiSelectablePill(
                            label = m,
                            isSelected = marital == m,
                            modifier = Modifier.weight(1f),
                            onClick = { marital = m }
                        )
                    }
                }

                if (errorMessage != null) {
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = errorMessage ?: "",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = ThemeRed,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Red button at bottom: "پروفائل محفوظ ڪريو"
                Button(
                    onClick = {
                        val age = ageStr.toIntOrNull() ?: 0
                        if (name.isBlank()) {
                            errorMessage = "مهرباني ڪري پنهنجو نالو داخل ڪريو"
                            return@Button
                        }
                        if (age < 18) {
                            errorMessage = "حفاظتي ضابطو: عمر گهٽ ۾ گهٽ 18 سال هجڻ لازمي آهي!"
                            return@Button
                        }
                        if (city.isBlank()) {
                            errorMessage = "مهرباني ڪري پنهنجو شهر داخل ڪريو"
                            return@Button
                        }
                        if (caste.isBlank()) {
                            errorMessage = "مهرباني ڪري پنهنجي ذات داخل ڪريو"
                            return@Button
                        }
                        onSaveProfileToFirebase(
                            name,
                            age,
                            gender,
                            city,
                            caste,
                            education,
                            height,
                            marital,
                            photoUrl,
                            photoBlurred
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp)
                        .testTag("save_profile_firebase_button"),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = ThemeRed,
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(14.dp),
                    enabled = !isSaving
                ) {
                    if (isSaving) {
                        CircularProgressIndicator(
                            color = Color.White,
                            modifier = Modifier.size(24.dp),
                            strokeWidth = 2.dp
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "فائر بيس تي محفوظ ٿي رهيو آهي...",
                            style = MaterialTheme.typography.labelLarge.copy(
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Default.CloudDone,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "پروفائل محفوظ ڪريو",
                            style = MaterialTheme.typography.labelLarge.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                color = Color.White
                            )
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(30.dp))
    }
}

@Composable
private fun SindhiFieldLabel(label: String) {
    Text(
        text = label,
        style = MaterialTheme.typography.labelMedium.copy(
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1E293B),
            fontSize = 13.5.sp
        ),
        modifier = Modifier.padding(bottom = 4.dp)
    )
}

@Composable
private fun redTextFieldColors() = OutlinedTextFieldDefaults.colors(
    focusedBorderColor = ThemeRed,
    unfocusedBorderColor = Color(0xFFE2E8F0),
    focusedLabelColor = ThemeRed,
    cursorColor = ThemeRed
)

@Composable
private fun SindhiSelectablePill(
    label: String,
    isSelected: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .background(if (isSelected) ThemeRed else ThemeRedBg)
            .border(1.dp, if (isSelected) ThemeDarkRed else ThemeRedBorder, RoundedCornerShape(10.dp))
            .clickable(onClick = onClick)
            .padding(vertical = 10.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium.copy(
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.SemiBold,
                color = if (isSelected) Color.White else ThemeDarkRed,
                fontSize = 12.5.sp
            ),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun SindhiChipPill(
    label: String,
    isSelected: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(if (isSelected) ThemeRed else Color(0xFFF8FAFC))
            .border(1.dp, if (isSelected) ThemeRed else Color(0xFFE2E8F0), RoundedCornerShape(8.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 8.dp, vertical = 4.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall.copy(
                color = if (isSelected) Color.White else Color(0xFF475569),
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                fontSize = 11.sp
            ),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun SindhiPresetPill(
    label: String,
    url: String,
    currentUrl: String,
    onSelect: (String) -> Unit
) {
    val isSelected = url == currentUrl
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(10.dp))
            .background(if (isSelected) ThemeRedBg else Color(0xFFF1F5F9))
            .border(1.dp, if (isSelected) ThemeRed else Color.Transparent, RoundedCornerShape(10.dp))
            .clickable { onSelect(url) }
            .padding(horizontal = 6.dp, vertical = 3.dp)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall.copy(
                color = if (isSelected) ThemeRed else Color(0xFF64748B),
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                fontSize = 10.sp
            )
        )
    }
}
