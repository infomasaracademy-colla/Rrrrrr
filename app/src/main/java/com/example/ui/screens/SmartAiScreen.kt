package com.example.ui.screens

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Sensors
import androidx.compose.material.icons.filled.SmartToy
import androidx.compose.material.icons.filled.ViewInAr
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.ItemSize
import com.example.data.RiskMenuRepository
import com.example.ui.theme.RiskBlack
import com.example.ui.theme.RiskBrightRed
import com.example.ui.theme.RiskDarkRed
import com.example.ui.theme.RiskGold
import com.example.ui.theme.RiskGreen
import com.example.ui.theme.RiskPrimaryRed
import com.example.viewmodel.RiskRestaurantViewModel

@Composable
fun SmartAiScreen(
    viewModel: RiskRestaurantViewModel,
    onNavigateToCart: () -> Unit
) {
    var scannedFoodState by remember { mutableStateOf(false) }
    var selected3DModel by remember { mutableStateOf("بيتزا ريسك الإيطالية (أطراف محشوة)") }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 100.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Hero AI Header
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                border = BorderStroke(1.5.dp, RiskGold)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Brush.horizontalGradient(listOf(RiskDarkRed, Color(0xFF100202), RiskBlack)))
                        .padding(20.dp)
                ) {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Psychology,
                                contentDescription = null,
                                tint = RiskGold,
                                modifier = Modifier.size(28.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "محرك ريسك التنبؤي الذكي",
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontWeight = FontWeight.Black,
                                    color = Color.White
                                )
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "نظام ذكاء اصطناعي فائق يتنبأ بوجبتك المفضلة ويراقب جودة الطهي بالفرن ويوفر معاينة تفاعلية حية",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.White.copy(alpha = 0.85f)
                        )
                    }
                }
            }
        }

        // 1. Anticipatory Behavior Prediction Card
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                border = BorderStroke(1.dp, RiskGold.copy(alpha = 0.5f))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(imageVector = Icons.Default.Sensors, contentDescription = null, tint = RiskBrightRed)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "التنبؤ الاستباقي لوجبة اليوم:",
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                        }
                        Surface(
                            color = RiskGreen.copy(alpha = 0.2f),
                            shape = RoundedCornerShape(6.dp)
                        ) {
                            Text(
                                text = "دقة 98.4%",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = RiskGreen,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "بناءً على التوقيت الحالي وموقعك بقنا ومواعيد سهرات اليوم، الخوارزمية تقترح تجهيز:",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Surface(
                        color = MaterialTheme.colorScheme.surface,
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(1.dp, RiskGold),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(
                                text = "🔥 فطيرة ميكس فراخ شرقي (وسط) + حشو أطراف جبنة",
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                color = RiskBrightRed
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "مورقة بالسمن البلدي مع إكسترا صوص شيدر وتومية سورية • جاهزة بالفرن خلال دقائق",
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(text = "السعر: 235 ج.م", fontWeight = FontWeight.Black, color = RiskGold)
                                Button(
                                    onClick = {
                                        val feteer = RiskMenuRepository.menuItems.first { it.id == "fo_mix_chicken" }
                                        viewModel.addToCart(
                                            item = feteer,
                                            selectedSize = ItemSize.MEDIUM,
                                            stuffedCrust = true,
                                            selectedExtras = emptyList(),
                                            selectedSauces = emptyList(),
                                            notes = "طلب ذكي مقترح استباقي",
                                            quantity = 1
                                        )
                                        onNavigateToCart()
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = RiskPrimaryRed),
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Text("تأكيد تجهيز الوجبة بالفرن", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }
            }
        }

        // 2. 3D Spatial Holographic Preview Simulation
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.5f))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Default.ViewInAr, contentDescription = null, tint = RiskGold)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "المعاينة التفاعلية الحية (Spatial 3D Food View):",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "معاينة مجسمة لشكل الوجبة، مطاطية الجبن الموتزاريلا، وسماكة العجين قبل طلبها:",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    InteractiveHologramViewer(title = selected3DModel)

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        OutlinedButton(
                            onClick = { selected3DModel = "بيتزا ريسك الخاصة (أطراف محشوة)" },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text("البيتزا", fontSize = 11.sp)
                        }
                        OutlinedButton(
                            onClick = { selected3DModel = "فطيرة الملوك الشرقية (عسل ومكسرات)" },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text("الفطير", fontSize = 11.sp)
                        }
                        OutlinedButton(
                            onClick = { selected3DModel = "كريب ريسك سوبر ديلوكس المقرمش" },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text("الكريب", fontSize = 11.sp)
                        }
                    }
                }
            }
        }

        // 3. Smart Fridge Computer Vision Scanner
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.5f))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Default.CameraAlt, contentDescription = null, tint = Color(0xFF1E88E5))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "فحص المكونات البصري الذكي:",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "وجه الكاميرا لمكوناتك وسيقترح الذكاء الاصطناعي وجبة ريسك المكملة لك لعشاء خارق بسعر مخفض:",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    if (!scannedFoodState) {
                        Button(
                            onClick = { scannedFoodState = true },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E88E5)),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Icon(imageVector = Icons.Default.CameraAlt, contentDescription = null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("بدء الفحص والتحليل البصري الآن", fontWeight = FontWeight.Bold)
                        }
                    } else {
                        Surface(
                            color = MaterialTheme.colorScheme.surface,
                            shape = RoundedCornerShape(10.dp),
                            border = BorderStroke(1.dp, RiskGreen),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text(
                                    text = "✅ تم التحليل البصري بنجاح!",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp,
                                    color = RiskGreen
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "تم رصد: (جبنة بيضاء + طماطم في مطبخك). نوصيك بطلب: \"صاروخ زنجر مقرمش + كريب بطاطس\" لعمل عشاء متكامل بخصم 15%!",
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun InteractiveHologramViewer(title: String) {
    val infiniteTransition = rememberInfiniteTransition(label = "spin")
    val angle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(8000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "spinAngle"
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(140.dp)
            .background(Color(0xFF0A0F1D), RoundedCornerShape(14.dp))
            .clip(RoundedCornerShape(14.dp)),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val center = Offset(size.width / 2, size.height / 2)

            // Holographic rings
            drawCircle(
                color = Color(0xFFD32F2F).copy(alpha = 0.3f),
                radius = 55f,
                center = center,
                style = Stroke(width = 2f)
            )
            drawCircle(
                color = Color(0xFFFFB300).copy(alpha = 0.4f),
                radius = 45f,
                center = center,
                style = Stroke(width = 3f)
            )
            drawCircle(
                color = Color(0xFFFF5252).copy(alpha = 0.8f),
                radius = 35f,
                center = center
            )
            drawCircle(
                color = Color(0xFFFFD54F),
                radius = 20f,
                center = center
            )
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(10.dp)
        ) {
            Surface(
                color = Color.Black.copy(alpha = 0.75f),
                shape = RoundedCornerShape(6.dp)
            ) {
                Text(
                    text = title,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }
            Spacer(modifier = Modifier.height(50.dp))
            Surface(
                color = RiskGold.copy(alpha = 0.9f),
                shape = RoundedCornerShape(4.dp)
            ) {
                Text(
                    text = "360° تفاعلي • مطاطية الجبن 100%",
                    color = Color.Black,
                    fontWeight = FontWeight.Bold,
                    fontSize = 9.sp,
                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                )
            }
        }
    }
}
