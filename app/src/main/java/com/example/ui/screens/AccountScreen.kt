package com.example.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.RiskMenuRepository
import com.example.data.UserRole
import com.example.ui.theme.RiskBrightRed
import com.example.ui.theme.RiskDarkRed
import com.example.ui.theme.RiskGold
import com.example.ui.theme.RiskPrimaryRed
import com.example.viewmodel.RiskRestaurantViewModel
import com.example.viewmodel.RiskUiState

data class FaqItem(val question: String, val answer: String)

@Composable
fun AccountScreen(
    uiState: RiskUiState,
    viewModel: RiskRestaurantViewModel,
    onNavigateToPaymentUpload: () -> Unit,
    onNavigateToAdmin: () -> Unit
) {
    val context = LocalContext.current
    var showEditProfileDialog by remember { mutableStateOf(false) }
    var showAppealDialog by remember { mutableStateOf(false) }
    var appealInputText by remember { mutableStateOf("") }
    var expandedFaqIndex by remember { mutableStateOf<Int?>(null) }

    val user = uiState.currentUser

    val faqs = remember {
        listOf(
            FaqItem(
                "ما هي مواعيد العمل والتوصيل في ريسك قنا؟",
                "نعمل يومياً من الساعة 12:00 ظهراً وحتى 3:00 فجراً لخدمتكم، وتتوفر خدمة التوصيل السريع لجميع مناطق مدينة قنا والقرى المحيطة."
            ),
            FaqItem(
                "كيف يتم شحن محفظة ريسك الرقمية؟",
                "تطبيق ريسك يعتمد نظام مالي حقيقي وصارم، حيث لا توجد نقاط وهمية. يتم الشحن بالتحويل الفعلي عبر فودافون كاش (01019043229) أو إنستاباي، ثم إرفاق إيصال التحويل ليقوم الذكاء الاصطناعي بمراجعته وإيداع المبلغ فوراً في محفظتك."
            ),
            FaqItem(
                "ماذا يحدث في حالة إرفاق إيصال تحويل مزور أو معدل؟",
                "يقوم نظام Gemini AI بفحص الإيصال بدقة وكشف أي تلاعب رقمي أو عدم تطابق في رقم المحفظة والمبلغ. في حال ثبوت الاحتيال يتم حظر الحساب فوراً تلقائياً، مع إمكانية تقديم التماس يدوياً لإدارة المطعم للمراجعة."
            ),
            FaqItem(
                "أين يقع مقر مطعم ريسك في قنا؟",
                "يقع مطعم ريسك في قلب قنا - منطقة المنشية - مباشرة أمام كافتيريا الزهراء، ونسعد باستقبالكم لتناول أشهى البيتزا والفطير والكريب ساخناً من الفرن."
            ),
            FaqItem(
                "ما هي المناطق المتاحة لخدمة الدليفري؟",
                "نوصل إلى المنشية، المساكن، الجامعة، الشئون، النحال، سيدي عبد الرحيم، الحصواية، المعنا، ودندرة بأسرع وقت مع كابتن التوصيل المجهز بحافظات حرارية."
            )
        )
    }

    // Three-finger swipe detector or long drag gesture down
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0F0F12))
            .pointerInput(Unit) {
                detectDragGestures { change, dragAmount ->
                    // If vertical downward drag is large enough
                    if (dragAmount.y > 60 && change.pressed) {
                        viewModel.triggerSecretDashboardFromGesture()
                    }
                }
            }
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item { Spacer(modifier = Modifier.height(8.dp)) }

            // 1. User Profile Header with Secret 20-Tap Trigger on Avatar
            item {
                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF1A1A22)),
                    border = BorderStroke(1.dp, if (user.isBanned) RiskBrightRed else RiskGold.copy(alpha = 0.4f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(18.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Avatar Box with 20-Tap Click Listener
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .size(88.dp)
                                .clip(CircleShape)
                                .background(
                                    Brush.radialGradient(
                                        listOf(
                                            RiskBrightRed.copy(alpha = 0.3f),
                                            Color(0xFF262630)
                                        )
                                    )
                                )
                                .border(
                                    BorderStroke(
                                        2.dp,
                                        if (user.role == UserRole.OWNER) RiskGold else RiskBrightRed
                                    ),
                                    CircleShape
                                )
                                .clickable {
                                    // 20-Tap Secret Admin Unlock Trigger!
                                    viewModel.registerProfileTap()
                                }
                                .testTag("profile_avatar_admin_trigger")
                        ) {
                            Icon(
                                imageVector = if (user.role == UserRole.OWNER) Icons.Default.AdminPanelSettings else Icons.Default.Person,
                                contentDescription = "الصورة الشخصية",
                                tint = if (user.role == UserRole.OWNER) RiskGold else Color.White,
                                modifier = Modifier.size(46.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // User Name & Role Badge
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = user.displayName,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = when (user.role) {
                                    UserRole.OWNER -> RiskGold.copy(alpha = 0.2f)
                                    UserRole.SUPERVISOR -> Color(0xFF3B82F6).copy(alpha = 0.2f)
                                    UserRole.CUSTOMER -> Color.White.copy(alpha = 0.1f)
                                },
                                border = BorderStroke(
                                    1.dp,
                                    when (user.role) {
                                        UserRole.OWNER -> RiskGold
                                        UserRole.SUPERVISOR -> Color(0xFF3B82F6)
                                        UserRole.CUSTOMER -> Color.White.copy(alpha = 0.2f)
                                    }
                                )
                            ) {
                                Text(
                                    text = user.role.titleAr,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = when (user.role) {
                                        UserRole.OWNER -> RiskGold
                                        UserRole.SUPERVISOR -> Color(0xFF60A5FA)
                                        UserRole.CUSTOMER -> Color.LightGray
                                    },
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "${user.phone} • ${user.address}",
                            fontSize = 13.sp,
                            color = Color.Gray
                        )

                        // Discreet Tap Progress (shown when user taps multiple times)
                        if (uiState.secretAdminTapCount in 5..19) {
                            Spacer(modifier = Modifier.height(6.dp))
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = RiskBrightRed.copy(alpha = 0.15f)
                            ) {
                                Text(
                                    text = "جاري تفعيل لوحة التحكم: نقرة ${uiState.secretAdminTapCount} من 20",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = RiskBrightRed,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Stats Summary Row
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = "${user.totalOrdersCount}",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = RiskGold
                                )
                                Text(
                                    text = "إجمالي الطلبات",
                                    fontSize = 11.sp,
                                    color = Color.Gray
                                )
                            }
                            Divider(
                                modifier = Modifier
                                    .height(30.dp)
                                    .width(1.dp),
                                color = Color(0xFF333340)
                            )
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = "${uiState.walletBalance.toInt()} ج.م",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF10B981)
                                )
                                Text(
                                    text = "رصيد المحفظة",
                                    fontSize = 11.sp,
                                    color = Color.Gray
                                )
                            }
                            Divider(
                                modifier = Modifier
                                    .height(30.dp)
                                    .width(1.dp),
                                color = Color(0xFF333340)
                            )
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = if (user.isBanned) "محظور" else "نشط وموثق",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (user.isBanned) RiskBrightRed else Color(0xFF10B981)
                                )
                                Text(
                                    text = "حالة الحساب",
                                    fontSize = 11.sp,
                                    color = Color.Gray
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Edit Profile Info Button
                        OutlinedButton(
                            onClick = { showEditProfileDialog = true },
                            shape = RoundedCornerShape(10.dp),
                            border = BorderStroke(1.dp, Color(0xFF3A3A48)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(16.dp), tint = Color.LightGray)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("تعديل بيانات الحساب (الاسم، الهاتف، العنوان)", fontSize = 12.sp, color = Color.LightGray)
                        }

                        // Direct button to open Admin Dashboard if already an Owner or Supervisor
                        if (user.role == UserRole.OWNER || user.role == UserRole.SUPERVISOR) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Button(
                                onClick = onNavigateToAdmin,
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF272733)),
                                border = BorderStroke(1.dp, RiskGold),
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Icon(Icons.Default.AdminPanelSettings, contentDescription = null, tint = RiskGold)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("فتح لوحة تحكم المطعم السرية (Admin)", color = RiskGold, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }

            // 2. BANNED BANNER (If user flagged for fraud receipt)
            if (user.isBanned) {
                item {
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF2D1214)),
                        border = BorderStroke(1.5.dp, RiskBrightRed),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Warning, contentDescription = null, tint = RiskBrightRed, modifier = Modifier.size(24.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "تنبيه أمني: الحساب محظور مؤقتاً!",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp,
                                    color = RiskBrightRed
                                )
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = user.banReason ?: "تم حظر الحساب لاشتباه في إيصال دفع غير مطابق عبر الذكاء الاصطناعي.",
                                fontSize = 13.sp,
                                color = Color(0xFFFFD2D2),
                                lineHeight = 18.sp
                            )
                            Spacer(modifier = Modifier.height(12.dp))

                            if (user.hasPendingAppeal) {
                                Surface(
                                    color = Color(0xFF452B1E),
                                    shape = RoundedCornerShape(8.dp),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text(
                                        text = "⏳ تم تقديم طلب التظلم وهو قيد المراجعة اليدوية من قبل إدارة ومسؤولي ريسك في لوحة التحكم.",
                                        fontSize = 12.sp,
                                        color = RiskGold,
                                        modifier = Modifier.padding(10.dp)
                                    )
                                }
                            } else {
                                Button(
                                    onClick = { showAppealDialog = true },
                                    colors = ButtonDefaults.buttonColors(containerColor = RiskBrightRed),
                                    shape = RoundedCornerShape(10.dp),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text("تقديم طلب تظلم ومراجعة للإدارة والمالك", fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }
            }

            // 3. EMBEDDED WALLET CARD (REAL FUNDS ONLY)
            item {
                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF181822)),
                    border = BorderStroke(1.dp, Color(0xFF2E2E3E)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.AccountBalanceWallet, contentDescription = null, tint = RiskGold)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "محفظة ريسك الرقمية",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = Color(0xFF10B981).copy(alpha = 0.15f)
                            ) {
                                Text(
                                    text = "نظام مالي حقيقي 100%",
                                    fontSize = 10.sp,
                                    color = Color(0xFF34D399),
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Current Balance Display
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(14.dp))
                                .background(
                                    Brush.horizontalGradient(
                                        listOf(Color(0xFF20202E), Color(0xFF171720))
                                    )
                                )
                                .padding(16.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(
                                        text = "الرصيد المتاح حالياً",
                                        fontSize = 12.sp,
                                        color = Color.Gray
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = "${uiState.walletBalance.toInt()} ج.م",
                                        fontSize = 28.sp,
                                        fontWeight = FontWeight.Black,
                                        color = if (uiState.walletBalance > 0) Color(0xFF10B981) else Color.White
                                    )
                                }

                                Button(
                                    onClick = onNavigateToPaymentUpload,
                                    colors = ButtonDefaults.buttonColors(containerColor = RiskBrightRed),
                                    shape = RoundedCornerShape(12.dp)
                                ) {
                                    Icon(Icons.Default.Receipt, contentDescription = null, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("شحن رصيد", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Security notice (As requested: no free points, real money only!)
                        Text(
                            text = "ℹ️ ملاحظة: الرصيد يُشحن فقط بالتحويل الفعلي المعتمد (فودافون كاش / إنستاباي) بعد مراجعة إيصال الدفع بواسطة الذكاء الاصطناعي.",
                            fontSize = 11.sp,
                            color = Color(0xFFAAAAAA),
                            lineHeight = 16.sp
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        // Transactions list
                        Text(
                            text = "سجل المعاملات الأخيرة",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.LightGray
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        if (uiState.walletTransactions.isEmpty()) {
                            Text(
                                text = "لا توجد معاملات بعد. اشحن محفظتك بالتحويل واستمتع بطلب فوري دون الحاجة لفكة!",
                                fontSize = 12.sp,
                                color = Color.Gray,
                                modifier = Modifier.padding(vertical = 8.dp)
                            )
                        } else {
                            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                uiState.walletTransactions.take(5).forEach { tx ->
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(Color(0xFF22222D))
                                            .padding(10.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Column {
                                            Text(tx.title, fontSize = 13.sp, color = Color.White, fontWeight = FontWeight.SemiBold)
                                            Text(tx.date, fontSize = 10.sp, color = Color.Gray)
                                        }
                                        Text(
                                            text = "${if (tx.isCredit) "+" else "-"}${tx.amount.toInt()} ج.م",
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = if (tx.isCredit) Color(0xFF10B981) else RiskBrightRed
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // 4. TECHNICAL SUPPORT & DIRECT CONTACT
            item {
                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF181822)),
                    border = BorderStroke(1.dp, Color(0xFF2E2E3E)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Security, contentDescription = null, tint = RiskGold)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "الدعم الفني وخدمة العملاء",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "فريق ريسك جاهز لمساعدتك وحل أي استفسار حول طلباتك أو التحويلات طوال فترة العمل.",
                            fontSize = 12.sp,
                            color = Color.Gray,
                            lineHeight = 16.sp
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        // WhatsApp Action Button
                        Button(
                            onClick = {
                                val url = "https://api.whatsapp.com/send?phone=201019043229&text=${Uri.encode("مرحباً إدارة ريسك، أحتاج مساعدة بخصوص حسابي/طلبي")}"
                                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                                context.startActivity(intent)
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF25D366)),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(Icons.Default.Chat, contentDescription = null, tint = Color.White)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("تواصل فوري عبر واتساب (01019043229)", color = Color.White, fontWeight = FontWeight.Bold)
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // Phone Call Actions
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            OutlinedButton(
                                onClick = {
                                    val dialIntent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:01019043229"))
                                    context.startActivity(dialIntent)
                                },
                                shape = RoundedCornerShape(10.dp),
                                border = BorderStroke(1.dp, Color(0xFF3B82F6)),
                                modifier = Modifier.weight(1f)
                            ) {
                                Icon(Icons.Default.Call, contentDescription = null, tint = Color(0xFF60A5FA), modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("اتصال فودافون", fontSize = 11.sp, color = Color(0xFF60A5FA))
                            }

                            OutlinedButton(
                                onClick = {
                                    val dialIntent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:01101137601"))
                                    context.startActivity(dialIntent)
                                },
                                shape = RoundedCornerShape(10.dp),
                                border = BorderStroke(1.dp, Color(0xFF10B981)),
                                modifier = Modifier.weight(1f)
                            ) {
                                Icon(Icons.Default.Call, contentDescription = null, tint = Color(0xFF34D399), modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("اتصال اتصالات", fontSize = 11.sp, color = Color(0xFF34D399))
                            }
                        }
                    }
                }
            }

            // 5. FREQUENTLY ASKED QUESTIONS (FAQ Accordion)
            item {
                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF181822)),
                    border = BorderStroke(1.dp, Color(0xFF2E2E3E)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.HelpOutline, contentDescription = null, tint = RiskGold)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "الأسئلة الشائعة (FAQ)",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        faqs.forEachIndexed { index, faq ->
                            val isExpanded = expandedFaqIndex == index
                            Card(
                                shape = RoundedCornerShape(12.dp),
                                colors = CardDefaults.cardColors(containerColor = Color(0xFF22222D)),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp)
                                    .clickable {
                                        expandedFaqIndex = if (isExpanded) null else index
                                    }
                            ) {
                                Column(modifier = Modifier.padding(14.dp)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = faq.question,
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = if (isExpanded) RiskGold else Color.White,
                                            modifier = Modifier.weight(1f)
                                        )
                                        Icon(
                                            imageVector = if (isExpanded) Icons.Default.ArrowDropUp else Icons.Default.ArrowDropDown,
                                            contentDescription = null,
                                            tint = Color.Gray
                                        )
                                    }

                                    AnimatedVisibility(
                                        visible = isExpanded,
                                        enter = expandVertically() + fadeIn(),
                                        exit = shrinkVertically() + fadeOut()
                                    ) {
                                        Column {
                                            Spacer(modifier = Modifier.height(8.dp))
                                            Divider(color = Color(0xFF333342))
                                            Spacer(modifier = Modifier.height(8.dp))
                                            Text(
                                                text = faq.answer,
                                                fontSize = 12.sp,
                                                color = Color(0xFFCCCCCC),
                                                lineHeight = 18.sp
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            item { Spacer(modifier = Modifier.height(24.dp)) }
        }
    }

    // Edit Profile Modal Dialog
    if (showEditProfileDialog) {
        var nameInput by remember { mutableStateOf(user.displayName) }
        var phoneInput by remember { mutableStateOf(user.phone) }
        var addressInput by remember { mutableStateOf(user.address) }

        AlertDialog(
            onDismissRequest = { showEditProfileDialog = false },
            title = { Text("تعديل بيانات الحساب", fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedTextField(
                        value = nameInput,
                        onValueChange = { nameInput = it },
                        label = { Text("الاسم بالكامل") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = phoneInput,
                        onValueChange = { phoneInput = it },
                        label = { Text("رقم الهاتف") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = addressInput,
                        onValueChange = { addressInput = it },
                        label = { Text("العنوان بالتفصيل في قنا") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.updateCustomerProfile(nameInput, phoneInput, addressInput)
                        showEditProfileDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = RiskBrightRed)
                ) {
                    Text("حفظ التغييرات")
                }
            },
            dismissButton = {
                TextButton(onClick = { showEditProfileDialog = false }) {
                    Text("إلغاء")
                }
            }
        )
    }

    // Ban Appeal Modal Dialog
    if (showAppealDialog) {
        AlertDialog(
            onDismissRequest = { showAppealDialog = false },
            title = { Text("تقديم تظلم لإدارة ريسك", fontWeight = FontWeight.Bold, color = RiskBrightRed) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(
                        text = "اكتب تفاصيل عملية التحويل (رقم المحفظة التي حولت منها، وقت التحويل، واسم المحول) لمراجعة الإيصال يدوياً وفك الحظر:",
                        fontSize = 12.sp,
                        color = Color.LightGray
                    )
                    OutlinedTextField(
                        value = appealInputText,
                        onValueChange = { appealInputText = it },
                        placeholder = { Text("مثال: حولت 100 ج من رقم 010xxxx الساعة 5:30 م والإيصال رسمي...") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(110.dp),
                        maxLines = 4
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.submitBanAppeal(appealInputText)
                        showAppealDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = RiskBrightRed)
                ) {
                    Text("إرسال التظلم للمالك")
                }
            },
            dismissButton = {
                TextButton(onClick = { showAppealDialog = false }) {
                    Text("إلغاء")
                }
            }
        )
    }
}
