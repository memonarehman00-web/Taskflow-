package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.GroupOff
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.example.ui.theme.GoldAccent
import com.example.ui.theme.GoldLight
import com.example.ui.theme.PrimaryDark
import com.example.ui.theme.PrimaryGreen

@Composable
fun MatchesFeedScreen(
    profiles: List<Profile>,
    favorites: Set<String>,
    globalBlurEnabled: Boolean,
    currentFilter: FilterCriteria,
    onFilterChange: (FilterCriteria) -> Unit,
    onToggleFavorite: (String) -> Unit,
    onViewDetails: (Profile) -> Unit,
    onRequestContact: (Profile) -> Unit,
    onReport: (Profile) -> Unit,
    onOpenSearchTab: () -> Unit
) {
    val quickCities = listOf("سڀ", "حيدرآباد", "ڪراچي", "سکر", "لاڙڪاڻو", "ميرپورخاص", "بينظير آباد", "خيرپور")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8FAF7))
    ) {
        // Top Filter Bar & Quick Chips
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(vertical = 8.dp)
        ) {
            // Stats & Search Bar Button
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF16A34A))
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "دستياب رشتا: ${profiles.size}",
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = PrimaryDark
                        )
                    )
                }

                // Quick Filter shortcut button
                OutlinedButton(
                    onClick = onOpenSearchTab,
                    shape = RoundedCornerShape(20.dp),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                    modifier = Modifier.testTag("matches_open_filter_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.FilterList,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = PrimaryGreen
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "ڳولا ۽ فلٽر",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = PrimaryGreen,
                            fontWeight = FontWeight.SemiBold
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            // Quick City Chips Row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                quickCities.forEach { city ->
                    val isSelected = (city == "سڀ" && currentFilter.city.isEmpty()) || (currentFilter.city == city)
                    SelectablePill(
                        label = city,
                        isSelected = isSelected,
                        onClick = {
                            val newCity = if (city == "سڀ") "" else city
                            onFilterChange(currentFilter.copy(city = newCity))
                        }
                    )
                }

                // Gender quick toggles
                SelectablePill(
                    label = "صرف عورت",
                    isSelected = currentFilter.gender == "عورت",
                    onClick = {
                        val newG = if (currentFilter.gender == "عورت") "" else "عورت"
                        onFilterChange(currentFilter.copy(gender = newG))
                    }
                )
                SelectablePill(
                    label = "صرف مرد",
                    isSelected = currentFilter.gender == "مرد",
                    onClick = {
                        val newG = if (currentFilter.gender == "مرد") "" else "مرد"
                        onFilterChange(currentFilter.copy(gender = newG))
                    }
                )
            }
        }

        // Feed list or empty state
        if (profiles.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Default.GroupOff,
                        contentDescription = null,
                        tint = Color(0xFF94A3B8),
                        modifier = Modifier.size(64.dp)
                    )
                    Spacer(modifier = Modifier.height(14.dp))
                    Text(
                        text = "ڪوبه رشتو نه مليو",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = PrimaryDark
                        )
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "توهان جي چونڊيل فلٽر مطابق هن وقت ڪو رشتو دستياب ناهي. مهرباني ڪري فلٽر تبديل ڪريو.",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = Color(0xFF64748B),
                            lineHeight = 18.sp
                        )
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = { onFilterChange(FilterCriteria()) },
                        colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text("سڀ فلٽر ختم ڪريو")
                    }
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .testTag("matches_feed_list"),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(profiles, key = { it.id }) { profile ->
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
    }
}
