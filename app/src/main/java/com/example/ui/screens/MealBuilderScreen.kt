package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.LocalPizza
import androidx.compose.material.icons.filled.LunchDining
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
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
import com.example.data.ExtraOption
import com.example.data.MenuCategory
import com.example.data.SauceOption
import com.example.ui.theme.RiskBlack
import com.example.ui.theme.RiskBrightRed
import com.example.ui.theme.RiskDarkRed
import com.example.ui.theme.RiskGold
import com.example.ui.theme.RiskPrimaryRed
import com.example.viewmodel.RiskRestaurantViewModel

data class MealBase(val id: String, val name: String, val category: MenuCategory, val basePrice: Double, val iconDesc: String)
data class FillingOption(val id: String, val name: String, val price: Double)
data class CrustOption(val id: String, val name: String, val price: Double)

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun MealBuilderScreen(
    viewModel: RiskRestaurantViewModel,
    onNavigateToCart: () -> Unit
) {
    val bases = remember {
        listOf(
            MealBase("b_crepe", "عجينة كريب مقرمشة", MenuCategory.CREPE, 40.0, "مثلث كريب ساخن ومقرمش"),
            MealBase("b_feteer", "فطير شرقي مورق بالسمن", MenuCategory.FETEER_ORIENTAL, 65.0, "طبقات فطير مورقة وغنية"),
            MealBase("b_pizza", "عجينة بيتزا إيطالية خاصة", MenuCategory.PIZZA, 60.0, "عجينة مخمرة بصلصة إيطالية")
        )
    }

    val fillings = remember {
        listOf(
            FillingOption("f_strips", "استربس دجاج كرسبي", 35.0),
            FillingOption("f_zinger", "دجاج زنجر حار", 35.0),
            FillingOption("f_meat", "لحمة مفرومة بلدي", 40.0),
            FillingOption("f_sausage", "سجق بلدي متبل", 40.0),
            FillingOption("f_chicken", "صدور فراخ مشوية", 40.0),
            FillingOption("f_kofta", "كفتة بلدي جريل", 35.0),
            FillingOption("f_hotdog", "هوت دوج مدخن", 25.0),
            FillingOption("f_sosis", "سوسيس", 25.0),
            FillingOption("f_tuna", "تونة قطع", 35.0)
        )
    }

    val crustOptions = remember {
        listOf(
            CrustOption("c_none", "بدون حشو أطراف", 0.0),
            CrustOption("c_mozzarella", "حشو أطراف موتزاريلا مطاطية", 25.0),
            CrustOption("c_sosis_cheese", "حشو أطراف سوسيس مع جبن", 30.0)
        )
    }

    val cheeses = remember {
        listOf(
            ExtraOption("ch_moz", "موتزاريلا إكسترا", 20.0),
            ExtraOption("ch_roumi", "جبنة رومي قديمة", 20.0),
            ExtraOption("ch_cheddar", "شيدر سايحة", 15.0),
            ExtraOption("ch_kiri", "جبنة كيري غنية", 25.0),
            ExtraOption("ch_fries", "باكت بطاطس فارم فريتس", 20.0)
        )
    }

    val sauces = remember {
        listOf(
            SauceOption("sc_cheddar", "صوص جبنة شيدر", 20.0),
            SauceOption("sc_ranch", "صوص رانش كريمي", 20.0),
            SauceOption("sc_bbq", "صوص باربكيو مدخن", 10.0),
            SauceOption("sc_sweet_chili", "سويت شيلي", 20.0),
            SauceOption("sc_tomia", "تومية سورية فاخرة", 10.0),
            SauceOption("sc_hot", "هوت صوص حار", 10.0),
            SauceOption("sc_mayo", "مايونيز", 10.0)
        )
    }

    var selectedBase by remember { mutableStateOf(bases[0]) }
    val selectedFillings = remember { mutableStateListOf(fillings[0]) }
    var selectedCrust by remember { mutableStateOf(crustOptions[0]) }
    val selectedCheeses = remember { mutableStateListOf(cheeses[0]) }
    val selectedSauces = remember { mutableStateListOf(sauces[0]) }
    var customMealName by remember { mutableStateOf("وجبة ريسك الخاصة بي") }
    var specialInstructions by remember { mutableStateOf("") }

    // Live Total Price
    val totalPrice = selectedBase.basePrice +
            selectedFillings.sumOf { it.price } +
            selectedCrust.price +
            selectedCheeses.sumOf { it.price } +
            selectedSauces.sumOf { it.price }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 100.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Header
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                border = BorderStroke(1.5.dp, RiskGold)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Brush.horizontalGradient(listOf(RiskDarkRed, RiskBlack)))
                        .padding(18.dp)
                ) {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.AutoAwesome,
                                contentDescription = null,
                                tint = RiskGold,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "مهندس الوجبات (اصنع وجبتك بنفسك)",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Black,
                                    color = Color.White
                                )
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "اختر العجينة، الحشوات المفضلة، الأجبان، نوع حشو الأطراف والصوصات مع حساب فوري للسعر!",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.White.copy(alpha = 0.85f)
                        )
                    }
                }
            }
        }

        // Live Visual Stack Card
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.5f))
            ) {
                Column(
                    modifier = Modifier.padding(14.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "طبقات وجبتك المبتكرة:",
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    // Visual Representation Stack
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(10.dp))
                            .padding(12.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        // 1. Sauces Layer
                        if (selectedSauces.isNotEmpty()) {
                            Surface(
                                color = RiskGold.copy(alpha = 0.2f),
                                shape = RoundedCornerShape(6.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = "🍶 صوصات: " + selectedSauces.joinToString { it.nameAr },
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = RiskGold,
                                    modifier = Modifier.padding(6.dp)
                                )
                            }
                        }

                        // 2. Cheeses Layer
                        if (selectedCheeses.isNotEmpty()) {
                            Surface(
                                color = Color(0xFFFFE082).copy(alpha = 0.25f),
                                shape = RoundedCornerShape(6.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = "🧀 أجبان: " + selectedCheeses.joinToString { it.nameAr },
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Color(0xFFFFB300),
                                    modifier = Modifier.padding(6.dp)
                                )
                            }
                        }

                        // 3. Fillings Layer
                        if (selectedFillings.isNotEmpty()) {
                            Surface(
                                color = RiskBrightRed.copy(alpha = 0.15f),
                                shape = RoundedCornerShape(6.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = "🥩 حشوات: " + selectedFillings.joinToString { it.name },
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = RiskBrightRed,
                                    modifier = Modifier.padding(6.dp)
                                )
                            }
                        }

                        // 4. Crust Layer
                        if (selectedCrust.id != "c_none") {
                            Surface(
                                color = RiskPrimaryRed.copy(alpha = 0.2f),
                                shape = RoundedCornerShape(6.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = "🥖 " + selectedCrust.name,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = RiskPrimaryRed,
                                    modifier = Modifier.padding(6.dp)
                                )
                            }
                        }

                        // 5. Base Dough Layer
                        Surface(
                            color = MaterialTheme.colorScheme.surfaceVariant,
                            shape = RoundedCornerShape(6.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "🥞 القاعدة: " + selectedBase.name,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(6.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("السعر الإجمالي للوجبة:", fontWeight = FontWeight.Bold)
                        Text(
                            text = "${totalPrice.toInt()} ج.م",
                            fontWeight = FontWeight.Black,
                            fontSize = 18.sp,
                            color = RiskBrightRed
                        )
                    }
                }
            }
        }

        // Step 1: Base Selection
        item {
            Text(
                text = "1. اختر نوع العجينة / القاعدة الأساسية:",
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                bases.forEach { b ->
                    val isSelected = selectedBase.id == b.id
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .clickable { selectedBase = b },
                        color = if (isSelected) RiskPrimaryRed.copy(alpha = 0.15f) else MaterialTheme.colorScheme.surfaceVariant,
                        border = BorderStroke(
                            1.5.dp,
                            if (isSelected) RiskBrightRed else MaterialTheme.colorScheme.outline.copy(alpha = 0.4f)
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = if (isSelected) Icons.Default.CheckCircle else Icons.Default.LunchDining,
                                    contentDescription = null,
                                    tint = if (isSelected) RiskBrightRed else MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text(text = b.name, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                    Text(text = b.iconDesc, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                }
                            }
                            Text(
                                text = "${b.basePrice.toInt()} ج.م",
                                fontWeight = FontWeight.Bold,
                                color = RiskBrightRed,
                                fontSize = 13.sp
                            )
                        }
                    }
                }
            }
        }

        // Step 2: Main Meat/Protein Fillings (Multiple selection)
        item {
            Text(
                text = "2. اختر الحشوات والبروتين (يمكنك اختيار أكثر من صنف):",
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                fillings.forEach { f ->
                    val isSelected = selectedFillings.contains(f)
                    FilterChip(
                        selected = isSelected,
                        onClick = {
                            if (isSelected) {
                                if (selectedFillings.size > 1) selectedFillings.remove(f)
                            } else {
                                selectedFillings.add(f)
                            }
                        },
                        label = {
                            Text("${f.name} (+${f.price.toInt()} ج)", fontSize = 12.sp)
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = RiskPrimaryRed,
                            selectedLabelColor = Color.White
                        )
                    )
                }
            }
        }

        // Step 3: Stuffed Crust (حشو الأطراف حسب الطلب)
        item {
            Text(
                text = "3. خيار حشو الأطراف حسب الطلب:",
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                crustOptions.forEach { crust ->
                    val isSelected = selectedCrust.id == crust.id
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .clickable { selectedCrust = crust },
                        color = if (isSelected) RiskGold.copy(alpha = 0.2f) else MaterialTheme.colorScheme.surfaceVariant,
                        border = BorderStroke(1.dp, if (isSelected) RiskGold else MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(10.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = crust.name, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal, fontSize = 13.sp)
                            Text(
                                text = if (crust.price == 0.0) "مجاناً" else "+${crust.price.toInt()} ج.م",
                                fontWeight = FontWeight.Bold,
                                color = RiskGold,
                                fontSize = 12.sp
                            )
                        }
                    }
                }
            }
        }

        // Step 4: Cheeses & Extras
        item {
            Text(
                text = "4. الأجبان والإضافات الإضافية:",
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                cheeses.forEach { cheese ->
                    val isSelected = selectedCheeses.contains(cheese)
                    FilterChip(
                        selected = isSelected,
                        onClick = {
                            if (isSelected) selectedCheeses.remove(cheese) else selectedCheeses.add(cheese)
                        },
                        label = {
                            Text("${cheese.nameAr} (+${cheese.price.toInt()} ج)", fontSize = 12.sp)
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = RiskGold.copy(alpha = 0.3f),
                            selectedLabelColor = RiskGold
                        )
                    )
                }
            }
        }

        // Step 5: Sauces
        item {
            Text(
                text = "5. الصوصات اللذيذة:",
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                sauces.forEach { sauce ->
                    val isSelected = selectedSauces.contains(sauce)
                    FilterChip(
                        selected = isSelected,
                        onClick = {
                            if (isSelected) selectedSauces.remove(sauce) else selectedSauces.add(sauce)
                        },
                        label = {
                            Text("${sauce.nameAr} (+${sauce.price.toInt()} ج)", fontSize = 12.sp)
                        }
                    )
                }
            }
        }

        // Custom name & notes
        item {
            OutlinedTextField(
                value = customMealName,
                onValueChange = { customMealName = it },
                label = { Text("اسم وجبتك المميزة") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = RiskGold,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline
                )
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = specialInstructions,
                onValueChange = { specialInstructions = it },
                label = { Text("أي تعليمات خاصة للمطبخ؟") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
        }

        // Add to Cart Button
        item {
            Button(
                onClick = {
                    val desc = buildString {
                        append("قاعدة: ${selectedBase.name} • حشوات: ${selectedFillings.joinToString { it.name }}")
                        if (selectedCrust.id != "c_none") append(" • ${selectedCrust.name}")
                        if (selectedCheeses.isNotEmpty()) append(" • أجبان: ${selectedCheeses.joinToString { it.nameAr }}")
                    }
                    viewModel.addCustomMealToCart(
                        customName = customMealName,
                        category = selectedBase.category,
                        description = desc,
                        price = totalPrice,
                        extras = selectedCheeses.toList(),
                        sauces = selectedSauces.toList(),
                        hasCrust = selectedCrust.id != "c_none",
                        notes = specialInstructions
                    )
                    onNavigateToCart()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .testTag("add_custom_meal_button"),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = RiskPrimaryRed,
                    contentColor = Color.White
                )
            ) {
                Icon(imageVector = Icons.Default.ShoppingCart, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "إضافة وجبتي المبتكرة للسلة (${totalPrice.toInt()} ج.م)",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            }
        }
    }
}
