package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.UserAccount
import com.example.ui.theme.BorderColor
import com.example.ui.theme.GoldAccent
import com.example.ui.theme.GoldLight
import com.example.ui.theme.PrimaryDark
import com.example.ui.theme.PrimaryGreen
import com.example.ui.theme.SuccessGreen

@Composable
fun PremiumScreen(
    userAccount: UserAccount,
    onUpgrade: (plan: String) -> Unit
) {
    var selectedMethod by remember { mutableStateOf("JazzCash") }
    var transactionId by remember { mutableStateOf("") }
    var showSuccessBanner by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8FAF7))
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(8.dp))

        // Premium Badge Header
        Box(
            modifier = Modifier
                .size(72.dp)
                .clip(CircleShape)
                .background(
                    Brush.linearGradient(listOf(GoldAccent, Color(0xFFAA7C11)))
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.WorkspacePremium,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(38.dp)
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "پريميئم سنڌي رڪنيت",
            style = MaterialTheme.typography.displayMedium.copy(
                fontWeight = FontWeight.Bold,
                color = PrimaryDark,
                fontSize = 24.sp
            ),
            textAlign = TextAlign.Center
        )

        Text(
            text = "باوقار ۽ سنجيده سنڌي خاندانن سان سڌو، محفوظ رابطو",
            style = MaterialTheme.typography.bodySmall.copy(
                color = Color(0xFF64748B),
                textAlign = TextAlign.Center
            )
        )

        Spacer(modifier = Modifier.height(18.dp))

        // Pricing Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .border(2.dp, GoldAccent, RoundedCornerShape(22.dp)),
            shape = RoundedCornerShape(22.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(GoldLight)
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "معزز سنڌي رشتو پلان",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = PrimaryDark,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    verticalAlignment = Alignment.Bottom,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = "1,500",
                        style = MaterialTheme.typography.displayMedium.copy(
                            fontWeight = FontWeight.ExtraBold,
                            color = Color(0xFFB45309),
                            fontSize = 32.sp
                        )
                    )
                    Text(
                        text = "پاڪستاني رپيا / ماهوار",
                        style = MaterialTheme.typography.labelMedium.copy(
                            color = Color(0xFF64748B),
                            fontWeight = FontWeight.SemiBold
                        ),
                        modifier = Modifier.padding(bottom = 6.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                PremiumFeatureItem("لامحدود پروفائل رابطا ۽ مڪمل احوال ڏسڻ جي اجازت")
                PremiumFeatureItem("پنهنجي پروفائل کي سنڌ مٿان اعليٰ درجي ۾ نمايان رکو")
                PremiumFeatureItem("سڌو سنئون والدين جو تصديق ٿيل رابطو نمبر کولي ڏسو")
                PremiumFeatureItem("پروفائل تي سنهري مستند تصديق ٿيل بئج (Gold Badge)")
                PremiumFeatureItem("حفاظتي ضابطن ۽ شرعي پردي جي مڪمل گارنٽي")

                Spacer(modifier = Modifier.height(20.dp))

                // Payment Options in Pakistan
                Text(
                    text = "پاڪستاني ادائگي جو طريقو چونڊيو:",
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = PrimaryDark
                    )
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    PaymentOptionPill(
                        label = "JazzCash",
                        subtitle = "0300-XXXXXXX",
                        icon = Icons.Default.PhoneAndroid,
                        isSelected = selectedMethod == "JazzCash",
                        modifier = Modifier.weight(1f)
                    ) { selectedMethod = "JazzCash" }

                    PaymentOptionPill(
                        label = "EasyPaisa",
                        subtitle = "03090063533",
                        icon = Icons.Default.PhoneAndroid,
                        isSelected = selectedMethod == "EasyPaisa",
                        modifier = Modifier.weight(1f)
                    ) { selectedMethod = "EasyPaisa" }

                    PaymentOptionPill(
                        label = "سنڌ بئنڪ",
                        subtitle = "Sindh Bank",
                        icon = Icons.Default.AccountBalance,
                        isSelected = selectedMethod == "سنڌ بئنڪ",
                        modifier = Modifier.weight(1f)
                    ) { selectedMethod = "سنڌ بئنڪ" }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Transaction ID Input
                Text(
                    text = "ٽرانزيڪشن آءِ ڊي / TID:",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF475569)
                    )
                )
                Spacer(modifier = Modifier.height(4.dp))
                OutlinedTextField(
                    value = transactionId,
                    onValueChange = { transactionId = it },
                    placeholder = { Text("مثال: 98451230") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("premium_tid_input"),
                    shape = RoundedCornerShape(10.dp)
                )

                Spacer(modifier = Modifier.height(18.dp))

                if (userAccount.isPremium) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0xFFE8F5E9))
                            .padding(12.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "توهان وٽ پريميئم رڪنيت چالو آهي! (Gold Member)",
                            style = MaterialTheme.typography.labelMedium.copy(
                                color = SuccessGreen,
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }
                } else {
                    Button(
                        onClick = {
                            onUpgrade(selectedMethod)
                            showSuccessBanner = true
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .testTag("premium_upgrade_button"),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = PrimaryGreen,
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            tint = GoldAccent,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "پريميئم رڪنيت چالو ڪريو",
                            style = MaterialTheme.typography.labelLarge.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp
                            )
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
fun PremiumFeatureItem(text: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(20.dp)
                .clip(CircleShape)
                .background(Color(0xFFE8F5E9)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = null,
                tint = PrimaryGreen,
                modifier = Modifier.size(13.dp)
            )
        }
        Spacer(modifier = Modifier.width(10.dp))
        Text(
            text = text,
            style = MaterialTheme.typography.bodySmall.copy(
                color = Color(0xFF334155),
                fontSize = 12.5.sp
            )
        )
    }
}

@Composable
fun PaymentOptionPill(
    label: String,
    subtitle: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    isSelected: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .background(if (isSelected) GoldLight else Color(0xFFF8FAFC))
            .border(1.dp, if (isSelected) GoldAccent else BorderColor, RoundedCornerShape(10.dp))
            .clickable(onClick = onClick)
            .padding(vertical = 8.dp, horizontal = 6.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = if (isSelected) PrimaryDark else Color(0xFF64748B),
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = if (isSelected) PrimaryDark else Color(0xFF334155),
                    fontSize = 11.sp
                )
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall.copy(
                    color = Color(0xFF94A3B8),
                    fontSize = 9.sp
                )
            )
        }
    }
}
