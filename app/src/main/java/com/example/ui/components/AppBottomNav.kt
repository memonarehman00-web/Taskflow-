package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Group
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AppLanguage
import com.example.ui.theme.GoldAccent
import com.example.ui.theme.GoldLight
import com.example.ui.theme.PrimaryDark
import com.example.ui.theme.PrimaryGreen
import com.example.ui.viewmodel.Screen

private data class NavItemData(
    val screen: Screen,
    val sindhiLabel: String,
    val urduLabel: String,
    val englishLabel: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val testTag: String
)

@Composable
fun AppBottomNav(
    currentScreen: Screen,
    favoriteCount: Int,
    language: AppLanguage,
    onTabSelected: (Screen) -> Unit
) {
    val items = listOf(
        NavItemData(
            screen = Screen.Matches,
            sindhiLabel = "رشتا",
            urduLabel = "رشتے",
            englishLabel = "Matches",
            selectedIcon = Icons.Filled.Group,
            unselectedIcon = Icons.Outlined.Group,
            testTag = "nav_item_matches"
        ),
        NavItemData(
            screen = Screen.Search,
            sindhiLabel = "ڳولا",
            urduLabel = "تلاش",
            englishLabel = "Search",
            selectedIcon = Icons.Filled.Search,
            unselectedIcon = Icons.Outlined.Search,
            testTag = "nav_item_search"
        ),
        NavItemData(
            screen = Screen.Favorites,
            sindhiLabel = "پسنديده",
            urduLabel = "پسندیدہ",
            englishLabel = "Saved",
            selectedIcon = Icons.Filled.Favorite,
            unselectedIcon = Icons.Outlined.FavoriteBorder,
            testTag = "nav_item_favorites"
        ),
        NavItemData(
            screen = Screen.Premium,
            sindhiLabel = "پريميئم",
            urduLabel = "پریمیم",
            englishLabel = "Premium",
            selectedIcon = Icons.Filled.Star,
            unselectedIcon = Icons.Outlined.StarBorder,
            testTag = "nav_item_premium"
        ),
        NavItemData(
            screen = Screen.Settings,
            sindhiLabel = "سيٽنگس",
            urduLabel = "سیٹنگز",
            englishLabel = "Settings",
            selectedIcon = Icons.Filled.Settings,
            unselectedIcon = Icons.Outlined.Settings,
            testTag = "nav_item_settings"
        )
    )

    NavigationBar(
        containerColor = Color.White,
        contentColor = PrimaryDark,
        tonalElevation = 8.dp
    ) {
        items.forEach { item ->
            val isSelected = currentScreen == item.screen
            val label = when (language) {
                AppLanguage.SINDHI -> item.sindhiLabel
                AppLanguage.URDU -> item.urduLabel
                AppLanguage.ENGLISH -> item.englishLabel
            }

            NavigationBarItem(
                selected = isSelected,
                onClick = { onTabSelected(item.screen) },
                icon = {
                    if (item.screen == Screen.Favorites && favoriteCount > 0) {
                        BadgedBox(
                            badge = {
                                Badge(
                                    containerColor = GoldAccent,
                                    contentColor = PrimaryDark
                                ) {
                                    Text(
                                        text = "$favoriteCount",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 10.sp
                                    )
                                }
                            }
                        ) {
                            Icon(
                                imageVector = if (isSelected) item.selectedIcon else item.unselectedIcon,
                                contentDescription = label,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    } else {
                        Icon(
                            imageVector = if (isSelected) item.selectedIcon else item.unselectedIcon,
                            contentDescription = label,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                },
                label = {
                    Text(
                        text = label,
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                            fontSize = 11.5.sp
                        )
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color.White,
                    selectedTextColor = PrimaryGreen,
                    unselectedIconColor = Color(0xFF64748B),
                    unselectedTextColor = Color(0xFF64748B),
                    indicatorColor = PrimaryGreen
                ),
                modifier = Modifier.testTag(item.testTag)
            )
        }
    }
}
