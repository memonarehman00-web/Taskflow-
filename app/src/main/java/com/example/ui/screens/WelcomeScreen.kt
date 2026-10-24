package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.AppLanguage
import com.example.ui.theme.AjrakRed
import com.example.ui.theme.BorderColor
import com.example.ui.theme.GoldAccent
import com.example.ui.theme.GoldLight
import com.example.ui.theme.PrimaryDark
import com.example.ui.theme.PrimaryGreen

@Composable
fun WelcomeScreen(
    language: AppLanguage,
    onStartAuth: () -> Unit,
    onBrowseAsGuest: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8FAF7))
            .verticalScroll(rememberScrollState())
    ) {
        // Hero Banner Art
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(230.dp)
                .background(PrimaryDark)
        ) {
            Image(
                painter = painterResource(id = R.drawable.sindhi_welcome_banner_1792584318132),
                contentDescription = "سنڌي رشتو ثقافتي بينر",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(Color.Transparent, Color(0xCC073822)),
                            startY = 120f
                        )
                    )
            )

            // Emblem Badge
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .size(68.dp)
                    .clip(CircleShape)
                    .background(Color.White)
                    .border(2.5.dp, GoldAccent, CircleShape)
                    .padding(4.dp),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_app_icon_1792584304814),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize().clip(CircleShape)
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Main Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            shape = RoundedCornerShape(22.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            border = CardDefaults.outlinedCardBorder()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "ڀلي ڪري آيا!",
                    style = MaterialTheme.typography.displayMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = PrimaryDark,
                        fontSize = 26.sp
                    ),
                    textAlign = TextAlign.Center
                )

                Text(
                    text = "سنڌي رشتو پاڪستان",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = GoldAccent
                    ),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "ملڪ اندر رکندڙ سڀني باوقار سنڌي خاندانن ۽ ماڻهن لاءِ بااعتماد، معزز ۽ سنجيده رشتن جو پليٽ فارم.",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = Color(0xFF475569),
                        textAlign = TextAlign.Center,
                        lineHeight = 22.sp
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Strict Legal 18+ Rule Badge
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFFFEF3C7))
                        .border(1.dp, Color(0xFFFDE68A), RoundedCornerShape(12.dp))
                        .padding(12.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Shield,
                            contentDescription = null,
                            tint = Color(0xFFB45309),
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "سخت قانوني پابندي: هي ايپ صرف 18 سال يا ان کان مٿي عمر وارن بالغ مردن ۽ عورتن جي شرعي ۽ باوقار نڪاح لاءِ آهي.",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = Color(0xFF92400E),
                                fontSize = 11.5.sp,
                                fontWeight = FontWeight.Medium,
                                lineHeight = 16.sp
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Primary Action Button
                Button(
                    onClick = onStartAuth,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .testTag("welcome_start_button"),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = PrimaryGreen,
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "اڪائونٽ شروع ڪريو / لاگ ان",
                            style = MaterialTheme.typography.labelLarge.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp
                            )
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Guest Browse Button
                OutlinedButton(
                    onClick = onBrowseAsGuest,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .testTag("welcome_guest_button"),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = PrimaryDark
                    )
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Visibility,
                            contentDescription = null,
                            tint = PrimaryGreen,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "پروفائل جو نمونو ڏسو (گيسٽ طور)",
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = FontWeight.SemiBold,
                                color = PrimaryDark,
                                fontSize = 14.sp
                            )
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Feature Highlights Row
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            FeatureHighlight(
                icon = Icons.Default.People,
                title = "100% سنڌي خانداني وقار",
                desc = "رشتن جي چونڊ ۽ خانداني روايتن جو خاص خيال"
            )
            Spacer(modifier = Modifier.height(8.dp))
            FeatureHighlight(
                icon = Icons.Default.Lock,
                title = "تصويرن تي رازداري بلر ڪنٽرول",
                desc = "توهان جي تصوير صرف توهان جي اجازت سان ظاهر ٿيندي"
            )
            Spacer(modifier = Modifier.height(8.dp))
            FeatureHighlight(
                icon = Icons.Default.CheckCircle,
                title = "والدين جي باضابطه وساطت",
                desc = "فون نمبر عوامي طور شايع ناهي ڪيو ويندو"
            )
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
fun FeatureHighlight(icon: ImageVector, title: String, desc: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(Color.White)
            .border(1.dp, BorderColor, RoundedCornerShape(12.dp))
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(GoldLight),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = PrimaryGreen,
                modifier = Modifier.size(20.dp)
            )
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = PrimaryDark,
                    fontSize = 13.5.sp
                )
            )
            Text(
                text = desc,
                style = MaterialTheme.typography.bodySmall.copy(
                    color = Color(0xFF64748B),
                    fontSize = 11.5.sp
                )
            )
        }
    }
}
