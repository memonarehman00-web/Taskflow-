package com.example.ui.components

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.HourglassBottom
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.filled.Work
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.PhoneInTalk
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.data.model.Profile
import com.example.ui.theme.BorderColor
import com.example.ui.theme.DangerRed
import com.example.ui.theme.GoldAccent
import com.example.ui.theme.GoldLight
import com.example.ui.theme.PrimaryDark
import com.example.ui.theme.PrimaryGreen
import com.example.ui.theme.SuccessGreen

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ProfileCard(
    profile: Profile,
    isFavorite: Boolean,
    globalBlurEnabled: Boolean,
    onToggleFavorite: () -> Unit,
    onViewDetails: () -> Unit,
    onRequestContact: () -> Unit,
    onReport: () -> Unit,
    modifier: Modifier = Modifier
) {
    var revealBlurredPhoto by remember { mutableStateOf(false) }
    val isPhotoBlurred = profile.photoBlurred && globalBlurEnabled && !revealBlurredPhoto

    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("profile_card_${profile.id}")
            .border(1.dp, BorderColor, RoundedCornerShape(18.dp)),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // Image Header Container
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .background(Color(0xFFE2E8F0))
            ) {
                // Profile Photo
                AsyncImage(
                    model = profile.photo,
                    contentDescription = profile.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                        .then(
                            if (isPhotoBlurred) Modifier.blur(18.dp)
                            else Modifier
                        )
                )

                // Dark gradient overlay on bottom for readability
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(Color.Transparent, Color(0x66000000)),
                                startY = 100f
                            )
                        )
                )

                // Blur Privacy Pill / Toggle if blurred
                if (profile.photoBlurred && globalBlurEnabled) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .clip(RoundedCornerShape(20.dp))
                            .background(Color(0xCC073822))
                            .border(1.dp, GoldAccent, RoundedCornerShape(20.dp))
                            .clickable { revealBlurredPhoto = !revealBlurredPhoto }
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                imageVector = if (revealBlurredPhoto) Icons.Default.VisibilityOff else Icons.Default.Lock,
                                contentDescription = "رازداري",
                                tint = GoldLight,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = if (revealBlurredPhoto) "بلر ٻيهر لاڳو ڪريو" else "رازداري بلر (ڏسڻ لاءِ ٽيپ ڪريو)",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = Color.White,
                                    fontSize = 11.sp
                                )
                            )
                        }
                    }
                }

                // Top Left: Verification Status Tag
                Box(
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(10.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(
                            if (profile.verified) Color(0xDD16A34A) else Color(0xDD92400E)
                        )
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = if (profile.verified) Icons.Default.CheckCircle else Icons.Default.HourglassBottom,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(13.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = if (profile.verified) "مستند تصديق ٿيل" else "تصديق زيرِ عمل",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = Color.White,
                                fontSize = 10.5.sp,
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }
                }

                // Top Right: Favorite Button
                IconButton(
                    onClick = onToggleFavorite,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(8.dp)
                        .size(38.dp)
                        .clip(CircleShape)
                        .background(Color(0xBBFFFFFF))
                        .testTag("favorite_button_${profile.id}")
                ) {
                    Icon(
                        imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                        contentDescription = "پسنديده",
                        tint = if (isFavorite) DangerRed else Color(0xFF475569),
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            // Body Content
            Column(modifier = Modifier.padding(14.dp)) {
                // Name & Age Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = profile.name,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = PrimaryDark,
                            fontSize = 17.sp
                        ),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f)
                    )
                    Text(
                        text = "${profile.age} سال",
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = GoldAccent,
                            fontSize = 15.sp
                        )
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Detail Chips
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    ProfileChip(icon = Icons.Default.LocationOn, text = profile.city)
                    if (profile.caste.isNotBlank()) {
                        ProfileChip(icon = Icons.Default.Shield, text = "ذات: ${profile.caste}")
                    }
                    if (profile.height.isNotBlank()) {
                        ProfileChip(icon = Icons.Default.CheckCircle, text = "قد: ${profile.height}")
                    }
                    ProfileChip(icon = Icons.Default.School, text = profile.education)
                    ProfileChip(icon = Icons.Default.Work, text = profile.profession)
                    ProfileChip(icon = Icons.Default.Favorite, text = profile.marital)
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Bio preview
                Text(
                    text = profile.bio,
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = Color(0xFF475569),
                        lineHeight = 18.sp
                    ),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Action Buttons Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Full Details button
                    Button(
                        onClick = onViewDetails,
                        modifier = Modifier
                            .weight(1f)
                            .testTag("details_button_${profile.id}"),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = PrimaryGreen,
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text(
                            text = "مڪمل احوال",
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                        )
                    }

                    // Request Contact button
                    OutlinedButton(
                        onClick = onRequestContact,
                        modifier = Modifier
                            .weight(1f)
                            .testTag("contact_button_${profile.id}"),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = PrimaryGreen
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.PhoneInTalk,
                            contentDescription = null,
                            modifier = Modifier.size(15.dp),
                            tint = PrimaryGreen
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "رابطو",
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold)
                        )
                    }

                    // Report/Block Button
                    IconButton(
                        onClick = onReport,
                        modifier = Modifier
                            .size(40.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(Color(0xFFF1F5F9))
                            .testTag("report_button_${profile.id}")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Shield,
                            contentDescription = "رپورٽ يا بلاڪ",
                            tint = Color(0xFF64748B),
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ProfileChip(icon: ImageVector, text: String) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(Color(0xFFF1F5F9))
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = PrimaryGreen,
                modifier = Modifier.size(12.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = text,
                style = MaterialTheme.typography.labelSmall.copy(
                    fontSize = 11.sp,
                    color = Color(0xFF334155)
                ),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}
