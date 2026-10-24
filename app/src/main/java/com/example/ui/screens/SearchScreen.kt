package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.FilterCriteria
import com.example.data.model.Profile
import com.example.ui.components.ProfileCard
import com.example.ui.components.SelectablePill
import com.example.ui.theme.BorderColor
import com.example.ui.theme.GoldAccent
import com.example.ui.theme.PrimaryDark
import com.example.ui.theme.PrimaryGreen

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SearchScreen(
    currentFilter: FilterCriteria,
    matchingProfiles: List<Profile>,
    favorites: Set<String>,
    globalBlurEnabled: Boolean,
    onFilterChange: (FilterCriteria) -> Unit,
    onResetFilters: () -> Unit,
    onToggleFavorite: (String) -> Unit,
    onViewDetails: (Profile) -> Unit,
    onRequestContact: (Profile) -> Unit,
    onReport: (Profile) -> Unit
) {
    val cities = listOf("سڀ", "حيدرآباد", "ڪراچي", "سکر", "لاڙڪاڻو", "ميرپورخاص", "بينظير آباد", "خيرپور", "دادو", "شڪارپور")
    val educations = listOf("سڀ", "ميڊيڪل", "انجنيئرنگ", "ماسٽرس", "گريجوئيشن", "ديني تعليم")
    val maritals = listOf("سڀ", "اڻ پرڻيل", "طلاق يافته", "بيواهه")

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8FAF7)),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("search_filter_card"),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = CardDefaults.outlinedCardBorder()
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "رشتن جي ڳولا ۽ فلٽر",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = PrimaryDark,
                                fontSize = 17.sp
                            )
                        )
                        Icon(
                            imageVector = Icons.Default.FilterList,
                            contentDescription = null,
                            tint = PrimaryGreen,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Age Range Slider
                    Text(
                        text = "گهٽ ۾ گهٽ عمر: ${currentFilter.minAge} سال",
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = PrimaryDark
                        )
                    )
                    Slider(
                        value = currentFilter.minAge.toFloat(),
                        onValueChange = { onFilterChange(currentFilter.copy(minAge = it.toInt())) },
                        valueRange = 18f..50f,
                        steps = 31,
                        modifier = Modifier.testTag("search_age_slider"),
                        colors = SliderDefaults.colors(
                            thumbColor = PrimaryGreen,
                            activeTrackColor = PrimaryGreen,
                            inactiveTrackColor = Color(0xFFE2E8F0)
                        )
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // Gender Selection
                    Text(
                        text = "جنس چونڊيو:",
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        listOf("سڀ", "مرد", "عورت").forEach { g ->
                            val isSelected = (g == "سڀ" && currentFilter.gender.isEmpty()) || (currentFilter.gender == g)
                            SelectablePill(
                                label = g,
                                isSelected = isSelected,
                                modifier = Modifier.weight(1f),
                                onClick = {
                                    val newG = if (g == "سڀ") "" else g
                                    onFilterChange(currentFilter.copy(gender = newG))
                                }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // City Selection
                    Text(
                        text = "شهر جي چونڊ:",
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        cities.forEach { c ->
                            val isSelected = (c == "سڀ" && currentFilter.city.isEmpty()) || (currentFilter.city == c)
                            SelectablePill(
                                label = c,
                                isSelected = isSelected,
                                onClick = {
                                    val newC = if (c == "سڀ") "" else c
                                    onFilterChange(currentFilter.copy(city = newC))
                                }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Education Filter
                    Text(
                        text = "تعليم:",
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        educations.forEach { e ->
                            val isSelected = (e == "سڀ" && currentFilter.education.isEmpty()) || (currentFilter.education == e)
                            SelectablePill(
                                label = e,
                                isSelected = isSelected,
                                onClick = {
                                    val newE = if (e == "سڀ") "" else e
                                    onFilterChange(currentFilter.copy(education = newE))
                                }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Marital Status Filter
                    Text(
                        text = "ازدواجي حيثيت:",
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        maritals.forEach { m ->
                            val isSelected = (m == "سڀ" && currentFilter.marital.isEmpty()) || (currentFilter.marital == m)
                            SelectablePill(
                                label = m,
                                isSelected = isSelected,
                                modifier = Modifier.weight(1f),
                                onClick = {
                                    val newM = if (m == "سڀ") "" else m
                                    onFilterChange(currentFilter.copy(marital = newM))
                                }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Results counter and reset button
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "ملندڙ رشتا: ${matchingProfiles.size}",
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = PrimaryGreen
                            )
                        )

                        OutlinedButton(
                            onClick = onResetFilters,
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.testTag("search_reset_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Clear,
                                contentDescription = null,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("فلٽر ختم ڪريو")
                        }
                    }
                }
            }
        }

        item {
            Text(
                text = "ڳولا جا نتيجا (${matchingProfiles.size})",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = PrimaryDark
                ),
                modifier = Modifier.padding(horizontal = 4.dp)
            )
        }

        items(matchingProfiles, key = { it.id }) { profile ->
            ProfileCard(
                profile = profile,
                isFavorite = favorites.contains(profile.id),
                globalBlurEnabled = globalBlurEnabled,
                onToggleFavorite = { onToggleFavorite(profile.id) },
                onViewDetails = { onViewDetails(profile) },
                onRequestContact = { onRequestContact(profile) },
                onReport = { onReport(profile) }
            )
        }
    }
}
