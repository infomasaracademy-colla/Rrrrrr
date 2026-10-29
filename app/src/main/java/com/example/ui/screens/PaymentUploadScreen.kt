package com.example.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.material3.Text
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.AiVerificationStatus
import com.example.data.PaymentMethod
import com.example.ui.theme.RiskBrightRed
import com.example.ui.theme.RiskGold
import com.example.viewmodel.RiskRestaurantViewModel
import com.example.viewmodel.RiskUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentUploadScreen(
    uiState: RiskUiState,
    viewModel: RiskRestaurantViewModel,
    onNavigateBack: () -> Unit
) {
    val context = LocalContext.current

    var selectedMethod by remember { mutableStateOf(PaymentMethod.VODAFONE_CASH) }
    var amountInput by remember { mutableStateOf("100") }
    var refNumberInput by remember { mutableStateOf("TXN948201") }
    var selectedImageUri by remember { mutableStateOf<String?>(null) }
    var isTestFakeSelected by remember { mutableStateOf(false) }

    // Android Zero-permission Photo Picker
    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if (uri != null) {
            selectedImageUri = uri.toString()
            isTestFakeSelected = false
        }
    }

    val copyToClipboard: (String, String) -> Unit = { label, text ->
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText(label, text)
        clipboard.setPrimaryClip(clip)
        Toast.makeText(context, "تم نسخ $label: $text", Toast.LENGTH_SHORT).show()
    }

    val parsedAmount = amountInput.toDoubleOrNull() ?: 0.0

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "لوحة الدفع وفحص الإيصال بالـ AI",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
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
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF14141A))
            )
        },
        containerColor = Color(0xFF0F0F12)
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item { Spacer(modifier = Modifier.height(6.dp)) }

            // 1. Instructions Banner
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF1A1A24)),
                    border = BorderStroke(1.dp, RiskGold.copy(alpha = 0.4f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = RiskGold.copy(alpha = 0.2f),
                            modifier = Modifier.size(40.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(Icons.Default.Security, contentDescription = null, tint = RiskGold)
                            }
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                "نظام التحقق الآلي والآمن لمحفظة ريسك",
                                fontWeight = FontWeight.Bold,
                                color = RiskGold,
                                fontSize = 13.sp
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                "حول المبلغ إلى رقم محفظة ريسك، وارفع صورة إيصال التحويل، وسيقوم نظام الذكاء الاصطناعي بمراجعته وتنزيل الفلوس أوتوماتيك لمحفظتك.",
                                fontSize = 11.sp,
                                color = Color.LightGray,
                                lineHeight = 15.sp
                            )
                        }
                    }
                }
            }

            // 2. Select Payment Method & View Store Accounts
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF181822)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            "1. اختر طريقة التحويل وانسخ بيانات محفظة ريسك",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.height(12.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            FilterChip(
                                selected = selectedMethod == PaymentMethod.VODAFONE_CASH,
                                onClick = { selectedMethod = PaymentMethod.VODAFONE_CASH },
                                label = { Text("فودافون كاش", fontWeight = FontWeight.Bold) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = RiskBrightRed,
                                    selectedLabelColor = Color.White
                                ),
                                modifier = Modifier.weight(1f)
                            )
                            FilterChip(
                                selected = selectedMethod == PaymentMethod.INSTAPAY,
                                onClick = { selectedMethod = PaymentMethod.INSTAPAY },
                                label = { Text("إنستاباي (InstaPay)", fontWeight = FontWeight.Bold) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = Color(0xFF3B82F6),
                                    selectedLabelColor = Color.White
                                ),
                                modifier = Modifier.weight(1f)
                            )
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Selected Target Account Details with Copy Button
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color(0xFF22222E))
                                .padding(14.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(
                                        text = if (selectedMethod == PaymentMethod.VODAFONE_CASH) "رقم محفظة فودافون كاش للمطعم:" else "معرف إنستاباي لمطعم ريسك:",
                                        fontSize = 11.sp,
                                        color = Color.Gray
                                    )
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        text = if (selectedMethod == PaymentMethod.VODAFONE_CASH) "01019043229" else "risk.foods@instapay",
                                        fontSize = 17.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = RiskGold
                                    )
                                }

                                Button(
                                    onClick = {
                                        val textToCopy = if (selectedMethod == PaymentMethod.VODAFONE_CASH) "01019043229" else "risk.foods@instapay"
                                        copyToClipboard(if (selectedMethod == PaymentMethod.VODAFONE_CASH) "رقم فودافون كاش" else "معرف إنستاباي", textToCopy)
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF333345)),
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Icon(Icons.Default.ContentCopy, contentDescription = null, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("نسخ", fontSize = 12.sp)
                                }
                            }
                        }
                    }
                }
            }

            // 3. Amount & Reference Input
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF181822)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            "2. حدد المبلغ ورقم المعاملة المكتوب في الإيصال",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.height(10.dp))

                        // Quick Amount Chips
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            listOf("50", "100", "200", "300").forEach { quick ->
                                FilterChip(
                                    selected = amountInput == quick,
                                    onClick = { amountInput = quick },
                                    label = { Text("$quick ج") },
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = RiskGold.copy(alpha = 0.25f),
                                        selectedLabelColor = RiskGold
                                    ),
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        OutlinedTextField(
                            value = amountInput,
                            onValueChange = { amountInput = it },
                            label = { Text("المبلغ المحول بالجنيه (EGP)") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = RiskGold,
                                focusedLabelColor = RiskGold
                            ),
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        OutlinedTextField(
                            value = refNumberInput,
                            onValueChange = { refNumberInput = it },
                            label = { Text("رقم العملية أو المرجع (Reference Number)") },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = RiskGold,
                                focusedLabelColor = RiskGold
                            ),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }

            // 4. Receipt Attachment / Photo Picker & Testing Tools
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF181822)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            "3. إرفاق صورة / سكرين شوت إيصال التحويل",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.height(10.dp))

                        // Photo Picker Trigger Box
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(100.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color(0xFF22222E))
                                .clickable {
                                    photoPickerLauncher.launch(
                                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                    )
                                }
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(
                                    Icons.Default.AddPhotoAlternate,
                                    contentDescription = null,
                                    tint = if (selectedImageUri != null) Color(0xFF10B981) else RiskGold,
                                    modifier = Modifier.size(32.dp)
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = if (selectedImageUri != null) "تم إرفاق صورة الإيصال بنجاح ✓ (اضغط للتغيير)" else "اضغط هنا لاختيار صورة الإيصال من المعرض",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (selectedImageUri != null) Color(0xFF10B981) else Color.LightGray
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Quick Test Samples for Simulation in Browser/Emulator
                        Text(
                            "أو جرب عينات الفحص السريع لمحاكاة عمل الـ AI:",
                            fontSize = 11.sp,
                            color = Color.Gray
                        )
                        Spacer(modifier = Modifier.height(6.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            OutlinedButton(
                                onClick = {
                                    selectedImageUri = "sample://genuine_receipt"
                                    refNumberInput = "VF-${(100000..999999).random()}"
                                    isTestFakeSelected = false
                                    Toast.makeText(context, "تم تجهيز إيصال سليم تجريبي", Toast.LENGTH_SHORT).show()
                                },
                                shape = RoundedCornerShape(8.dp),
                                border = BorderStroke(1.dp, Color(0xFF10B981)),
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("إيصال حقيقي سليم", fontSize = 11.sp, color = Color(0xFF34D399), fontWeight = FontWeight.Bold)
                            }

                            OutlinedButton(
                                onClick = {
                                    selectedImageUri = "sample://fake_fraud_receipt"
                                    refNumberInput = "FAKE-000000"
                                    isTestFakeSelected = true
                                    Toast.makeText(context, "تم تجهيز إيصال مشبوه لاختبار كشف الاحتيال والحظر", Toast.LENGTH_SHORT).show()
                                },
                                shape = RoundedCornerShape(8.dp),
                                border = BorderStroke(1.dp, RiskBrightRed),
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("إيصال مشبوه/مزور (اختبار)", fontSize = 10.sp, color = RiskBrightRed, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }

            // 5. AI Verification Radar (When Processing)
            if (uiState.isAiVerifyingPayment) {
                item {
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF1A1A24)),
                        border = BorderStroke(1.5.dp, RiskGold),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(20.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            CircularProgressIndicator(color = RiskGold, modifier = Modifier.size(44.dp))
                            Spacer(modifier = Modifier.height(14.dp))
                            Text(
                                "نظام فحص الإيصالات بالذكاء الاصطناعي (Gemini AI)",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = RiskGold
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = uiState.aiVerificationProgressText,
                                fontSize = 12.sp,
                                color = Color.White,
                                textAlign = TextAlign.Center,
                                lineHeight = 16.sp
                            )
                        }
                    }
                }
            }

            // 6. Action Submit Button
            item {
                Button(
                    onClick = {
                        if (parsedAmount <= 0) {
                            Toast.makeText(context, "يرجى تحديد مبلغ صحيح", Toast.LENGTH_SHORT).show()
                            return@Button
                        }
                        if (selectedImageUri == null) {
                            selectedImageUri = "sample://default_genuine"
                        }
                        viewModel.verifyPaymentWithAi(
                            method = selectedMethod,
                            amount = parsedAmount,
                            referenceNumber = refNumberInput,
                            receiptImageUri = selectedImageUri,
                            isSampleFake = isTestFakeSelected
                        )
                    },
                    enabled = !uiState.isAiVerifyingPayment,
                    colors = ButtonDefaults.buttonColors(containerColor = RiskBrightRed),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                ) {
                    Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = RiskGold)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        "فحص الإيصال بالـ AI واعتماد شحن المحفظة",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }

            // 7. Last Result Feedback Card
            uiState.lastVerificationResult?.let { res ->
                item {
                    val isSuccess = res.status == AiVerificationStatus.APPROVED_AUTOMATIC || res.status == AiVerificationStatus.MANUALLY_APPROVED
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (isSuccess) Color(0xFF132A1C) else Color(0xFF331416)
                        ),
                        border = BorderStroke(1.5.dp, if (isSuccess) Color(0xFF10B981) else RiskBrightRed),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = if (isSuccess) Icons.Default.CheckCircle else Icons.Default.Warning,
                                    contentDescription = null,
                                    tint = if (isSuccess) Color(0xFF10B981) else RiskBrightRed,
                                    modifier = Modifier.size(24.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = if (isSuccess) "تم اعتماد التحويل وشحن المحفظة بنجاح!" else "تم رفض الإيصال وحظر الحساب!",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 15.sp,
                                    color = if (isSuccess) Color(0xFF34D399) else RiskBrightRed
                                )
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = res.aiExplanation,
                                fontSize = 12.sp,
                                color = Color.White,
                                lineHeight = 16.sp
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                text = "رقم المعاملة: ${res.id} • المبلغ: ${res.amount.toInt()} ج.م • دقة الفحص: ${(res.aiConfidence * 100).toInt()}%",
                                fontSize = 11.sp,
                                color = Color.LightGray
                            )
                        }
                    }
                }
            }

            item { Spacer(modifier = Modifier.height(24.dp)) }
        }
    }
}
