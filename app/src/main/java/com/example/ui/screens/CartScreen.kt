package com.example.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.DeleteSweep
import androidx.compose.material.icons.filled.DeliveryDining
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Navigation
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.RemoveShoppingCart
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Store
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.CartItem
import com.example.data.DeliveryType
import com.example.data.OrderSchedule
import com.example.data.PaymentMethod
import com.example.data.QenaZone
import com.example.data.RiskMenuRepository
import com.example.ui.theme.RiskBrightRed
import com.example.ui.theme.RiskGold
import com.example.ui.theme.RiskGreen
import com.example.ui.theme.RiskPrimaryRed
import com.example.viewmodel.RiskRestaurantViewModel
import com.example.viewmodel.RiskUiState

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CartScreen(
    uiState: RiskUiState,
    viewModel: RiskRestaurantViewModel,
    onNavigateToMenu: () -> Unit,
    onNavigateToTracking: () -> Unit
) {
    val context = LocalContext.current
    val checkoutPhotoLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if (uri != null) {
            viewModel.setCheckoutPaymentProof(uri.toString(), uiState.checkoutPaymentReference)
        }
    }

    if (uiState.cartItems.isEmpty()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = Icons.Default.RemoveShoppingCart,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f),
                    modifier = Modifier.size(80.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "سلة الطلبات فارغة",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "تصفح قائمة طعام ريسك أو ابتكر وجبتك الخاصة من مهندس الوجبات",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(24.dp))
                Button(
                    onClick = onNavigateToMenu,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = RiskPrimaryRed,
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("ابدأ بتصفح المنيو", fontWeight = FontWeight.Bold)
                }
            }
        }
        return
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 100.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Header with clear button
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "سلة الطلب (${uiState.cartItemsCount} صنف)",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                )

                OutlinedButton(
                    onClick = { viewModel.clearCart() },
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = MaterialTheme.colorScheme.error
                    ),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.error.copy(alpha = 0.5f))
                ) {
                    Icon(
                        imageVector = Icons.Default.DeleteSweep,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("إفراغ السلة", fontSize = 12.sp)
                }
            }
        }

        // Cart items list
        items(uiState.cartItems, key = { it.cartId }) { item ->
            CartItemRow(
                cartItem = item,
                onIncrease = { viewModel.updateCartItemQuantity(item.cartId, 1) },
                onDecrease = { viewModel.updateCartItemQuantity(item.cartId, -1) },
                onDelete = { viewModel.removeFromCart(item.cartId) }
            )
        }

        // 1. Delivery Type (Delivery vs Pick-up)
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.5f))
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                        text = "طريقة الاستلام:",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        DeliveryType.entries.forEach { type ->
                            val isSelected = uiState.deliveryType == type
                            Surface(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(10.dp))
                                    .clickable { viewModel.setDeliveryType(type) },
                                color = if (isSelected) RiskPrimaryRed else MaterialTheme.colorScheme.surface,
                                border = BorderStroke(
                                    1.dp,
                                    if (isSelected) RiskGold else MaterialTheme.colorScheme.outline
                                ),
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Row(
                                    modifier = Modifier.padding(vertical = 10.dp, horizontal = 6.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Center
                                ) {
                                    Icon(
                                        imageVector = if (type == DeliveryType.DELIVERY) Icons.Default.DeliveryDining else Icons.Default.Store,
                                        contentDescription = null,
                                        tint = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface,
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = if (type == DeliveryType.DELIVERY) "دليفري بقنا" else "اطلب واستلم ساخنة",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 12.sp,
                                        color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // 2. Qena Hyper-Local Delivery Zones (when Delivery selected)
        if (uiState.deliveryType == DeliveryType.DELIVERY) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                    border = BorderStroke(1.dp, RiskGold.copy(alpha = 0.5f))
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Map, contentDescription = null, tint = RiskGold, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "اختر منطقتك بقنا (حساب تكلفة ومسافة ذكية):",
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp
                            )
                        }
                        Spacer(modifier = Modifier.height(10.dp))

                        FlowRow(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            QenaZone.entries.forEach { zone ->
                                val isSelected = uiState.selectedZone == zone
                                FilterChip(
                                    selected = isSelected,
                                    onClick = { viewModel.setQenaZone(zone) },
                                    label = {
                                        Text(
                                            text = "${zone.zoneName} (${zone.deliveryFee.toInt()} ج)",
                                            fontSize = 11.sp
                                        )
                                    },
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = RiskPrimaryRed,
                                        selectedLabelColor = Color.White
                                    )
                                )
                            }
                        }
                    }
                }
            }
        }

        // 3. Scheduling & Pre-ordering
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.5f))
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.AccessTime, contentDescription = null, tint = RiskGold, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "موعد تجهيز واستلام الطلب:",
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                    }
                    Spacer(modifier = Modifier.height(10.dp))

                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        OrderSchedule.entries.forEach { sched ->
                            val isSelected = uiState.selectedSchedule == sched
                            Surface(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(8.dp))
                                    .clickable { viewModel.setSchedule(sched) },
                                color = if (isSelected) RiskPrimaryRed.copy(alpha = 0.15f) else MaterialTheme.colorScheme.surface,
                                border = BorderStroke(1.dp, if (isSelected) RiskPrimaryRed else MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = if (isSelected) Icons.Default.CheckCircle else Icons.Default.AccessTime,
                                        contentDescription = null,
                                        tint = if (isSelected) RiskPrimaryRed else MaterialTheme.colorScheme.onSurfaceVariant,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = sched.labelAr,
                                        fontSize = 12.sp,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // 4. Payment Method Selection
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.5f))
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Payment, contentDescription = null, tint = RiskGold, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "اختر طريقة الدفع:",
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                    }
                    Spacer(modifier = Modifier.height(10.dp))

                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        PaymentMethod.entries.forEach { pm ->
                            val isSelected = uiState.selectedPaymentMethod == pm
                            Surface(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(10.dp))
                                    .clickable { viewModel.setPaymentMethod(pm) },
                                color = if (isSelected) RiskPrimaryRed.copy(alpha = 0.15f) else MaterialTheme.colorScheme.surface,
                                border = BorderStroke(1.dp, if (isSelected) RiskBrightRed else MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)),
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Row(
                                    modifier = Modifier.padding(10.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(text = pm.titleAr, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                        Text(text = pm.subtitleAr, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                    }
                                    if (pm == PaymentMethod.RISK_WALLET) {
                                        Surface(
                                            color = RiskGold,
                                            shape = RoundedCornerShape(4.dp)
                                        ) {
                                            Text(
                                                text = "${uiState.walletBalance.toInt()} ج رصيد",
                                                fontSize = 10.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = Color.Black,
                                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }

                    // Embedded Payment Gateway Verification Interface for Vodafone Cash & InstaPay
                    if (uiState.selectedPaymentMethod == PaymentMethod.VODAFONE_CASH || uiState.selectedPaymentMethod == PaymentMethod.INSTAPAY) {
                        Spacer(modifier = Modifier.height(12.dp))
                        Card(
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                            border = BorderStroke(1.5.dp, RiskGold),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.Security, contentDescription = null, tint = RiskGold, modifier = Modifier.size(18.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "بوابة التحويل الرقمي وإرفاق الإيصال",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 13.sp,
                                        color = RiskGold
                                    )
                                }
                                Spacer(modifier = Modifier.height(8.dp))

                                val targetAccount = if (uiState.selectedPaymentMethod == PaymentMethod.VODAFONE_CASH) "01019043229" else "risk.foods@instapay"
                                val targetLabel = if (uiState.selectedPaymentMethod == PaymentMethod.VODAFONE_CASH) "محفظة فودافون كاش" else "معرف إنستاباي"

                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = MaterialTheme.colorScheme.surfaceVariant,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Row(
                                        modifier = Modifier.padding(10.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Column {
                                            Text("حول مبلغ: ${uiState.finalTotal.toInt()} ج.م إلى $targetLabel:", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                            Text(targetAccount, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = RiskGold)
                                        }
                                        OutlinedButton(
                                            onClick = {
                                                val clipboard = context.getSystemService(android.content.Context.CLIPBOARD_SERVICE) as ClipboardManager
                                                clipboard.setPrimaryClip(ClipData.newPlainText(targetLabel, targetAccount))
                                                Toast.makeText(context, "تم نسخ $targetLabel: $targetAccount", Toast.LENGTH_SHORT).show()
                                            },
                                            shape = RoundedCornerShape(6.dp)
                                        ) {
                                            Icon(Icons.Default.ContentCopy, contentDescription = null, modifier = Modifier.size(14.dp))
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Text("نسخ", fontSize = 11.sp)
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.height(10.dp))

                                OutlinedTextField(
                                    value = uiState.checkoutPaymentReference,
                                    onValueChange = { viewModel.setCheckoutPaymentProof(uiState.checkoutReceiptUri, it) },
                                    label = { Text("رقم العملية أو المرجع (Reference No.)") },
                                    placeholder = { Text("مثال: TXN-839201") },
                                    singleLine = true,
                                    modifier = Modifier.fillMaxWidth()
                                )

                                Spacer(modifier = Modifier.height(10.dp))

                                // Screenshot Picker Trigger
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = if (uiState.checkoutReceiptUri != null) Color(0xFF1B3824) else MaterialTheme.colorScheme.surfaceVariant,
                                    border = BorderStroke(1.dp, if (uiState.checkoutReceiptUri != null) Color(0xFF10B981) else MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable {
                                            checkoutPhotoLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                                        }
                                ) {
                                    Row(
                                        modifier = Modifier.padding(12.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.Center
                                    ) {
                                        Icon(
                                            Icons.Default.AddPhotoAlternate,
                                            contentDescription = null,
                                            tint = if (uiState.checkoutReceiptUri != null) Color(0xFF10B981) else RiskGold,
                                            modifier = Modifier.size(20.dp)
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(
                                            text = if (uiState.checkoutReceiptUri != null) "تم إرفاق صورة الإيصال بنجاح ✓ (اضغط للتغيير)" else "اضغط لرفع سكرين شوت إيصال التحويل من المعرض",
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 12.sp,
                                            color = if (uiState.checkoutReceiptUri != null) Color(0xFF34D399) else MaterialTheme.colorScheme.onSurface
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(6.dp))

                                // Quick test sample buttons
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    OutlinedButton(
                                        onClick = {
                                            viewModel.setCheckoutPaymentProof("sample://genuine_order_receipt", "VF-${(100000..999999).random()}")
                                            Toast.makeText(context, "تم تجهيز إيصال سليم تجريبي للطلب", Toast.LENGTH_SHORT).show()
                                        },
                                        shape = RoundedCornerShape(6.dp),
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Text("إيصال تجريبي سليم", fontSize = 10.sp, color = Color(0xFF10B981))
                                    }

                                    OutlinedButton(
                                        onClick = {
                                            viewModel.setCheckoutPaymentProof("sample://suspicious_order_receipt", "FAKE-000000")
                                            Toast.makeText(context, "تم تجهيز إيصال مشبوه للاختبار", Toast.LENGTH_SHORT).show()
                                        },
                                        shape = RoundedCornerShape(6.dp),
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Text("إيصال مشبوه للاختبار", fontSize = 10.sp, color = RiskBrightRed)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // 5. Customer Details Input
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.5f))
            ) {
                Column(
                    modifier = Modifier.padding(14.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        text = "بيانات العميل للتسليم:",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )

                    OutlinedTextField(
                        value = uiState.customerName,
                        onValueChange = {
                            viewModel.updateCustomerDetails(it, uiState.customerPhone, uiState.customerAddress, uiState.orderNotes)
                        },
                        label = { Text("الاسم") },
                        leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )

                    OutlinedTextField(
                        value = uiState.customerPhone,
                        onValueChange = {
                            viewModel.updateCustomerDetails(uiState.customerName, it, uiState.customerAddress, uiState.orderNotes)
                        },
                        label = { Text("رقم الموبايل للتواصل") },
                        leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null) },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )

                    if (uiState.deliveryType == DeliveryType.DELIVERY) {
                        OutlinedTextField(
                            value = uiState.customerAddress,
                            onValueChange = {
                                viewModel.updateCustomerDetails(uiState.customerName, uiState.customerPhone, it, uiState.orderNotes)
                            },
                            label = { Text("العنوان بالتفصيل في قنا (الشارع، العمارة، رقم الشقة)") },
                            leadingIcon = { Icon(Icons.Default.LocationOn, contentDescription = null) },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )
                    }

                    OutlinedTextField(
                        value = uiState.orderNotes,
                        onValueChange = {
                            viewModel.updateCustomerDetails(uiState.customerName, uiState.customerPhone, uiState.customerAddress, it)
                        },
                        label = { Text("ملاحظات إضافية على الطلب") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                }
            }
        }

        // 6. Bill Summary
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.5f))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = "تفاصيل الحساب النهائي:", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "مجموع الأصناف:", color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Text(text = "${uiState.subtotal.toInt()} ج.م", fontWeight = FontWeight.SemiBold)
                    }

                    if (uiState.deliveryType == DeliveryType.DELIVERY) {
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = "خدمة التوصيل (${uiState.selectedZone.zoneName}):", color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Text(text = "${uiState.deliveryFee.toInt()} ج.م", fontWeight = FontWeight.SemiBold)
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f))
                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "الإجمالي المطلوب:", fontWeight = FontWeight.Black, fontSize = 16.sp)
                        Text(
                            text = "${uiState.finalTotal.toInt()} ج.م",
                            fontWeight = FontWeight.Black,
                            fontSize = 18.sp,
                            color = RiskBrightRed
                        )
                    }
                }
            }
        }

        // 7. Order Confirmation & WhatsApp Actions
        item {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // PRIMARY BUTTON: Confirm Order & Launch Live Tracking
                Button(
                    onClick = {
                        val newOrder = viewModel.submitOrder()
                        if (newOrder != null) {
                            Toast.makeText(context, "تم تأكيد طلبك ${newOrder.orderId} بنجاح!", Toast.LENGTH_LONG).show()
                            onNavigateToTracking()
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .testTag("confirm_order_and_track_button"),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = RiskPrimaryRed,
                        contentColor = Color.White
                    )
                ) {
                    Icon(imageVector = Icons.Default.Navigation, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "تأكيد الطلب وبدء التتبع اللحظي للكابتن",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                }

                // SECONDARY BUTTON: Send WhatsApp copy directly
                OutlinedButton(
                    onClick = {
                        val whatsappUrl = viewModel.getEncodedWhatsAppUrl(RiskMenuRepository.restaurant.phone1)
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(whatsappUrl))
                        try {
                            context.startActivity(intent)
                        } catch (e: Exception) {
                            Toast.makeText(context, "لم يتم العثور على تطبيق واتساب", Toast.LENGTH_SHORT).show()
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.5.dp, RiskGreen),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = RiskGreen)
                ) {
                    Icon(imageVector = Icons.Default.Chat, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("إرسال نسخة الطلب عبر واتساب (01019043229)", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                }
            }
        }
    }
}

@Composable
fun CartItemRow(
    cartItem: CartItem,
    onIncrease: () -> Unit,
    onDecrease: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.5f))
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = cartItem.menuItem.nameAr,
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        if (cartItem.selectedSize != null) {
                            Spacer(modifier = Modifier.width(6.dp))
                            Surface(
                                color = RiskPrimaryRed.copy(alpha = 0.15f),
                                shape = RoundedCornerShape(4.dp)
                            ) {
                                Text(
                                    text = cartItem.selectedSize.titleAr,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = RiskBrightRed,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }
                    }

                    if (cartItem.hasStuffedCrust) {
                        Text(
                            text = "+ حشو الأطراف جبنة (25 ج)",
                            fontSize = 11.sp,
                            color = RiskGold,
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    if (cartItem.selectedExtras.isNotEmpty()) {
                        Text(
                            text = "إضافات: " + cartItem.selectedExtras.joinToString { it.nameAr },
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    if (cartItem.selectedSauces.isNotEmpty()) {
                        Text(
                            text = "صوصات: " + cartItem.selectedSauces.joinToString { it.nameAr },
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    if (cartItem.notes.isNotBlank()) {
                        Text(
                            text = "ملاحظة: ${cartItem.notes}",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                IconButton(
                    onClick = onDelete,
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "حذف من السلة",
                        tint = MaterialTheme.colorScheme.error.copy(alpha = 0.8f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(8.dp))
                        .padding(horizontal = 4.dp, vertical = 2.dp)
                ) {
                    IconButton(
                        onClick = onDecrease,
                        modifier = Modifier.size(28.dp)
                    ) {
                        Icon(imageVector = Icons.Default.Remove, contentDescription = "تقليل", modifier = Modifier.size(16.dp))
                    }
                    Text(
                        text = "${cartItem.quantity}",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        modifier = Modifier.padding(horizontal = 8.dp)
                    )
                    IconButton(
                        onClick = onIncrease,
                        modifier = Modifier.size(28.dp)
                    ) {
                        Icon(imageVector = Icons.Default.Add, contentDescription = "زيادة", modifier = Modifier.size(16.dp))
                    }
                }

                Text(
                    text = "${cartItem.totalPrice.toInt()} ج.م",
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 15.sp,
                    color = RiskBrightRed
                )
            }
        }
    }
}
