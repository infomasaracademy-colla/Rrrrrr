package com.example.ui.screens

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DeliveryDining
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.RestaurantMenu
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.AiVerificationStatus
import com.example.data.ItemSize
import com.example.data.MenuCategory
import com.example.data.MenuItem
import com.example.data.OrderStatus
import com.example.data.RiskMenuRepository
import com.example.data.UserRole
import com.example.ui.theme.RiskBrightRed
import com.example.ui.theme.RiskGold
import com.example.viewmodel.RiskRestaurantViewModel
import com.example.viewmodel.RiskUiState

enum class AdminTab(val titleAr: String) {
    PRICES("تعديل الأسعار"),
    PAYMENTS("مراجعة الدفع والـ AI"),
    ORDERS("الطلبات الحية"),
    ROLES("الإشراف ونقل الملكية")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminDashboardScreen(
    uiState: RiskUiState,
    viewModel: RiskRestaurantViewModel,
    onNavigateBack: () -> Unit
) {
    val context = LocalContext.current
    var currentAdminTab by remember { mutableStateOf(AdminTab.PRICES) }

    val user = uiState.currentUser
    val config = uiState.restaurantConfig
    val isOwner = user.role == UserRole.OWNER || config.ownerUserId == user.uid
    val isSupervisor = user.role == UserRole.SUPERVISOR || config.supervisorUids.contains(user.uid)
    val isAuthorized = isOwner || isSupervisor

    // First time claim state
    val needsFirstOwner = config.ownerUserId == null

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.AdminPanelSettings, contentDescription = null, tint = RiskGold)
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(
                                "لوحة التحكم السرية لمطعم ريسك",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Text(
                                text = when {
                                    isOwner -> "صلاحية كاملة: مالك المطعم الأوحد (Owner) 👑"
                                    isSupervisor -> "صلاحية: مشرف مطعم ريسك 🛡️"
                                    needsFirstOwner -> "تنبيه: لم يتم تسجيل مالك حتى الآن!"
                                    else -> "طلب صلاحية تفويض"
                                },
                                fontSize = 11.sp,
                                color = RiskGold
                            )
                        }
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "رجوع",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF14141C))
            )
        },
        containerColor = Color(0xFF0D0D12)
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            if (needsFirstOwner) {
                // FIRST PERSON OPENS: CLAIM OWNERSHIP
                FirstOwnerClaimView(viewModel = viewModel)
            } else if (!isAuthorized) {
                // SECONDARY PERSON OPENS: ENTER SUPERVISOR CODE
                SupervisorCodeGateView(
                    config = config,
                    user = user,
                    viewModel = viewModel
                )
            } else {
                // AUTHORIZED ADMIN PANEL
                Column(modifier = Modifier.fillMaxSize()) {
                    // Admin Tabs
                    TabRow(
                        selectedTabIndex = currentAdminTab.ordinal,
                        containerColor = Color(0xFF181822),
                        contentColor = RiskGold,
                        indicator = { tabPositions ->
                            TabRowDefaults.SecondaryIndicator(
                                Modifier.tabIndicatorOffset(tabPositions[currentAdminTab.ordinal]),
                                color = RiskGold
                            )
                        }
                    ) {
                        AdminTab.values().forEach { tab ->
                            Tab(
                                selected = currentAdminTab == tab,
                                onClick = { currentAdminTab = tab },
                                text = {
                                    Text(
                                        text = tab.titleAr,
                                        fontWeight = if (currentAdminTab == tab) FontWeight.Bold else FontWeight.Normal,
                                        fontSize = 12.sp,
                                        color = if (currentAdminTab == tab) RiskGold else Color.LightGray
                                    )
                                }
                            )
                        }
                    }

                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(14.dp)
                    ) {
                        when (currentAdminTab) {
                            AdminTab.PRICES -> {
                                MenuPriceEditorView(uiState = uiState, viewModel = viewModel)
                            }
                            AdminTab.PAYMENTS -> {
                                PaymentModerationView(uiState = uiState, viewModel = viewModel)
                            }
                            AdminTab.ORDERS -> {
                                LiveOrdersAdminView(uiState = uiState, viewModel = viewModel)
                            }
                            AdminTab.ROLES -> {
                                OwnershipAndRolesView(
                                    uiState = uiState,
                                    viewModel = viewModel,
                                    isOwner = isOwner
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

// --- 1. FIRST OWNER REGISTRATION VIEW ---
@Composable
fun FirstOwnerClaimView(viewModel: RiskRestaurantViewModel) {
    var ownerNameInput by remember { mutableStateOf("صاحب مطعم ريسك") }
    var ownerEmailInput by remember { mutableStateOf("owner@riskfoods.com") }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(18.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Surface(
                shape = CircleShape,
                color = RiskGold.copy(alpha = 0.2f),
                modifier = Modifier.size(80.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(Icons.Default.AdminPanelSettings, contentDescription = null, tint = RiskGold, modifier = Modifier.size(48.dp))
                }
            }
        }
        item {
            Text(
                "أنت أول شخص يفتح لوحة التحكم! 👑",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                "سجل نفسك كالمالك الأوحد للمطعم (Owner). ستتمكن من تغيير كل أسعار المنيو، التحكم في الطلبات، مراجعة إيصالات التحويل، وتعيين المشرفين أو نقل الملكية.",
                fontSize = 13.sp,
                color = Color.LightGray,
                lineHeight = 18.sp
            )
        }
        item {
            OutlinedTextField(
                value = ownerNameInput,
                onValueChange = { ownerNameInput = it },
                label = { Text("اسم المالك الرسمي") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(10.dp))
            OutlinedTextField(
                value = ownerEmailInput,
                onValueChange = { ownerEmailInput = it },
                label = { Text("البريد الإلكتروني للمالك") },
                modifier = Modifier.fillMaxWidth()
            )
        }
        item {
            Button(
                onClick = {
                    viewModel.claimOwnership(ownerNameInput, ownerEmailInput)
                },
                colors = ButtonDefaults.buttonColors(containerColor = RiskBrightRed),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text("تثبيت ملكيتي الرسمية لمطعم ريسك الآن", fontSize = 15.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

// --- 2. SUPERVISOR ACCESS CODE GATE VIEW ---
@Composable
fun SupervisorCodeGateView(
    config: com.example.data.RestaurantConfig,
    user: com.example.data.AppUser,
    viewModel: RiskRestaurantViewModel
) {
    var codeInput by remember { mutableStateOf("") }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(18.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Surface(
                shape = CircleShape,
                color = Color(0xFF3B82F6).copy(alpha = 0.2f),
                modifier = Modifier.size(80.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(Icons.Default.Lock, contentDescription = null, tint = Color(0xFF60A5FA), modifier = Modifier.size(44.dp))
                }
            }
        }
        item {
            Text(
                "لوحة تحكم ريسك مغلقة بكود الإشراف 🛡️",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                "المالك الرسمي مسجل بالفعل (${config.ownerName ?: "مالك ريسك"}). لتفعيل صلاحيات المشرف، يرجى إدخال رمز الإشراف المعتمد من المالك:",
                fontSize = 13.sp,
                color = Color.LightGray,
                lineHeight = 18.sp
            )
        }
        item {
            OutlinedTextField(
                value = codeInput,
                onValueChange = { codeInput = it },
                label = { Text("رمز تفويض الإشراف (Supervisor Code)") },
                placeholder = { Text("مثال: RISK2025") },
                modifier = Modifier.fillMaxWidth()
            )
        }
        item {
            Button(
                onClick = {
                    viewModel.authorizeAsSupervisor(codeInput)
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3B82F6)),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text("تأكيد الرمز وتفعيل صلاحية الإشراف", fontSize = 14.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

// --- 3. MENU PRICE & AVAILABILITY EDITOR ---
@Composable
fun MenuPriceEditorView(
    uiState: RiskUiState,
    viewModel: RiskRestaurantViewModel
) {
    var selectedCategory by remember { mutableStateOf(MenuCategory.PIZZA) }
    var itemToEditPrice by remember { mutableStateOf<Pair<MenuItem, ItemSize?>?>(null) }
    var newPriceText by remember { mutableStateOf("") }

    val effectiveItems = viewModel.getEffectiveMenuItems().filter {
        selectedCategory == MenuCategory.ALL || it.category == selectedCategory
    }

    Column(modifier = Modifier.fillMaxSize()) {
        // Category Selector Chips
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(MenuCategory.values()) { cat ->
                FilterChip(
                    selected = selectedCategory == cat,
                    onClick = { selectedCategory = cat },
                    label = { Text(cat.titleAr, fontSize = 11.sp) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = RiskBrightRed,
                        selectedLabelColor = Color.White
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(effectiveItems, key = { it.id }) { item ->
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF1B1B26)),
                    border = BorderStroke(1.dp, if (item.isAvailable) Color(0xFF2C2C3E) else RiskBrightRed.copy(alpha = 0.5f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(item.nameAr, fontWeight = FontWeight.Bold, color = Color.White, fontSize = 14.sp)
                                Text(item.category.titleAr, fontSize = 11.sp, color = Color.Gray)
                            }

                            // Availability Toggle Switch
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = if (item.isAvailable) "متوفر" else "نفذ",
                                    fontSize = 11.sp,
                                    color = if (item.isAvailable) Color(0xFF10B981) else RiskBrightRed,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Switch(
                                    checked = item.isAvailable,
                                    onCheckedChange = { viewModel.toggleItemAvailability(item.id, it) },
                                    colors = SwitchDefaults.colors(
                                        checkedThumbColor = Color(0xFF10B981),
                                        checkedTrackColor = Color(0xFF10B981).copy(alpha = 0.4f)
                                    )
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))
                        Divider(color = Color(0xFF2C2C3E))
                        Spacer(modifier = Modifier.height(8.dp))

                        // Prices Row (Single or S / M / L)
                        if (item.sizePrices.isNotEmpty()) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                item.sizePrices.forEach { (size, price) ->
                                    Surface(
                                        shape = RoundedCornerShape(8.dp),
                                        color = Color(0xFF242434),
                                        modifier = Modifier
                                            .weight(1f)
                                            .clickable {
                                                itemToEditPrice = Pair(item, size)
                                                newPriceText = price.toInt().toString()
                                            }
                                    ) {
                                        Column(
                                            modifier = Modifier.padding(6.dp),
                                            horizontalAlignment = Alignment.CenterHorizontally
                                        ) {
                                            Text(size.titleAr, fontSize = 11.sp, color = Color.LightGray)
                                            Text("${price.toInt()} ج", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = RiskGold)
                                            Text("اضغط لتعديل", fontSize = 9.sp, color = Color(0xFF60A5FA))
                                        }
                                    }
                                }
                            }
                        } else {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text("السعر الموحد: ${item.priceSingle?.toInt() ?: 0} ج.م", color = RiskGold, fontWeight = FontWeight.Bold)
                                Button(
                                    onClick = {
                                        itemToEditPrice = Pair(item, null)
                                        newPriceText = (item.priceSingle?.toInt() ?: 0).toString()
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2A2A3C)),
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Icon(Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(14.dp), tint = RiskGold)
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("تعديل السعر", fontSize = 11.sp, color = RiskGold)
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // Edit Price Modal Dialog
    itemToEditPrice?.let { (item, size) ->
        AlertDialog(
            onDismissRequest = { itemToEditPrice = null },
            title = {
                Text(
                    text = "تعديل سعر: ${item.nameAr}${size?.let { " (${it.titleAr})" } ?: ""}",
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Column {
                    Text("أدخل السعر الجديد بالجنيه (سيتم تحديثه في كل التطبيق وفورستور):", fontSize = 12.sp, color = Color.Gray)
                    Spacer(modifier = Modifier.height(10.dp))
                    OutlinedTextField(
                        value = newPriceText,
                        onValueChange = { newPriceText = it },
                        label = { Text("السعر الجديد (ج.م)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val parsed = newPriceText.toDoubleOrNull() ?: 0.0
                        if (parsed > 0) {
                            viewModel.updateItemPrice(item.id, size, parsed)
                        }
                        itemToEditPrice = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = RiskBrightRed)
                ) {
                    Text("حفظ السعر الجديد")
                }
            },
            dismissButton = {
                TextButton(onClick = { itemToEditPrice = null }) {
                    Text("إلغاء")
                }
            }
        )
    }
}

// --- 4. PAYMENT & AI FRAUD MODERATION VIEW ---
@Composable
fun PaymentModerationView(
    uiState: RiskUiState,
    viewModel: RiskRestaurantViewModel
) {
    var selectedSubTab by remember { mutableStateOf(0) } // 0: Order Payments, 1: Wallet Top-ups
    var selectedOrderToReject by remember { mutableStateOf<com.example.data.ActiveOrder?>(null) }
    var rejectionReasonInput by remember { mutableStateOf("") }
    var previewReceiptUri by remember { mutableStateOf<String?>(null) }

    val electronicOrders = uiState.activeOrders.filter {
        it.paymentApprovalStatus != com.example.data.PaymentApprovalStatus.NOT_REQUIRED
    }
    val walletRequests = uiState.paymentRequests

    Column(modifier = Modifier.fillMaxSize()) {
        // Sub-filter tabs
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            FilterChip(
                selected = selectedSubTab == 0,
                onClick = { selectedSubTab = 0 },
                label = { Text("مدفوعات الطلبات بالسكرين شوت (${electronicOrders.size})", fontSize = 11.sp, fontWeight = FontWeight.Bold) },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = RiskGold.copy(alpha = 0.25f),
                    selectedLabelColor = RiskGold
                ),
                modifier = Modifier.weight(1f)
            )
            FilterChip(
                selected = selectedSubTab == 1,
                onClick = { selectedSubTab = 1 },
                label = { Text("شحن المحفظة وفحص الـ AI (${walletRequests.size})", fontSize = 11.sp, fontWeight = FontWeight.Bold) },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = RiskBrightRed.copy(alpha = 0.25f),
                    selectedLabelColor = RiskBrightRed
                ),
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        if (selectedSubTab == 0) {
            // ORDER SCREENSHOT PAYMENTS
            if (electronicOrders.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(Icons.Default.Payments, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(44.dp))
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("لا توجد طلبات مدفوعة إلكترونياً بانتظار المراجعة", color = Color.Gray, fontSize = 13.sp)
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(electronicOrders, key = { it.orderId }) { order ->
                        val isApproved = order.paymentApprovalStatus == com.example.data.PaymentApprovalStatus.APPROVED
                        val isRejected = order.paymentApprovalStatus == com.example.data.PaymentApprovalStatus.REJECTED

                        Card(
                            shape = RoundedCornerShape(14.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = when {
                                    isApproved -> Color(0xFF14241A)
                                    isRejected -> Color(0xFF281416)
                                    else -> Color(0xFF1F1F2B)
                                }
                            ),
                            border = BorderStroke(
                                1.dp,
                                when {
                                    isApproved -> Color(0xFF10B981)
                                    isRejected -> RiskBrightRed
                                    else -> RiskGold
                                }
                            ),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column {
                                        Text(
                                            text = "طلب رقم: ${order.orderId}",
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 15.sp,
                                            color = RiskGold
                                        )
                                        Text(
                                            text = "${order.customerName} • ${order.customerPhone}",
                                            fontSize = 12.sp,
                                            color = Color.LightGray
                                        )
                                    }
                                    Text(
                                        text = "${order.finalTotal.toInt()} ج.م",
                                        fontWeight = FontWeight.Black,
                                        fontSize = 17.sp,
                                        color = Color.White
                                    )
                                }

                                Spacer(modifier = Modifier.height(8.dp))
                                Divider(color = Color(0xFF333345))
                                Spacer(modifier = Modifier.height(8.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column {
                                        Text(
                                            text = "طريقة الدفع: ${order.paymentMethod.titleAr}",
                                            fontSize = 12.sp,
                                            color = Color.White
                                        )
                                        Text(
                                            text = "رقم المرجع: ${order.paymentReference ?: "غير محدد"}",
                                            fontSize = 11.sp,
                                            color = Color.Gray
                                        )
                                    }

                                    Surface(
                                        shape = RoundedCornerShape(6.dp),
                                        color = when {
                                            isApproved -> Color(0xFF10B981).copy(alpha = 0.2f)
                                            isRejected -> RiskBrightRed.copy(alpha = 0.2f)
                                            else -> RiskGold.copy(alpha = 0.2f)
                                        }
                                    ) {
                                        Text(
                                            text = order.paymentApprovalStatus.labelAr,
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = when {
                                                isApproved -> Color(0xFF34D399)
                                                isRejected -> RiskBrightRed
                                                else -> RiskGold
                                            },
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                        )
                                    }
                                }

                                if (order.receiptImageUri != null) {
                                    Spacer(modifier = Modifier.height(8.dp))
                                    OutlinedButton(
                                        onClick = { previewReceiptUri = order.receiptImageUri },
                                        shape = RoundedCornerShape(8.dp),
                                        border = BorderStroke(1.dp, Color(0xFF3B82F6)),
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Icon(Icons.Default.Visibility, contentDescription = null, modifier = Modifier.size(16.dp), tint = Color(0xFF60A5FA))
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text("معاينة سكرين شوت الإيصال المرفوع", fontSize = 11.sp, color = Color(0xFF60A5FA))
                                    }
                                }

                                if (isRejected && order.paymentRejectionReason != null) {
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text(
                                        text = "سبب الرفض: ${order.paymentRejectionReason}",
                                        fontSize = 11.sp,
                                        color = RiskBrightRed
                                    )
                                }

                                // Action Buttons (Approve / Reject)
                                if (order.paymentApprovalStatus == com.example.data.PaymentApprovalStatus.PENDING_REVIEW) {
                                    Spacer(modifier = Modifier.height(10.dp))
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        Button(
                                            onClick = { viewModel.adminApproveOrderPayment(order.orderId) },
                                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF10B981)),
                                            shape = RoundedCornerShape(8.dp),
                                            modifier = Modifier.weight(1f)
                                        ) {
                                            Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(14.dp))
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Text("اعتماد الدفع ✓", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                        }

                                        OutlinedButton(
                                            onClick = {
                                                selectedOrderToReject = order
                                                rejectionReasonInput = "المبلغ لم يصل لمحفظة ريسك أو الإيصال غير واضح"
                                            },
                                            border = BorderStroke(1.dp, RiskBrightRed),
                                            shape = RoundedCornerShape(8.dp),
                                            modifier = Modifier.weight(1f)
                                        ) {
                                            Icon(Icons.Default.Close, contentDescription = null, tint = RiskBrightRed, modifier = Modifier.size(14.dp))
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Text("رفض الدفع ✗", fontSize = 11.sp, color = RiskBrightRed)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } else {
            // WALLET TOP-UP REQUESTS & AI VERIFICATION
            if (walletRequests.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(Icons.Default.Payments, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(44.dp))
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("لا توجد إيصالات شحن محفظة حالياً للمراجعة", color = Color.Gray, fontSize = 13.sp)
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(walletRequests, key = { it.id }) { req ->
                        val isFraud = req.status == AiVerificationStatus.FLAGGED_FRAUD || req.status == AiVerificationStatus.MANUALLY_REJECTED
                        Card(
                            shape = RoundedCornerShape(14.dp),
                            colors = CardDefaults.cardColors(containerColor = if (isFraud) Color(0xFF281416) else Color(0xFF14241A)),
                            border = BorderStroke(1.dp, if (isFraud) RiskBrightRed else Color(0xFF10B981)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column {
                                        Text(
                                            text = "${req.userName} (${req.userPhone})",
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 14.sp,
                                            color = Color.White
                                        )
                                        Text(
                                            text = "معاملة: ${req.referenceNumber} • ${req.dateStr}",
                                            fontSize = 11.sp,
                                            color = Color.LightGray
                                        )
                                    }
                                    Text(
                                        text = "${req.amount.toInt()} ج.م",
                                        fontWeight = FontWeight.Black,
                                        fontSize = 18.sp,
                                        color = RiskGold
                                    )
                                }

                                Spacer(modifier = Modifier.height(8.dp))
                                Divider(color = Color(0xFF3A3A4A))
                                Spacer(modifier = Modifier.height(8.dp))

                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = if (isFraud) Icons.Default.Warning else Icons.Default.CheckCircle,
                                        contentDescription = null,
                                        tint = if (isFraud) RiskBrightRed else Color(0xFF10B981),
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = req.status.labelAr,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 12.sp,
                                        color = if (isFraud) RiskBrightRed else Color(0xFF34D399)
                                    )
                                }

                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = req.aiExplanation,
                                    fontSize = 12.sp,
                                    color = Color(0xFFE2E2E2),
                                    lineHeight = 16.sp
                                )

                                if (uiState.currentUser.hasPendingAppeal && isFraud) {
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Surface(
                                        color = Color(0xFF452B1E),
                                        shape = RoundedCornerShape(8.dp),
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Text(
                                            text = "📝 رسالة تظلم العميل للمالك: ${uiState.currentUser.appealMessage ?: "لا توجد رسالة"}",
                                            fontSize = 11.sp,
                                            color = RiskGold,
                                            modifier = Modifier.padding(8.dp)
                                        )
                                    }
                                }

                                if (isFraud) {
                                    Spacer(modifier = Modifier.height(10.dp))
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        Button(
                                            onClick = { viewModel.adminApprovePaymentAndUnban(req.id) },
                                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF10B981)),
                                            shape = RoundedCornerShape(8.dp),
                                            modifier = Modifier.weight(1f)
                                        ) {
                                            Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(14.dp))
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Text("فك الحظر واعتماد الدفع", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                        }

                                        OutlinedButton(
                                            onClick = { viewModel.adminRejectPaymentAndConfirmBan(req.id) },
                                            border = BorderStroke(1.dp, RiskBrightRed),
                                            shape = RoundedCornerShape(8.dp),
                                            modifier = Modifier.weight(1f)
                                        ) {
                                            Icon(Icons.Default.Close, contentDescription = null, tint = RiskBrightRed, modifier = Modifier.size(14.dp))
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Text("تأكيد الرفض والحظر", fontSize = 11.sp, color = RiskBrightRed)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // Reject Reason Modal Dialog for Order Payments
    selectedOrderToReject?.let { order ->
        AlertDialog(
            onDismissRequest = { selectedOrderToReject = null },
            title = { Text("رفض إيصال دفع الطلب ${order.orderId}", fontWeight = FontWeight.Bold, color = RiskBrightRed) },
            text = {
                Column {
                    Text("أدخل سبب رفض الإيصال لإشعار العميل به:", fontSize = 12.sp, color = Color.Gray)
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = rejectionReasonInput,
                        onValueChange = { rejectionReasonInput = it },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.adminRejectOrderPayment(order.orderId, rejectionReasonInput)
                        selectedOrderToReject = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = RiskBrightRed)
                ) {
                    Text("تأكيد الرفض")
                }
            },
            dismissButton = {
                TextButton(onClick = { selectedOrderToReject = null }) {
                    Text("إلغاء")
                }
            }
        )
    }

    // Receipt Screenshot Preview Dialog
    previewReceiptUri?.let { uri ->
        AlertDialog(
            onDismissRequest = { previewReceiptUri = null },
            title = { Text("معاينة سكرين شوت الدفع", fontWeight = FontWeight.Bold) },
            text = {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(Color(0xFF22222E)),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(Icons.Default.Image, contentDescription = null, tint = RiskGold, modifier = Modifier.size(48.dp))
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("صورة الإيصال المرفقة من العميل", fontWeight = FontWeight.Bold, color = Color.White, fontSize = 13.sp)
                            Text(uri, fontSize = 10.sp, color = Color.Gray, maxLines = 1)
                        }
                    }
                }
            },
            confirmButton = {
                Button(onClick = { previewReceiptUri = null }) {
                    Text("إغلاق المعاينة")
                }
            }
        )
    }
}

// --- 5. LIVE ORDERS ADMIN VIEW ---
@Composable
fun LiveOrdersAdminView(
    uiState: RiskUiState,
    viewModel: RiskRestaurantViewModel
) {
    val orders = uiState.activeOrders

    if (orders.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("لا توجد طلبات جارية حالياً", color = Color.Gray, fontSize = 14.sp)
        }
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(orders, key = { it.orderId }) { order ->
                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF1B1B26)),
                    border = BorderStroke(1.dp, Color(0xFF333348)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "طلب: ${order.orderId}",
                                fontWeight = FontWeight.Bold,
                                color = RiskGold,
                                fontSize = 15.sp
                            )
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = RiskBrightRed.copy(alpha = 0.2f)
                            ) {
                                Text(
                                    text = order.status.titleAr,
                                    fontSize = 11.sp,
                                    color = RiskBrightRed,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "${order.customerName} • ${order.customerPhone} • ${order.qenaZone.zoneName}",
                            fontSize = 12.sp,
                            color = Color.LightGray
                        )

                        Spacer(modifier = Modifier.height(8.dp))
                        // Items Preview
                        order.items.forEach { item ->
                            Text(
                                text = "• ${item.menuItem.nameAr} x${item.quantity} (${item.totalPrice.toInt()} ج)",
                                fontSize = 11.sp,
                                color = Color.Gray
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "الإجمالي: ${order.finalTotal.toInt()} ج.م (${order.paymentMethod.titleAr})",
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            fontSize = 13.sp
                        )

                        Spacer(modifier = Modifier.height(10.dp))
                        Text("تغيير حالة الطلب فوراً:", fontSize = 11.sp, color = Color.Gray)
                        Spacer(modifier = Modifier.height(6.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            OrderStatus.values().forEach { st ->
                                OutlinedButton(
                                    onClick = { viewModel.adminUpdateOrderStatus(order.orderId, st) },
                                    colors = ButtonDefaults.outlinedButtonColors(
                                        containerColor = if (order.status == st) RiskGold.copy(alpha = 0.2f) else Color.Transparent
                                    ),
                                    border = BorderStroke(1.dp, if (order.status == st) RiskGold else Color(0xFF3A3A4A)),
                                    shape = RoundedCornerShape(8.dp),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text(
                                        text = when (st) {
                                            OrderStatus.RECEIVED -> "استلام"
                                            OrderStatus.PREPARING -> "تحضير"
                                            OrderStatus.ON_THE_WAY -> "توصيل"
                                            OrderStatus.DELIVERED -> "تم"
                                        },
                                        fontSize = 10.sp,
                                        color = if (order.status == st) RiskGold else Color.LightGray,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

// --- 6. OWNERSHIP & ROLES VIEW ---
@Composable
fun OwnershipAndRolesView(
    uiState: RiskUiState,
    viewModel: RiskRestaurantViewModel,
    isOwner: Boolean
) {
    val context = LocalContext.current
    val config = uiState.restaurantConfig

    var showTransferDialog by remember { mutableStateOf(false) }
    var transferName by remember { mutableStateOf("") }
    var transferEmail by remember { mutableStateOf("") }

    var showEditCodeDialog by remember { mutableStateOf(false) }
    var newSupervisorCode by remember { mutableStateOf(config.supervisorSecretCode) }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Current Owner Card
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1B1B26)),
                border = BorderStroke(1.dp, RiskGold),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.AdminPanelSettings, contentDescription = null, tint = RiskGold, modifier = Modifier.size(28.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text("المالك الرسمي للمطعم (Owner)", fontWeight = FontWeight.Bold, color = RiskGold, fontSize = 15.sp)
                            Text("له كافة الصلاحيات المالية وتعديل الأسعار والمشرفين", fontSize = 11.sp, color = Color.Gray)
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                    Text("اسم المالك: ${config.ownerName ?: "غير محدد"}", fontSize = 14.sp, color = Color.White, fontWeight = FontWeight.SemiBold)
                    Text("البريد الإلكتروني: ${config.ownerEmail ?: "غير محدد"}", fontSize = 12.sp, color = Color.LightGray)
                    Text("معرف الحساب: ${config.ownerUserId ?: "غير محدد"}", fontSize = 10.sp, color = Color.DarkGray)

                    if (isOwner) {
                        Spacer(modifier = Modifier.height(14.dp))
                        Button(
                            onClick = { showTransferDialog = true },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF991B1B)),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(Icons.Default.SwapHoriz, contentDescription = null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("نقل ملكية التطبيق والمطعم لشخص آخر", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        // Supervisor Secret Code Card
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1B1B26)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Key, contentDescription = null, tint = Color(0xFF60A5FA))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("رمز تفويض المشرفين (Supervisor Code)", fontWeight = FontWeight.Bold, color = Color.White, fontSize = 13.sp)
                        }
                        if (isOwner) {
                            IconButton(onClick = { showEditCodeDialog = true }) {
                                Icon(Icons.Default.Edit, contentDescription = null, tint = RiskGold, modifier = Modifier.size(18.dp))
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "الرمز الحالي: ${config.supervisorSecretCode}",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Black,
                        color = RiskGold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "يشارك المالك هذا الرمز مع المشرفين المعتمدين ليتمكنوا من فتح لوحة التحكم ومتابعة الأسعار والطلبات.",
                        fontSize = 11.sp,
                        color = Color.LightGray,
                        lineHeight = 15.sp
                    )
                }
            }
        }
    }

    // Transfer Ownership Dialog
    if (showTransferDialog) {
        AlertDialog(
            onDismissRequest = { showTransferDialog = false },
            title = { Text("نقل ملكية مطعم ريسك", fontWeight = FontWeight.Bold, color = RiskBrightRed) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(
                        text = "⚠️ تحذير: بعد نقل الملكية، سيصبح الشخص الجديد هو المالك الرسمي الوحيد وسيتم تحويلك إلى مشرف:",
                        fontSize = 12.sp,
                        color = Color.LightGray
                    )
                    OutlinedTextField(
                        value = transferName,
                        onValueChange = { transferName = it },
                        label = { Text("اسم المالك الجديد") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = transferEmail,
                        onValueChange = { transferEmail = it },
                        label = { Text("البريد الإلكتروني للمالك الجديد") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (transferName.isNotBlank()) {
                            viewModel.transferOwnership(transferName, transferEmail)
                            showTransferDialog = false
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = RiskBrightRed)
                ) {
                    Text("تأكيد نقل الملكية")
                }
            },
            dismissButton = {
                TextButton(onClick = { showTransferDialog = false }) {
                    Text("إلغاء")
                }
            }
        )
    }

    // Edit Supervisor Code Dialog
    if (showEditCodeDialog) {
        AlertDialog(
            onDismissRequest = { showEditCodeDialog = false },
            title = { Text("تغيير رمز تفويض المشرفين", fontWeight = FontWeight.Bold) },
            text = {
                OutlinedTextField(
                    value = newSupervisorCode,
                    onValueChange = { newSupervisorCode = it },
                    label = { Text("الرمز السري الجديد (4 خانات فأكثر)") },
                    modifier = Modifier.fillMaxWidth()
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.updateSupervisorCode(newSupervisorCode)
                        showEditCodeDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = RiskBrightRed)
                ) {
                    Text("حفظ الرمز")
                }
            },
            dismissButton = {
                TextButton(onClick = { showEditCodeDialog = false }) {
                    Text("إلغاء")
                }
            }
        )
    }
}
