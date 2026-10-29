package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Fastfood
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.MenuCategory
import com.example.data.MenuItem
import com.example.data.RiskMenuRepository
import com.example.ui.components.CategorySelectorRow
import com.example.ui.components.MenuItemCard
import com.example.ui.theme.RiskBlack
import com.example.ui.theme.RiskBrightRed
import com.example.ui.theme.RiskDarkRed
import com.example.ui.theme.RiskGold
import com.example.ui.theme.RiskPrimaryRed
import com.example.viewmodel.RiskUiState

@Composable
fun MenuScreen(
    uiState: RiskUiState,
    onCategorySelect: (MenuCategory) -> Unit,
    onSearchChange: (String) -> Unit,
    onItemClick: (MenuItem) -> Unit,
    onFavoriteToggle: (String) -> Unit,
    onOpenOriginalMenu: () -> Unit,
    onCallClick: () -> Unit
) {
    // Filter items based on category, search query, and dynamic price overrides
    val filteredItems = remember(uiState.selectedCategory, uiState.searchQuery, uiState.menuOverrides) {
        val allEffective = RiskMenuRepository.menuItems.map { original ->
            val override = uiState.menuOverrides[original.id]
            if (override != null) {
                val updatedSizePrices = if (override.sizePrices.isNotEmpty()) {
                    val map = original.sizePrices.toMutableMap()
                    override.sizePrices.forEach { (sizeKey, price) ->
                        val sizeEnum = try { com.example.data.ItemSize.valueOf(sizeKey) } catch (e: Exception) { null }
                        if (sizeEnum != null) map[sizeEnum] = price
                    }
                    map
                } else {
                    original.sizePrices
                }
                original.copy(
                    priceSingle = override.priceSingle ?: original.priceSingle,
                    sizePrices = updatedSizePrices,
                    isAvailable = override.isAvailable
                )
            } else {
                original
            }
        }

        val baseList = if (uiState.selectedCategory == MenuCategory.ALL) {
            allEffective
        } else {
            allEffective.filter { it.category == uiState.selectedCategory }
        }

        if (uiState.searchQuery.isBlank()) {
            baseList
        } else {
            val q = uiState.searchQuery.trim().lowercase()
            baseList.filter {
                it.nameAr.contains(q, ignoreCase = true) ||
                it.descriptionAr.contains(q, ignoreCase = true)
            }
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        // Search bar
        OutlinedTextField(
            value = uiState.searchQuery,
            onValueChange = onSearchChange,
            placeholder = { Text("ابحث في منيو ريسك (بيتزا، كريب، صاروخ...)") },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            },
            trailingIcon = {
                if (uiState.searchQuery.isNotEmpty()) {
                    IconButton(onClick = { onSearchChange("") }) {
                        Icon(imageVector = Icons.Default.Clear, contentDescription = "مسح البحث")
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .testTag("menu_search_field"),
            singleLine = true,
            shape = RoundedCornerShape(14.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                focusedBorderColor = RiskBrightRed,
                unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
            )
        )

        // Categories chip selector
        CategorySelectorRow(
            selectedCategory = uiState.selectedCategory,
            onCategorySelected = onCategorySelect
        )

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 80.dp)
        ) {
            // Hero Promo Banner
            if (uiState.searchQuery.isBlank() && uiState.selectedCategory == MenuCategory.ALL) {
                item {
                    PromoHeroBanner(
                        onOpenOriginalMenu = onOpenOriginalMenu,
                        onCallClick = onCallClick
                    )
                }
            }

            // Section Header
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (uiState.searchQuery.isNotBlank()) "نتائج البحث (${filteredItems.size})"
                        else uiState.selectedCategory.titleAr,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    )

                    Text(
                        text = "${filteredItems.size} صنف",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            // Empty state if nothing matches
            if (filteredItems.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(40.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                imageVector = Icons.Default.Fastfood,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f),
                                modifier = Modifier.size(64.dp)
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = "لم يتم العثور على وجبات مطابقة",
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "جرب البحث بكلمة أخرى أو تصفح الأقسام",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }

            // Food items
            items(filteredItems, key = { it.id }) { item ->
                val isFav = uiState.favoriteIds.contains(item.id)
                MenuItemCard(
                    item = item,
                    isFavorite = isFav,
                    onFavoriteToggle = { onFavoriteToggle(item.id) },
                    onItemClick = { onItemClick(item) },
                    onAddClick = { onItemClick(item) }
                )
            }
        }
    }
}

@Composable
fun PromoHeroBanner(
    onOpenOriginalMenu: () -> Unit,
    onCallClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.horizontalGradient(
                        colors = listOf(RiskDarkRed, RiskBlack, Color(0xFF1E0505))
                    )
                )
                .padding(16.dp)
        ) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        color = RiskGold,
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = "🔥 خدمة التوصيل متاحة بقنا",
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Black,
                            color = Color.Black
                        )
                    }

                    Text(
                        text = "Since 2025",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = RiskGold
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "أشهى بيتزا إيطالي وفطير شرقي وكريب في قنا!",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White
                    )
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "المنشية - أمام كافتيريا الزهراء • تذوق الفخامة مع أصناف ريسك الخاصة",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = Color.White.copy(alpha = 0.85f)
                    )
                )

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = onOpenOriginalMenu,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = RiskGold,
                            contentColor = Color.Black
                        ),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(
                            imageVector = Icons.Default.MenuBook,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("المنيو الورقي", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }

                    Button(
                        onClick = onCallClick,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = RiskPrimaryRed,
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Phone,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("اتصل الآن", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}
