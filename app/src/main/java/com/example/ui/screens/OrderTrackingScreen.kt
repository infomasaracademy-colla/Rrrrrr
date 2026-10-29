package com.example.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DeliveryDining
import androidx.compose.material.icons.filled.DirectionsBike
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Navigation
import androidx.compose.material.icons.filled.OutdoorGrill
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.ActiveOrder
import com.example.data.DeliveryType
import com.example.data.OrderStatus
import com.example.data.RiskMenuRepository
import com.example.ui.theme.RiskBlack
import com.example.ui.theme.RiskBrightRed
import com.example.ui.theme.RiskDarkRed
import com.example.ui.theme.RiskGold
import com.example.ui.theme.RiskGreen
import com.example.ui.theme.RiskPrimaryRed
import com.example.viewmodel.RiskRestaurantViewModel
import com.example.viewmodel.RiskUiState

@Composable
fun OrderTrackingScreen(
    uiState: RiskUiState,
    viewModel: RiskRestaurantViewModel,
    onNavigateToMenu: () -> Unit
) {
    val context = LocalContext.current
    val activeOrder = uiState.selectedOrderForTracking ?: uiState.activeOrders.firstOrNull()

    if (activeOrder == null) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(
                    imageVector = Icons.Default.ReceiptLong,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f),
                    modifier = Modifier.size(70.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "لا توجد طلبات جارية حالياً",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "عند إرسال طلب جديد من المنيو، ستتمكن من تتبع تحضيره وخريطة حركة الكابتن في قنا هنا لحظة بلحظة",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(20.dp))
                Button(
                    onClick = onNavigateToMenu,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = RiskPrimaryRed,
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("اطلب الآن من المنيو", fontWeight = FontWeight.Bold)
                }
            }
        }
        return
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 100.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Order ID Header Card
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                border = BorderStroke(1.dp, RiskGold)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Brush.horizontalGradient(listOf(RiskDarkRed, RiskBlack)))
                        .padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "تتبع مباشر للطلب: ${activeOrder.orderId}",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Black,
                                    color = Color.White
                                )
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "التوقيت: ${activeOrder.orderTime} • ${activeOrder.schedule.labelAr}",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.White.copy(alpha = 0.8f)
                            )
                        }

                        Surface(
                            color = RiskGold,
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                text = "${activeOrder.finalTotal.toInt()} ج.م",
                                fontWeight = FontWeight.Black,
                                color = Color.Black,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                fontSize = 13.sp
                            )
                        }
                    }
                }
            }
        }

        // Animated Status Progress Tracker
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.4f))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "حالة الطلب الحالية:",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                        Text(
                            text = activeOrder.status.titleAr,
                            fontWeight = FontWeight.Black,
                            color = RiskBrightRed,
                            fontSize = 14.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    LinearProgressIndicator(
                        progress = { activeOrder.status.progress },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                            .clip(RoundedCornerShape(4.dp)),
                        color = RiskBrightRed,
                        trackColor = MaterialTheme.colorScheme.surface
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // Step 1: Received
                    StatusStepRow(
                        title = "1. تم استلام الطلب بالمطعم",
                        desc = "طلبك مسجل في سيستم كاشير ريسك",
                        isActive = activeOrder.status.ordinal >= OrderStatus.RECEIVED.ordinal,
                        isCurrent = activeOrder.status == OrderStatus.RECEIVED
                    )

                    // Step 2: Preparing in Oven
                    StatusStepRow(
                        title = "2. قيد التحضير والتسوية بالفرن",
                        desc = "فرد العجين وإضافة اللحوم والأجبان الطازجة بالفرن الحجري",
                        isActive = activeOrder.status.ordinal >= OrderStatus.PREPARING.ordinal,
                        isCurrent = activeOrder.status == OrderStatus.PREPARING
                    )

                    // Step 3: On The Way
                    StatusStepRow(
                        title = "3. الكابتن في الطريق بدراجته النارية",
                        desc = "خرج من المنشية أمام كافتيريا الزهراء متجهاً لمنطقتك بقنا",
                        isActive = activeOrder.status.ordinal >= OrderStatus.ON_THE_WAY.ordinal,
                        isCurrent = activeOrder.status == OrderStatus.ON_THE_WAY
                    )

                    // Step 4: Delivered
                    StatusStepRow(
                        title = "4. تم التسليم بنجاح",
                        desc = "تم استلام الوجبة ساخنة وطازجة",
                        isActive = activeOrder.status.ordinal >= OrderStatus.DELIVERED.ordinal,
                        isCurrent = activeOrder.status == OrderStatus.DELIVERED
                    )
                }
            }
        }

        // Live Simulated Map Radar of Qena Delivery Route
        if (activeOrder.deliveryType == DeliveryType.DELIVERY) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                    border = BorderStroke(1.5.dp, RiskGold.copy(alpha = 0.7f))
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Navigation,
                                    contentDescription = null,
                                    tint = RiskGold,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "رادار التوصيل المباشر لشوارع قنا",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp
                                )
                            }

                            Surface(
                                color = RiskGreen.copy(alpha = 0.2f),
                                shape = RoundedCornerShape(6.dp)
                            ) {
                                Text(
                                    text = "مباشر • مباشر",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = RiskGreen,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // Custom Map Canvas Visualizer
                        QenaLiveRouteVisualizer(
                            progressPercent = activeOrder.status.progress,
                            zoneName = activeOrder.qenaZone.zoneName
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "نقطة الانطلاق: المنشية (محل RISK)",
                                    fontSize = 11.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Text(
                                    text = "الوجهة: ${activeOrder.qenaZone.zoneName}",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            Column(horizontalAlignment = Alignment.End) {
                                Text(
                                    text = "الوقت المتبقي التقديري:",
                                    fontSize = 11.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                val minsRemaining = when (activeOrder.status) {
                                    OrderStatus.RECEIVED -> activeOrder.qenaZone.estimatedMinutes
                                    OrderStatus.PREPARING -> (activeOrder.qenaZone.estimatedMinutes * 0.6).toInt()
                                    OrderStatus.ON_THE_WAY -> (activeOrder.qenaZone.estimatedMinutes * 0.3).toInt().coerceAtLeast(3)
                                    OrderStatus.DELIVERED -> 0
                                }
                                Text(
                                    text = "$minsRemaining دقيقة",
                                    fontWeight = FontWeight.Black,
                                    color = RiskBrightRed,
                                    fontSize = 14.sp
                                )
                            }
                        }
                    }
                }
            }
        }

        // Delivery Captain Card & Quick Actions
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.5f))
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Surface(
                                color = RiskPrimaryRed.copy(alpha = 0.15f),
                                shape = CircleShape,
                                modifier = Modifier.size(46.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        imageVector = Icons.Default.DirectionsBike,
                                        contentDescription = null,
                                        tint = RiskPrimaryRed,
                                        modifier = Modifier.size(26.dp)
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = activeOrder.captainName,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp
                                )
                                Text(
                                    text = "كابتن دليفري ريسك بقنا • ${activeOrder.captainPhone}",
                                    fontSize = 11.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }

                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            IconButton(
                                onClick = {
                                    val dialIntent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${activeOrder.captainPhone}"))
                                    context.startActivity(dialIntent)
                                }
                            ) {
                                Icon(Icons.Default.Call, contentDescription = "اتصال بالكابتن", tint = RiskPrimaryRed)
                            }
                            IconButton(
                                onClick = {
                                    val url = "https://api.whatsapp.com/send?phone=2${activeOrder.captainPhone}"
                                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                                    context.startActivity(intent)
                                }
                            ) {
                                Icon(Icons.Default.Chat, contentDescription = "مراسلة الكابتن", tint = RiskGreen)
                            }
                        }
                    }
                }
            }
        }

        // Order Summary Details
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.4f))
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                        text = "تفاصيل أصناف الوجبة:",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    activeOrder.items.forEach { item ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "• ${item.menuItem.nameAr} x${item.quantity}",
                                fontSize = 13.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "${item.totalPrice.toInt()} ج.م",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = RiskBrightRed
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "طريقة الدفع:", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Text(text = activeOrder.paymentMethod.titleAr, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }

                    if (activeOrder.paymentApprovalStatus != com.example.data.PaymentApprovalStatus.NOT_REQUIRED) {
                        Spacer(modifier = Modifier.height(6.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = "حالة مراجعة الإيصال:", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Surface(
                                shape = RoundedCornerShape(4.dp),
                                color = when (activeOrder.paymentApprovalStatus) {
                                    com.example.data.PaymentApprovalStatus.APPROVED -> Color(0xFF10B981).copy(alpha = 0.2f)
                                    com.example.data.PaymentApprovalStatus.REJECTED -> RiskBrightRed.copy(alpha = 0.2f)
                                    else -> RiskGold.copy(alpha = 0.2f)
                                }
                            ) {
                                Text(
                                    text = activeOrder.paymentApprovalStatus.labelAr,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = when (activeOrder.paymentApprovalStatus) {
                                        com.example.data.PaymentApprovalStatus.APPROVED -> Color(0xFF34D399)
                                        com.example.data.PaymentApprovalStatus.REJECTED -> RiskBrightRed
                                        else -> RiskGold
                                    },
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }
                    }
                }
            }
        }

        // Live Simulation Controls for User
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.dp, RiskGold.copy(alpha = 0.4f))
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                        text = "🎮 تجربة محاكاة التتبع اللحظي:",
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        color = RiskGold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Button(
                            onClick = {
                                viewModel.advanceOrderStatus(activeOrder.orderId, OrderStatus.PREPARING)
                            },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = RiskPrimaryRed)
                        ) {
                            Text("في الفرن", fontSize = 11.sp)
                        }

                        Button(
                            onClick = {
                                viewModel.advanceOrderStatus(activeOrder.orderId, OrderStatus.ON_THE_WAY)
                            },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE65100))
                        ) {
                            Text("مع الكابتن", fontSize = 11.sp)
                        }

                        Button(
                            onClick = {
                                viewModel.advanceOrderStatus(activeOrder.orderId, OrderStatus.DELIVERED)
                            },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = RiskGreen)
                        ) {
                            Text("تم التسليم", fontSize = 11.sp)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun StatusStepRow(
    title: String,
    desc: String,
    isActive: Boolean,
    isCurrent: Boolean
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        verticalAlignment = Alignment.Top
    ) {
        Surface(
            color = if (isActive) RiskPrimaryRed else MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
            shape = CircleShape,
            modifier = Modifier.size(24.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                if (isActive) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.width(10.dp))

        Column {
            Text(
                text = title,
                fontWeight = if (isCurrent) FontWeight.Black else FontWeight.Bold,
                fontSize = 13.sp,
                color = if (isActive) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
            )
            Text(
                text = desc,
                fontSize = 11.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun QenaLiveRouteVisualizer(
    progressPercent: Float,
    zoneName: String
) {
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 0.9f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulseAlpha"
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(130.dp)
            .background(Color(0xFF0F172A), RoundedCornerShape(12.dp))
            .clip(RoundedCornerShape(12.dp))
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val startPoint = Offset(size.width * 0.15f, size.height * 0.7f)
            val endPoint = Offset(size.width * 0.85f, size.height * 0.3f)
            val midPoint1 = Offset(size.width * 0.45f, size.height * 0.85f)
            val midPoint2 = Offset(size.width * 0.65f, size.height * 0.45f)

            // Draw route grid lines simulating Qena streets
            for (i in 1..4) {
                drawLine(
                    color = Color.White.copy(alpha = 0.05f),
                    start = Offset(0f, size.height * (i / 5f)),
                    end = Offset(size.width, size.height * (i / 5f)),
                    strokeWidth = 1f
                )
                drawLine(
                    color = Color.White.copy(alpha = 0.05f),
                    start = Offset(size.width * (i / 5f), 0f),
                    end = Offset(size.width * (i / 5f), size.height),
                    strokeWidth = 1f
                )
            }

            // Draw Road
            drawLine(
                color = Color.White.copy(alpha = 0.2f),
                start = startPoint,
                end = endPoint,
                strokeWidth = 6f
            )

            // Current captain location along the path
            val currentCaptainX = startPoint.x + (endPoint.x - startPoint.x) * progressPercent
            val currentCaptainY = startPoint.y + (endPoint.y - startPoint.y) * progressPercent
            val captainPos = Offset(currentCaptainX, currentCaptainY)

            // Traveled route line
            drawLine(
                color = Color(0xFFFF3D00),
                start = startPoint,
                end = captainPos,
                strokeWidth = 6f
            )

            // Restaurant marker (Start)
            drawCircle(color = Color(0xFFB71C1C), radius = 10f, center = startPoint)
            drawCircle(color = Color.White, radius = 5f, center = startPoint)

            // Destination marker (End)
            drawCircle(color = Color(0xFF2E7D32), radius = 10f, center = endPoint)
            drawCircle(color = Color.White, radius = 5f, center = endPoint)

            // Captain marker with pulse ring
            drawCircle(
                color = Color(0xFFFFB300).copy(alpha = pulseAlpha),
                radius = 20f,
                center = captainPos,
                style = Stroke(width = 3f)
            )
            drawCircle(color = Color(0xFFFFB300), radius = 12f, center = captainPos)
            drawCircle(color = Color.Black, radius = 4f, center = captainPos)
        }

        // Overlay text labels on top of canvas
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom
        ) {
            Surface(
                color = Color.Black.copy(alpha = 0.7f),
                shape = RoundedCornerShape(4.dp)
            ) {
                Text(
                    text = "محل ريسك (المنشية)",
                    color = Color.White,
                    fontSize = 10.sp,
                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                )
            }

            Surface(
                color = Color.Black.copy(alpha = 0.7f),
                shape = RoundedCornerShape(4.dp)
            ) {
                Text(
                    text = zoneName,
                    color = RiskGold,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                )
            }
        }
    }
}
