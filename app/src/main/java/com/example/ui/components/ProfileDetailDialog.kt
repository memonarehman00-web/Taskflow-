package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Mail
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coil.compose.AsyncImage
import com.example.data.model.Profile
import com.example.ui.theme.BorderColor
import com.example.ui.theme.DangerRed
import com.example.ui.theme.GoldAccent
import com.example.ui.theme.GoldLight
import com.example.ui.theme.PrimaryDark
import com.example.ui.theme.PrimaryGreen
import com.example.ui.theme.SuccessGreen

@Composable
fun ProfileDetailDialog(
    profile: Profile,
    isFavorite: Boolean,
    globalBlurEnabled: Boolean,
    onDismiss: () -> Unit,
    onToggleFavorite: () -> Unit,
    onRequestContact: () -> Unit,
    onReport: () -> Unit
) {
    var revealBlurredPhoto by remember { mutableStateOf(false) }
    val isPhotoBlurred = profile.photoBlurred && globalBlurEnabled && !revealBlurredPhoto

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.92f)
                .clip(RoundedCornerShape(24.dp))
                .border(1.5.dp, GoldAccent, RoundedCornerShape(24.dp))
                .testTag("profile_detail_dialog"),
            color = Color.White
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                // Header row with Close & Favorite
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFF1F5F9))
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "بند ڪريو",
                            tint = Color(0xFF64748B),
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    Row {
                        IconButton(
                            onClick = onToggleFavorite,
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFF1F5F9))
                        ) {
                            Icon(
                                imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                                contentDescription = "پسنديده",
                                tint = if (isFavorite) DangerRed else Color(0xFF64748B),
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Avatar Photo
                Box(
                    modifier = Modifier
                        .size(120.dp)
                        .align(Alignment.CenterHorizontally)
                        .clip(CircleShape)
                        .border(3.dp, GoldAccent, CircleShape)
                        .background(Color(0xFFF1F5F9))
                ) {
                    AsyncImage(
                        model = profile.photo,
                        contentDescription = profile.name,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(120.dp)
                            .then(
                                if (isPhotoBlurred) Modifier.blur(18.dp)
                                else Modifier
                            )
                    )

                    if (profile.photoBlurred && globalBlurEnabled) {
                        Box(
                            modifier = Modifier
                                .align(Alignment.BottomCenter)
                                .fillMaxWidth()
                                .background(Color(0xDD073822))
                                .clickable { revealBlurredPhoto = !revealBlurredPhoto }
                                .padding(vertical = 4.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = if (revealBlurredPhoto) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                    contentDescription = null,
                                    tint = GoldLight,
                                    modifier = Modifier.size(12.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = if (revealBlurredPhoto) "بلر ڪريو" else "ڏسو",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = Color.White,
                                        fontSize = 10.sp
                                    )
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = profile.name,
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = PrimaryDark
                    ),
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )

                Text(
                    text = "${profile.age} سال • ${profile.city}",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = Color(0xFF64748B),
                        fontWeight = FontWeight.Medium
                    ),
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Metadata Card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF8FAFC)),
                    border = CardDefaults.outlinedCardBorder()
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        DetailRow(label = "ازدواجي حيثيت:", value = profile.marital)
                        if (profile.caste.isNotBlank()) {
                            DetailRow(label = "ذات:", value = profile.caste)
                        }
                        if (profile.height.isNotBlank()) {
                            DetailRow(label = "قد:", value = profile.height)
                        }
                        DetailRow(label = "تعليم:", value = profile.education)
                        DetailRow(label = "ملازمت / پيشو:", value = profile.profession)
                        DetailRow(
                            label = "حفاظتي حيثيت:",
                            value = if (profile.verified) "سرڪاري سڃاڻپ ذريعي تصديق مڪمل" else "غير تصديق ٿيل (بنيادي رڪارڊ)",
                            valueColor = if (profile.verified) SuccessGreen else Color(0xFFB45309)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Bio & Family Background
                Text(
                    text = "خانداني پس منظر ۽ گهرڀاتين جي خواهش:",
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = PrimaryDark
                    )
                )

                Spacer(modifier = Modifier.height(6.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color.White)
                        .border(1.dp, BorderColor, RoundedCornerShape(12.dp))
                        .padding(12.dp)
                ) {
                    Text(
                        text = profile.bio,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = Color(0xFF334155),
                            lineHeight = 22.sp
                        )
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Safety Rule Banner
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFFFEF3C7))
                        .border(1.dp, Color(0xFFFDE68A), RoundedCornerShape(12.dp))
                        .padding(12.dp)
                ) {
                    Row(verticalAlignment = Alignment.Top) {
                        Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = null,
                            tint = Color(0xFFB45309),
                            modifier = Modifier
                                .size(18.dp)
                                .padding(top = 2.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "حفاظتي ضابطو: نمبر سڌو سنئون لڪيل رکيل آهي. شاديءَ جي رابطي لاءِ والدين / سرپرست جو فون نمبر ٻنهي پاسن جي باضابطه رضامندي بعد ڏنو ويندو آهي.",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = Color(0xFF92400E),
                                fontSize = 11.5.sp,
                                lineHeight = 17.sp
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                // Action Buttons
                Button(
                    onClick = onRequestContact,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("detail_request_contact_button"),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = PrimaryGreen,
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Mail,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "رابطي جي باضابطه درخواست",
                        style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedButton(
                    onClick = onReport,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(44.dp)
                        .testTag("detail_report_button"),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = DangerRed)
                ) {
                    Icon(
                        imageVector = Icons.Default.Shield,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = DangerRed
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "شڪايت / واهپيدار بلاڪ ڪريو",
                        style = MaterialTheme.typography.labelMedium.copy(color = DangerRed)
                    )
                }
            }
        }
    }
}

@Composable
fun DetailRow(label: String, value: String, valueColor: Color = Color(0xFF1E293B)) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium.copy(
                fontWeight = FontWeight.Bold,
                color = Color(0xFF64748B)
            )
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium.copy(
                fontWeight = FontWeight.SemiBold,
                color = valueColor
            )
        )
    }
}
