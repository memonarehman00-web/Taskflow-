package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AppLanguage
import com.example.ui.theme.GoldAccent
import com.example.ui.theme.GoldLight
import com.example.ui.theme.PrimaryDark
import com.example.ui.theme.PrimaryGreen
import com.example.ui.viewmodel.Screen

@Composable
fun AppTopBar(
    currentScreen: Screen,
    language: AppLanguage,
    onLanguageChange: (AppLanguage) -> Unit,
    canNavigateBack: Boolean,
    onBackClick: () -> Unit,
    onSafetyPolicyClick: () -> Unit
) {
    var showLangMenu by remember { mutableStateOf(false) }

    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = PrimaryGreen,
        shadowElevation = 6.dp
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(PrimaryDark, PrimaryGreen)
                    )
                )
                .statusBarsPadding()
                .padding(horizontal = 16.dp, vertical = 10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    if (canNavigateBack) {
                        IconButton(
                            onClick = onBackClick,
                            modifier = Modifier
                                .testTag("top_bar_back_button")
                                .size(40.dp)
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "واپس",
                                tint = GoldLight
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                    }

                    Column {
                        val title = when (language) {
                            AppLanguage.SINDHI -> currentScreen.titleSindhi
                            AppLanguage.URDU -> currentScreen.titleUrdu
                            AppLanguage.ENGLISH -> currentScreen.titleEnglish
                        }
                        Text(
                            text = title,
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            ),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            text = if (language == AppLanguage.SINDHI) "سنڌي رشتو پاڪستان"
                            else if (language == AppLanguage.URDU) "سندھی رشتہ پاکستان"
                            else "Sindhi Rishta Pakistan",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = GoldAccent,
                                fontSize = 11.sp
                            )
                        )
                    }
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Safety Guidelines icon button
                    IconButton(
                        onClick = onSafetyPolicyClick,
                        modifier = Modifier
                            .testTag("top_bar_safety_button")
                            .size(38.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Shield,
                            contentDescription = "تحفظ ضابطا",
                            tint = GoldAccent,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(4.dp))

                    // Language Selector
                    Box {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .testTag("top_bar_language_button")
                                .clip(RoundedCornerShape(16.dp))
                                .background(Color(0x33000000))
                                .clickable { showLangMenu = true }
                                .padding(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Language,
                                contentDescription = "ٻولي",
                                tint = GoldLight,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = language.label,
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = Color.White,
                                    fontWeight = FontWeight.Medium
                                )
                            )
                        }

                        DropdownMenu(
                            expanded = showLangMenu,
                            onDismissRequest = { showLangMenu = false }
                        ) {
                            AppLanguage.values().forEach { lang ->
                                DropdownMenuItem(
                                    text = { Text(lang.label) },
                                    onClick = {
                                        onLanguageChange(lang)
                                        showLangMenu = false
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
