package com.example.ui.screens

import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Profile
import com.example.ui.components.ProfileCard
import com.example.ui.theme.DangerRed
import com.example.ui.theme.PrimaryDark
import com.example.ui.theme.PrimaryGreen

@Composable
fun FavoritesScreen(
    favoriteProfiles: List<Profile>,
    globalBlurEnabled: Boolean,
    onToggleFavorite: (String) -> Unit,
    onViewDetails: (Profile) -> Unit,
    onRequestContact: (Profile) -> Unit,
    onReport: (Profile) -> Unit,
    onExploreMatches: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8FAF7))
    ) {
        if (favoriteProfiles.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Outlined.FavoriteBorder,
                        contentDescription = null,
                        tint = Color(0xFFCBD5E1),
                        modifier = Modifier.size(70.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "ڪوبه رشتو محفوظ ناهي ٿيل",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = PrimaryDark
                        )
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "جڏهن توهان کي ڪو رشتو پسند اچي، ته دل واري بٽڻ تي ٽيپ ڪري محفوظ ڪري سگهو ٿا.",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = Color(0xFF64748B),
                            lineHeight = 18.sp
                        )
                    )
                    Spacer(modifier = Modifier.height(18.dp))
                    Button(
                        onClick = onExploreMatches,
                        colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.testTag("explore_matches_from_fav_button")
                    ) {
                        Text("رشتا ڏسو")
                    }
                }
            }
        } else {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(horizontal = 16.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Favorite,
                    contentDescription = null,
                    tint = DangerRed,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "محفوظ ڪيل رشتا (${favoriteProfiles.size})",
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = PrimaryDark
                    )
                )
            }

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .testTag("favorites_list"),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(favoriteProfiles, key = { it.id }) { profile ->
                    ProfileCard(
                        profile = profile,
                        isFavorite = true,
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
