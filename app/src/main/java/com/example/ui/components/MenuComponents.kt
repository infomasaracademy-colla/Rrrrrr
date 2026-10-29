package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.ExtraOption
import com.example.data.ItemSize
import com.example.data.MenuCategory
import com.example.data.MenuItem
import com.example.data.RiskMenuRepository
import com.example.data.SauceOption
import com.example.ui.theme.RiskBrightRed
import com.example.ui.theme.RiskGold
import com.example.ui.theme.RiskPrimaryRed

@Composable
fun RiskTopBar(
    cartCount: Int,
    onCartClick: () -> Unit,
    onCallClick: () -> Unit,
    onOpenMenuGallery: () -> Unit,
    onAiClick: () -> Unit,
    onLogoClick: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.surface,
        shadowElevation = 6.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Right / Start side: Logo and Restaurant Brand
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .clickable { onLogoClick() }
            ) {
                Image(
                    painter = painterResource(id = R.drawable.risk_logo),
                    contentDescription = "شعار مطعم ريسك",
                    modifier = Modifier
                        .size(46.dp)
                        .clip(CircleShape)
                        .border(1.5.dp, RiskGold, CircleShape)
                )

                Spacer(modifier = Modifier.width(10.dp))

                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "RISK",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Black,
                                letterSpacing = 1.sp,
                                color = RiskBrightRed
                            )
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Fast foods",
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = RiskGold
                            )
                        )
                    }
                    Text(
                        text = "قنا - المنشية (أمام كافتيريا الزهراء)",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            // Left / Actions side
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                // AI Features Button
                IconButton(
                    onClick = onAiClick,
                    modifier = Modifier.size(38.dp).testTag("ai_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.AutoAwesome,
                        contentDescription = "ريسك الذكي ومعاينة 3D",
                        tint = RiskGold
                    )
                }

                // View Original Menu Button
                IconButton(
                    onClick = onOpenMenuGallery,
                    modifier = Modifier.size(38.dp).testTag("original_menu_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.MenuBook,
                        contentDescription = "عرض صور المنيو الورقي",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                // Quick Call Button
                IconButton(
                    onClick = onCallClick,
                    modifier = Modifier.size(38.dp).testTag("quick_call_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Call,
                        contentDescription = "اتصال بالمطعم",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }

                // Cart Icon with Badge
                IconButton(
                    onClick = onCartClick,
                    modifier = Modifier.size(38.dp).testTag("cart_button")
                ) {
                    BadgedBox(
                        badge = {
                            if (cartCount > 0) {
                                Badge(
                                    containerColor = RiskBrightRed,
                                    contentColor = Color.White
                                ) {
                                    Text(
                                        text = cartCount.toString(),
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.ShoppingCart,
                            contentDescription = "سلة المشتريات",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CategorySelectorRow(
    selectedCategory: MenuCategory,
    onCategorySelected: (MenuCategory) -> Unit
) {
    val scrollState = rememberScrollState()
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(scrollState)
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        MenuCategory.entries.forEach { category ->
            val isSelected = category == selectedCategory
            FilterChip(
                selected = isSelected,
                onClick = { onCategorySelected(category) },
                label = {
                    Text(
                        text = category.titleAr,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                        fontSize = 13.sp
                    )
                },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = RiskPrimaryRed,
                    selectedLabelColor = Color.White,
                    containerColor = MaterialTheme.colorScheme.surfaceVariant,
                    labelColor = MaterialTheme.colorScheme.onSurfaceVariant
                ),
                border = FilterChipDefaults.filterChipBorder(
                    enabled = true,
                    selected = isSelected,
                    borderColor = if (isSelected) RiskGold else MaterialTheme.colorScheme.outline
                ),
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier.testTag("category_chip_${category.name}")
            )
        }
    }
}

@Composable
fun MenuItemCard(
    item: MenuItem,
    isFavorite: Boolean,
    onFavoriteToggle: () -> Unit,
    onItemClick: () -> Unit,
    onAddClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .clickable { onItemClick() }
            .testTag("menu_item_card_${item.id}"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        border = BorderStroke(
            1.dp,
            if (item.isRiskSpecial) RiskGold else MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = item.nameAr,
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            ),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )

                        if (item.isRiskSpecial) {
                            Spacer(modifier = Modifier.width(6.dp))
                            Surface(
                                color = RiskGold.copy(alpha = 0.2f),
                                shape = RoundedCornerShape(6.dp),
                                border = BorderStroke(1.dp, RiskGold)
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Star,
                                        contentDescription = null,
                                        tint = RiskGold,
                                        modifier = Modifier.size(12.dp)
                                    )
                                    Spacer(modifier = Modifier.width(2.dp))
                                    Text(
                                        text = "ريسك سبيشال",
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = RiskGold
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = item.descriptionAr,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                // Favorite button
                IconButton(
                    onClick = onFavoriteToggle,
                    modifier = Modifier.size(36.dp)
                ) {
                    Icon(
                        imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = "إضافة للمفضلة",
                        tint = if (isFavorite) RiskBrightRed else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Pricing and Size pills or single price
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (item.sizePrices.isNotEmpty()) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        item.sizePrices.forEach { (size, price) ->
                            Surface(
                                color = MaterialTheme.colorScheme.surface,
                                shape = RoundedCornerShape(8.dp),
                                border = BorderStroke(0.8.dp, MaterialTheme.colorScheme.outline)
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "${size.titleAr}: ",
                                        fontSize = 11.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                    Text(
                                        text = "${price.toInt()} ج.م",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = RiskBrightRed
                                    )
                                }
                            }
                        }
                    }
                } else if (item.priceSingle != null) {
                    Text(
                        text = "${item.priceSingle.toInt()} ج.م",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.ExtraBold,
                            color = RiskBrightRed
                        )
                    )
                }

                // Add to cart / customize button
                Button(
                    onClick = onAddClick,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = RiskPrimaryRed,
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(10.dp),
                    contentPadding = ButtonDefaults.ButtonWithIconContentPadding,
                    modifier = Modifier.height(38.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "اطلب",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun ItemCustomizationSheet(
    item: MenuItem,
    onDismiss: () -> Unit,
    onAddToCart: (
        size: ItemSize?,
        stuffedCrust: Boolean,
        extras: List<ExtraOption>,
        sauces: List<SauceOption>,
        notes: String,
        quantity: Int
    ) -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    // Default size selection: first available size if sizePrices exists
    var selectedSize by remember {
        mutableStateOf(item.sizePrices.keys.firstOrNull() ?: ItemSize.MEDIUM)
    }
    var stuffedCrust by remember { mutableStateOf(false) }
    val selectedExtras = remember { mutableStateListOf<ExtraOption>() }
    val selectedSauces = remember { mutableStateListOf<SauceOption>() }
    var notes by remember { mutableStateOf("") }
    var quantity by remember { mutableIntStateOf(1) }

    // Live price calculation
    val currentBasePrice = if (item.sizePrices.isNotEmpty()) {
        item.sizePrices[selectedSize] ?: item.getStartingPrice()
    } else {
        item.priceSingle ?: item.getStartingPrice()
    }
    val crustPrice = if (stuffedCrust) 25.0 else 0.0
    val extrasPrice = selectedExtras.sumOf { it.price }
    val saucesPrice = selectedSauces.sumOf { it.price }
    val unitPrice = currentBasePrice + crustPrice + extrasPrice + saucesPrice
    val finalPrice = unitPrice * quantity

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.surface
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 8.dp)
        ) {
            // Title & close
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = item.nameAr,
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    )
                    Text(
                        text = item.descriptionAr,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                IconButton(onClick = onDismiss) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = "إغلاق")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 1. Size Selection (if multiple sizes)
            if (item.sizePrices.isNotEmpty()) {
                Text(
                    text = "اختر الحجم:",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    item.sizePrices.forEach { (size, price) ->
                        val isSelected = selectedSize == size
                        Surface(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(12.dp))
                                .clickable { selectedSize = size },
                            color = if (isSelected) RiskPrimaryRed else MaterialTheme.colorScheme.surfaceVariant,
                            border = BorderStroke(
                                1.5.dp,
                                if (isSelected) RiskGold else MaterialTheme.colorScheme.outline
                            ),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Column(
                                modifier = Modifier.padding(vertical = 10.dp, horizontal = 4.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = size.titleAr,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = "${price.toInt()} ج.م",
                                    fontSize = 12.sp,
                                    color = if (isSelected) RiskGold else RiskBrightRed,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }

            // 2. Stuffed Crust Option (for Pizzas as noted on the printed menu: حشو الأطراف حسب الطلب)
            if (item.allowsStuffedCrust) {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .clickable { stuffedCrust = !stuffedCrust },
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    border = BorderStroke(
                        1.dp,
                        if (stuffedCrust) RiskGold else MaterialTheme.colorScheme.outline
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Checkbox(
                                checked = stuffedCrust,
                                onCheckedChange = { stuffedCrust = it },
                                colors = CheckboxDefaults.colors(checkedColor = RiskPrimaryRed)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Column {
                                Text(
                                    text = "حشو الأطراف جبنة موتزاريلا",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp
                                )
                                Text(
                                    text = "أطراف محشوة غنية بالجبن السايحة",
                                    fontSize = 11.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                        Text(
                            text = "+25 ج.م",
                            fontWeight = FontWeight.Bold,
                            color = RiskBrightRed,
                            fontSize = 13.sp
                        )
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }

            // 3. Extras Selection
            Text(
                text = "الإضافات اللذيذة (اختياري):",
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                RiskMenuRepository.availableExtras.forEach { extra ->
                    val isChecked = selectedExtras.contains(extra)
                    FilterChip(
                        selected = isChecked,
                        onClick = {
                            if (isChecked) selectedExtras.remove(extra) else selectedExtras.add(extra)
                        },
                        label = {
                            Text(
                                text = "${extra.nameAr} (+${extra.price.toInt()} ج)",
                                fontSize = 11.sp
                            )
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = RiskPrimaryRed.copy(alpha = 0.2f),
                            selectedLabelColor = MaterialTheme.colorScheme.primary
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // 4. Sauces Selection
            Text(
                text = "الصوصات المفضلة:",
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                RiskMenuRepository.availableSauces.forEach { sauce ->
                    val isChecked = selectedSauces.contains(sauce)
                    FilterChip(
                        selected = isChecked,
                        onClick = {
                            if (isChecked) selectedSauces.remove(sauce) else selectedSauces.add(sauce)
                        },
                        label = {
                            Text(
                                text = "${sauce.nameAr} (+${sauce.price.toInt()} ج)",
                                fontSize = 11.sp
                            )
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = RiskGold.copy(alpha = 0.2f),
                            selectedLabelColor = RiskGold
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // 5. Special Notes
            OutlinedTextField(
                value = notes,
                onValueChange = { notes = it },
                label = { Text("أي ملاحظات خاصة؟ (مثلاً: بدون بصل، صوص زيادة)") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = RiskGold,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            // 6. Quantity and Add to Cart Button
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 20.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Quantity controls
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(12.dp))
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    IconButton(
                        onClick = { if (quantity > 1) quantity-- },
                        modifier = Modifier.size(36.dp)
                    ) {
                        Icon(imageVector = Icons.Default.Remove, contentDescription = "تقليل")
                    }
                    Text(
                        text = "$quantity",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        modifier = Modifier.padding(horizontal = 10.dp)
                    )
                    IconButton(
                        onClick = { quantity++ },
                        modifier = Modifier.size(36.dp)
                    ) {
                        Icon(imageVector = Icons.Default.Add, contentDescription = "زيادة")
                    }
                }

                // Confirm Add button
                Button(
                    onClick = {
                        val finalChosenSize = if (item.sizePrices.isNotEmpty()) selectedSize else null
                        onAddToCart(finalChosenSize, stuffedCrust, selectedExtras.toList(), selectedSauces.toList(), notes, quantity)
                    },
                    modifier = Modifier
                        .height(48.dp)
                        .weight(1f)
                        .padding(start = 12.dp)
                        .testTag("confirm_add_to_cart_button"),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = RiskPrimaryRed,
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = "إضافة للسلة (${finalPrice.toInt()} ج.م)",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}
