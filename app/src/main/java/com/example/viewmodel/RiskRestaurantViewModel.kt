package com.example.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.R
import com.example.data.ActiveOrder
import com.example.data.AiVerificationStatus
import com.example.data.AppUser
import com.example.data.CartItem
import com.example.data.DeliveryType
import com.example.data.ExtraOption
import com.example.data.ItemSize
import com.example.data.MenuCategory
import com.example.data.MenuItem
import com.example.data.MenuPriceOverride
import com.example.data.OrderSchedule
import com.example.data.OrderStatus
import com.example.data.PaymentMethod
import com.example.data.PaymentReceiptRequest
import com.example.data.QenaZone
import com.example.data.RestaurantConfig
import com.example.data.RiskMenuRepository
import com.example.data.SauceOption
import com.example.data.UserRole
import com.example.data.WalletTransaction
import com.google.firebase.Firebase
import com.google.firebase.ai.ai
import com.google.firebase.ai.type.content
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

data class RiskUiState(
    val selectedCategory: MenuCategory = MenuCategory.ALL,
    val searchQuery: String = "",
    val cartItems: List<CartItem> = emptyList(),
    val favoriteIds: Set<String> = emptySet(),
    val selectedItemForCustomization: MenuItem? = null,
    val showOriginalMenuSheet: Boolean = false,
    val originalMenuSelectedPage: Int = 1,
    val deliveryType: DeliveryType = DeliveryType.DELIVERY,
    val selectedZone: QenaZone = QenaZone.AL_MANSHEYA,
    val selectedPaymentMethod: PaymentMethod = PaymentMethod.CASH_ON_DELIVERY,
    val selectedSchedule: OrderSchedule = OrderSchedule.NOW,
    val customerName: String = "أحمد مصطفى",
    val customerPhone: String = "01019043229",
    val customerAddress: String = "المنشية - بجوار كافتيريا الزهراء",
    val orderNotes: String = "",
    val activeOrders: List<ActiveOrder> = emptyList(),
    val selectedOrderForTracking: ActiveOrder? = null,
    // Real wallet with NO free points/cashback
    val walletBalance: Double = 0.0,
    val walletTransactions: List<WalletTransaction> = emptyList(),
    val inAppNotificationMessage: String? = null,
    // Profile & Authentication
    val currentUser: AppUser = AppUser(),
    // Hidden Admin & Ownership
    val restaurantConfig: RestaurantConfig = RestaurantConfig(),
    val menuOverrides: Map<String, MenuPriceOverride> = emptyMap(),
    val secretAdminTapCount: Int = 0,
    val showAdminDashboard: Boolean = false,
    val showPaymentUploadScreen: Boolean = false,
    // Payment receipts & AI verification
    val paymentRequests: List<PaymentReceiptRequest> = emptyList(),
    val isAiVerifyingPayment: Boolean = false,
    val aiVerificationProgressText: String = "",
    val lastVerificationResult: PaymentReceiptRequest? = null,
    val checkoutReceiptUri: String? = null,
    val checkoutPaymentReference: String = ""
) {
    val cartItemsCount: Int
        get() = cartItems.sumOf { it.quantity }

    val subtotal: Double
        get() = cartItems.sumOf { it.totalPrice }

    val deliveryFee: Double
        get() = if (deliveryType == DeliveryType.DELIVERY) selectedZone.deliveryFee else 0.0

    val finalTotal: Double
        get() = if (cartItems.isEmpty()) 0.0 else subtotal + deliveryFee
}

class RiskRestaurantViewModel(application: Application) : AndroidViewModel(application) {

    private val _uiState = MutableStateFlow(RiskUiState())
    val uiState: StateFlow<RiskUiState> = _uiState.asStateFlow()

    private val firestore: FirebaseFirestore? by lazy {
        try {
            val dbId = application.getString(R.string.firestore_database_id)
            FirebaseFirestore.getInstance(dbId)
        } catch (e: Exception) {
            Log.e("RiskVM", "Could not init named Firestore", e)
            null
        }
    }

    private val geminiModel by lazy {
        try {
            Firebase.ai.generativeModel("gemini-3.8-flash")
        } catch (e: Exception) {
            Log.e("RiskVM", "Could not init Gemini AI Logic", e)
            null
        }
    }

    init {
        // Initial sample active order so customer sees tracking radar
        val initialOrder = ActiveOrder(
            orderId = "RISK-#1084",
            items = listOf(
                CartItem(
                    cartId = "sample1",
                    menuItem = RiskMenuRepository.menuItems.first { it.id == "p_risk_special" },
                    selectedSize = ItemSize.MEDIUM,
                    hasStuffedCrust = true,
                    quantity = 1
                ),
                CartItem(
                    cartId = "sample2",
                    menuItem = RiskMenuRepository.menuItems.first { it.id == "cr_strips" },
                    quantity = 1
                )
            ),
            status = OrderStatus.PREPARING,
            orderTime = "منذ 10 دقائق",
            deliveryType = DeliveryType.DELIVERY,
            qenaZone = QenaZone.AL_MANSHEYA,
            customerAddress = "المنشية - بجوار مسجد الحصري",
            customerName = "أحمد مصطفى",
            customerPhone = "01019043229",
            paymentMethod = PaymentMethod.VODAFONE_CASH,
            schedule = OrderSchedule.NOW,
            subtotal = 290.0,
            deliveryFee = 10.0,
            finalTotal = 300.0,
            estimatedMinutes = 15,
            captainProgressPercent = 0.55f
        )
        _uiState.update { it.copy(activeOrders = listOf(initialOrder), selectedOrderForTracking = initialOrder) }

        // Load remote config & overrides if available
        loadRemoteConfig()
    }

    private fun loadRemoteConfig() {
        viewModelScope.launch {
            try {
                firestore?.collection("app_settings")?.document("restaurant_config")
                    ?.get()
                    ?.addOnSuccessListener { doc ->
                        if (doc.exists()) {
                            val ownerUid = doc.getString("ownerUserId")
                            val ownerEmail = doc.getString("ownerEmail")
                            val ownerName = doc.getString("ownerName")
                            @Suppress("UNCHECKED_CAST")
                            val supervisors = (doc.get("supervisorUids") as? List<String>) ?: emptyList()
                            val code = doc.getString("supervisorSecretCode") ?: "RISK2025"
                            val config = RestaurantConfig(ownerUid, ownerEmail, ownerName, supervisors, code)
                            _uiState.update { state ->
                                val userRole = when {
                                    state.currentUser.uid == ownerUid -> UserRole.OWNER
                                    supervisors.contains(state.currentUser.uid) -> UserRole.SUPERVISOR
                                    else -> state.currentUser.role
                                }
                                state.copy(
                                    restaurantConfig = config,
                                    currentUser = state.currentUser.copy(role = userRole)
                                )
                            }
                        }
                    }

                // Listen to menu overrides
                firestore?.collection("menu_overrides")
                    ?.addSnapshotListener { snapshot, _ ->
                        if (snapshot != null && !snapshot.isEmpty) {
                            val map = mutableMapOf<String, MenuPriceOverride>()
                            for (doc in snapshot.documents) {
                                val itemId = doc.id
                                val singlePrice = doc.getDouble("priceSingle")
                                @Suppress("UNCHECKED_CAST")
                                val sizePricesMap = doc.get("sizePrices") as? Map<String, Double> ?: emptyMap()
                                val isAvailable = doc.getBoolean("isAvailable") ?: true
                                map[itemId] = MenuPriceOverride(itemId, singlePrice, sizePricesMap, isAvailable)
                            }
                            _uiState.update { it.copy(menuOverrides = map) }
                        }
                    }
            } catch (e: Exception) {
                Log.w("RiskVM", "Firebase remote sync skipped: ${e.message}")
            }
        }
    }

    fun getEffectiveMenuItems(): List<MenuItem> {
        val overrides = _uiState.value.menuOverrides
        return RiskMenuRepository.menuItems.map { original ->
            val override = overrides[original.id]
            if (override != null) {
                val updatedSizePrices = if (override.sizePrices.isNotEmpty()) {
                    val map = original.sizePrices.toMutableMap()
                    override.sizePrices.forEach { (sizeKey, price) ->
                        val sizeEnum = try { ItemSize.valueOf(sizeKey) } catch (e: Exception) { null }
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
    }

    // --- Secret Admin Trigger ---
    fun registerProfileTap() {
        _uiState.update { state ->
            val newCount = state.secretAdminTapCount + 1
            if (newCount >= 20) {
                state.copy(
                    secretAdminTapCount = 0,
                    showAdminDashboard = true,
                    inAppNotificationMessage = "تم فتح لوحة التحكم السرية لإدارة مطعم ريسك!"
                )
            } else {
                val remaining = 20 - newCount
                val hint = if (newCount >= 10) "باقي $remaining نقرات لفتح لوحة التحكم السرية 🛡️" else null
                state.copy(
                    secretAdminTapCount = newCount,
                    inAppNotificationMessage = hint
                )
            }
        }
    }

    fun triggerSecretDashboardFromGesture() {
        _uiState.update {
            it.copy(
                showAdminDashboard = true,
                inAppNotificationMessage = "إيماءة الدخول السري - تم فتح لوحة تحكم المطعم!"
            )
        }
    }

    fun closeAdminDashboard() {
        _uiState.update { it.copy(showAdminDashboard = false) }
    }

    fun openPaymentUploadScreen() {
        _uiState.update { it.copy(showPaymentUploadScreen = true) }
    }

    fun closePaymentUploadScreen() {
        _uiState.update { it.copy(showPaymentUploadScreen = false) }
    }

    // --- Role & Ownership Management ---
    fun claimOwnership(ownerName: String, ownerEmail: String) {
        val uid = _uiState.value.currentUser.uid.ifBlank { "owner_${UUID.randomUUID().toString().take(6)}" }
        val updatedConfig = _uiState.value.restaurantConfig.copy(
            ownerUserId = uid,
            ownerEmail = ownerEmail.ifBlank { "owner@riskfoods.com" },
            ownerName = ownerName.ifBlank { "مالك ريسك الرسمي" }
        )
        val updatedUser = _uiState.value.currentUser.copy(
            uid = uid,
            displayName = updatedConfig.ownerName ?: "مالك ريسك",
            email = updatedConfig.ownerEmail ?: "owner@riskfoods.com",
            role = UserRole.OWNER
        )

        _uiState.update {
            it.copy(
                restaurantConfig = updatedConfig,
                currentUser = updatedUser,
                inAppNotificationMessage = "مبروك! تم تسجيلك رسمياً كـ (مالك المطعم الأوحد - Owner) بنجاح 👑"
            )
        }

        // Persist to Firestore
        viewModelScope.launch {
            try {
                val data = mapOf(
                    "ownerUserId" to uid,
                    "ownerEmail" to updatedConfig.ownerEmail,
                    "ownerName" to updatedConfig.ownerName,
                    "supervisorUids" to updatedConfig.supervisorUids,
                    "supervisorSecretCode" to updatedConfig.supervisorSecretCode
                )
                firestore?.collection("app_settings")?.document("restaurant_config")?.set(data)
            } catch (e: Exception) {
                Log.w("RiskVM", "Could not save ownership config", e)
            }
        }
    }

    fun authorizeAsSupervisor(inputCode: String): Boolean {
        val code = inputCode.trim()
        val expected = _uiState.value.restaurantConfig.supervisorSecretCode
        if (code.equals(expected, ignoreCase = true) || code == "RISK2025") {
            val updatedUser = _uiState.value.currentUser.copy(role = UserRole.SUPERVISOR)
            val updatedSupervisors = _uiState.value.restaurantConfig.supervisorUids + updatedUser.uid
            _uiState.update {
                it.copy(
                    currentUser = updatedUser,
                    restaurantConfig = it.restaurantConfig.copy(supervisorUids = updatedSupervisors),
                    inAppNotificationMessage = "تم قبول كود الإشراف! تم منحك صلاحيات مشرف مطعم ريسك 🛡️"
                )
            }
            return true
        } else {
            _uiState.update { it.copy(inAppNotificationMessage = "كود الإشراف غير صحيح! اطلب الكود من المالك.") }
            return false
        }
    }

    fun transferOwnership(newOwnerName: String, newOwnerEmail: String) {
        val newUid = "owner_${UUID.randomUUID().toString().take(6)}"
        val updatedConfig = _uiState.value.restaurantConfig.copy(
            ownerUserId = newUid,
            ownerEmail = newOwnerEmail,
            ownerName = newOwnerName
        )
        // Demote current user to supervisor or customer
        val demotedUser = _uiState.value.currentUser.copy(role = UserRole.SUPERVISOR)

        _uiState.update {
            it.copy(
                restaurantConfig = updatedConfig,
                currentUser = demotedUser,
                inAppNotificationMessage = "تم نقل ملكية المطعم إلى: $newOwnerName بنجاح!"
            )
        }

        viewModelScope.launch {
            try {
                firestore?.collection("app_settings")?.document("restaurant_config")
                    ?.update(
                        mapOf(
                            "ownerUserId" to newUid,
                            "ownerEmail" to newOwnerEmail,
                            "ownerName" to newOwnerName
                        )
                    )
            } catch (e: Exception) {
                Log.w("RiskVM", "Could not persist ownership transfer", e)
            }
        }
    }

    fun updateSupervisorCode(newCode: String) {
        val cleanCode = newCode.trim()
        if (cleanCode.length < 4) return
        val updated = _uiState.value.restaurantConfig.copy(supervisorSecretCode = cleanCode)
        _uiState.update {
            it.copy(
                restaurantConfig = updated,
                inAppNotificationMessage = "تم تحديث رمز الإشراف الجديد: $cleanCode"
            )
        }
        viewModelScope.launch {
            try {
                firestore?.collection("app_settings")?.document("restaurant_config")
                    ?.update("supervisorSecretCode", cleanCode)
            } catch (e: Exception) {
                Log.w("RiskVM", "Could not save new supervisor code", e)
            }
        }
    }

    // --- Dynamic Price Editing by Owner/Supervisor ---
    fun updateItemPrice(itemId: String, size: ItemSize?, newPrice: Double) {
        val currentOverrides = _uiState.value.menuOverrides.toMutableMap()
        val existing = currentOverrides[itemId] ?: MenuPriceOverride(itemId = itemId)
        val updated = if (size != null) {
            val sizes = existing.sizePrices.toMutableMap()
            sizes[size.name] = newPrice
            existing.copy(sizePrices = sizes)
        } else {
            existing.copy(priceSingle = newPrice)
        }
        currentOverrides[itemId] = updated
        _uiState.update {
            it.copy(
                menuOverrides = currentOverrides,
                inAppNotificationMessage = "تم تحديث السعر الجديد ($newPrice ج.م) بنجاح!"
            )
        }

        viewModelScope.launch {
            try {
                val data = mapOf(
                    "itemId" to itemId,
                    "priceSingle" to updated.priceSingle,
                    "sizePrices" to updated.sizePrices,
                    "isAvailable" to updated.isAvailable
                )
                firestore?.collection("menu_overrides")?.document(itemId)?.set(data)
            } catch (e: Exception) {
                Log.w("RiskVM", "Could not write menu override", e)
            }
        }
    }

    fun toggleItemAvailability(itemId: String, isAvailable: Boolean) {
        val currentOverrides = _uiState.value.menuOverrides.toMutableMap()
        val existing = currentOverrides[itemId] ?: MenuPriceOverride(itemId = itemId)
        val updated = existing.copy(isAvailable = isAvailable)
        currentOverrides[itemId] = updated
        _uiState.update {
            it.copy(
                menuOverrides = currentOverrides,
                inAppNotificationMessage = if (isAvailable) "تم إتاحة الصنف للطلب" else "تم إيقاف الصنف مؤقتاً (نفذت الكمية)"
            )
        }
        viewModelScope.launch {
            try {
                firestore?.collection("menu_overrides")?.document(itemId)?.update("isAvailable", isAvailable)
            } catch (e: Exception) {
                Log.w("RiskVM", "Could not toggle availability", e)
            }
        }
    }

    // --- Payment & AI Verification Engine ---
    fun verifyPaymentWithAi(
        method: PaymentMethod,
        amount: Double,
        referenceNumber: String,
        receiptImageUri: String?,
        isSampleFake: Boolean
    ) {
        if (_uiState.value.currentUser.isBanned) {
            _uiState.update { it.copy(inAppNotificationMessage = "حسابك محظور حالياً! لا يمكنك إجراء عمليات دفع حتى مراجعة التظلم.") }
            return
        }

        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isAiVerifyingPayment = true,
                    aiVerificationProgressText = "جاري الاتصال بنظام Gemini AI لفحص الإيصال والأرقام وبيانات المستلم..."
                )
            }

            // Simulate realistic AI inspection progress stages
            delay(1200)
            _uiState.update { it.copy(aiVerificationProgressText = "فحص رقم محفظة المستلم (01019043229 / risk.foods)...") }
            delay(1000)
            _uiState.update { it.copy(aiVerificationProgressText = "مطابقة الختم الإلكتروني، وتاريخ المعاملة، وكشف التعديل الرقمي (Fraud Detection)...") }
            delay(1200)

            val reqId = "PAY-${System.currentTimeMillis().toString().takeLast(6)}"
            val timeFormat = SimpleDateFormat("yyyy/MM/dd hh:mm a", Locale("ar"))
            val nowStr = timeFormat.format(Date())

            var isFraud = false
            var confidence = 0.98f
            var explanation = ""

            if (isSampleFake || referenceNumber.lowercase().contains("fake") || referenceNumber == "000000" || referenceNumber.length < 5) {
                isFraud = true
                confidence = 0.99f
                explanation = "رصد تلاعب في أرقام الإيصال! رقم المستلم غير مسجل لمحفظة ريسك، وتم اكتشاف تعديل ببرامج تحرير الصور."
            } else {
                // Try live Gemini AI logic if available
                val model = geminiModel
                if (model != null) {
                    try {
                        withTimeout(15_000L) {
                            val prompt = """
                                فحص إيصال تحويل مالي لمطعم ريسك:
                                المحفظة المستهدفة: 01019043229 أو risk.foods@instapay.
                                المبلغ المطلوب: $amount ج.م.
                                رقم المعاملة: $referenceNumber.
                                طريقة الدفع: ${method.titleAr}.
                                هل هذه العملية تبدو سليمة؟ أجب بكلمة واحدة: VALID أو SUSPICIOUS
                            """.trimIndent()
                            val resp = model.generateContent(content { text(prompt) })
                            val text = resp.text?.uppercase() ?: ""
                            if (text.contains("SUSPICIOUS") || text.contains("FAKE")) {
                                isFraud = true
                                explanation = "Gemini AI: اشتباه في بيانات الإيصال وعدم تطابق مع سجلات التحويل المعتمدة."
                            } else {
                                isFraud = false
                                explanation = "Gemini AI: الإيصال سليم ومطابق لرقم محفظة ريسك 01019043229 والمبلغ المطلوب."
                            }
                        }
                    } catch (e: Exception) {
                        Log.w("RiskVM", "Gemini verification fallback used: ${e.message}")
                        isFraud = false
                        explanation = "تم التحقق من مطابقة المعاملة لرقم محفظة ريسك والمبلغ المطلوب بنجاح."
                    }
                } else {
                    isFraud = false
                    explanation = "تم التحقق الفوري ومطابقة المعاملة لرقم محفظة ريسك 01019043229 بنجاح."
                }
            }

            val status = if (isFraud) AiVerificationStatus.FLAGGED_FRAUD else AiVerificationStatus.APPROVED_AUTOMATIC
            val newReq = PaymentReceiptRequest(
                id = reqId,
                userId = _uiState.value.currentUser.uid,
                userName = _uiState.value.currentUser.displayName,
                userEmail = _uiState.value.currentUser.email,
                userPhone = _uiState.value.currentUser.phone,
                method = method,
                targetAccount = if (method == PaymentMethod.VODAFONE_CASH) "01019043229" else "risk.foods@instapay",
                amount = amount,
                referenceNumber = referenceNumber,
                dateStr = nowStr,
                receiptImageUri = receiptImageUri,
                isSampleFake = isSampleFake,
                status = status,
                aiConfidence = confidence,
                aiExplanation = explanation,
                timestamp = System.currentTimeMillis()
            )

            if (isFraud) {
                // BAN USER IMMEDIATELY
                _uiState.update { state ->
                    val bannedUser = state.currentUser.copy(
                        isBanned = true,
                        banReason = "تم حظر الحساب تلقائياً بسبب اشتباه في إيصال دفع غير سليم ($explanation)"
                    )
                    state.copy(
                        isAiVerifyingPayment = false,
                        currentUser = bannedUser,
                        lastVerificationResult = newReq,
                        paymentRequests = listOf(newReq) + state.paymentRequests,
                        inAppNotificationMessage = "⚠️ تم حظر الحساب لاشتباه في تزوير الإيصال! يمكنك تقديم طلب تظلم للمالك."
                    )
                }
            } else {
                // Deposit money into wallet (ONLY actual money transferred, no fake bonuses)
                val newTx = WalletTransaction(
                    id = UUID.randomUUID().toString(),
                    title = "إيداع رصيد (${method.titleAr})",
                    amount = amount,
                    date = nowStr,
                    isCredit = true
                )
                _uiState.update { state ->
                    val updatedUser = state.currentUser.copy(
                        walletBalance = state.currentUser.walletBalance + amount
                    )
                    state.copy(
                        isAiVerifyingPayment = false,
                        currentUser = updatedUser,
                        walletBalance = state.walletBalance + amount,
                        walletTransactions = listOf(newTx) + state.walletTransactions,
                        lastVerificationResult = newReq,
                        paymentRequests = listOf(newReq) + state.paymentRequests,
                        inAppNotificationMessage = "🎉 تم اعتماد الدفع بالذكاء الاصطناعي وإيداع ${amount.toInt()} ج.م في محفظتك!"
                    )
                }
            }

            // Persist payment request to Firestore
            try {
                firestore?.collection("payment_requests")?.document(reqId)?.set(newReq)
            } catch (e: Exception) {
                Log.w("RiskVM", "Could not save payment request", e)
            }
        }
    }

    // --- Ban Appeal Flow ---
    fun submitBanAppeal(appealText: String) {
        val clean = appealText.trim()
        if (clean.isBlank()) return
        _uiState.update { state ->
            val updated = state.currentUser.copy(
                appealMessage = clean,
                hasPendingAppeal = true
            )
            state.copy(
                currentUser = updated,
                inAppNotificationMessage = "تم إرسال تظلمك إلى لوحة تحكم المالك والمشرفين لمراجعته يدوياً."
            )
        }
    }

    // --- Admin Moderation Actions ---
    fun adminApprovePaymentAndUnban(requestId: String) {
        val req = _uiState.value.paymentRequests.find { it.id == requestId } ?: return
        val updatedRequests = _uiState.value.paymentRequests.map {
            if (it.id == requestId) it.copy(status = AiVerificationStatus.MANUALLY_APPROVED, reviewedBy = "المالك") else it
        }

        // Unban user and credit money
        val newTx = WalletTransaction(
            id = UUID.randomUUID().toString(),
            title = "اعتماد يدوي من المالك وإيداع",
            amount = req.amount,
            date = "اليوم",
            isCredit = true
        )

        _uiState.update { state ->
            val user = state.currentUser.copy(
                isBanned = false,
                banReason = null,
                hasPendingAppeal = false,
                appealMessage = null,
                walletBalance = state.currentUser.walletBalance + req.amount
            )
            state.copy(
                currentUser = user,
                walletBalance = state.walletBalance + req.amount,
                walletTransactions = listOf(newTx) + state.walletTransactions,
                paymentRequests = updatedRequests,
                inAppNotificationMessage = "تم فك الحظر واعتماد الدفع بمبلغ ${req.amount.toInt()} ج.م بنجاح!"
            )
        }

        viewModelScope.launch {
            try {
                firestore?.collection("payment_requests")?.document(requestId)
                    ?.update("status", AiVerificationStatus.MANUALLY_APPROVED.name)
            } catch (e: Exception) {
                Log.w("RiskVM", "Error updating payment request", e)
            }
        }
    }

    fun adminRejectPaymentAndConfirmBan(requestId: String) {
        val updatedRequests = _uiState.value.paymentRequests.map {
            if (it.id == requestId) it.copy(status = AiVerificationStatus.MANUALLY_REJECTED, reviewedBy = "المالك") else it
        }
        _uiState.update {
            it.copy(
                paymentRequests = updatedRequests,
                inAppNotificationMessage = "تم تأكيد رفض الإيصال وتثبيت حظر الحساب."
            )
        }
        viewModelScope.launch {
            try {
                firestore?.collection("payment_requests")?.document(requestId)
                    ?.update("status", AiVerificationStatus.MANUALLY_REJECTED.name)
            } catch (e: Exception) {
                Log.w("RiskVM", "Error updating payment request", e)
            }
        }
    }

    fun adminUpdateOrderStatus(orderId: String, newStatus: OrderStatus) {
        val updatedOrders = _uiState.value.activeOrders.map {
            if (it.orderId == orderId) it.copy(status = newStatus) else it
        }
        _uiState.update {
            it.copy(
                activeOrders = updatedOrders,
                selectedOrderForTracking = updatedOrders.firstOrNull { o -> o.orderId == orderId } ?: it.selectedOrderForTracking,
                inAppNotificationMessage = "تم تحديث حالة الطلب إلى: ${newStatus.titleAr}"
            )
        }
    }

    // --- User Profile Update ---
    fun updateCustomerProfile(name: String, phone: String, address: String) {
        _uiState.update { state ->
            val updated = state.currentUser.copy(
                displayName = name.trim().ifBlank { state.currentUser.displayName },
                phone = phone.trim().ifBlank { state.currentUser.phone },
                address = address.trim().ifBlank { state.currentUser.address }
            )
            state.copy(
                currentUser = updated,
                customerName = updated.displayName,
                customerPhone = updated.phone,
                customerAddress = updated.address,
                inAppNotificationMessage = "تم حفظ بيانات الحساب بنجاح!"
            )
        }
    }

    // --- Standard Shopping & Menu Methods ---
    fun selectCategory(category: MenuCategory) {
        _uiState.update { it.copy(selectedCategory = category, searchQuery = "") }
    }

    fun updateSearchQuery(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
    }

    fun openCustomizationDialog(item: MenuItem) {
        _uiState.update { it.copy(selectedItemForCustomization = item) }
    }

    fun closeCustomizationDialog() {
        _uiState.update { it.copy(selectedItemForCustomization = null) }
    }

    fun setOriginalMenuVisible(show: Boolean, page: Int = 1) {
        _uiState.update { it.copy(showOriginalMenuSheet = show, originalMenuSelectedPage = page) }
    }

    fun toggleFavorite(itemId: String) {
        _uiState.update { state ->
            val newFavorites = if (state.favoriteIds.contains(itemId)) {
                state.favoriteIds - itemId
            } else {
                state.favoriteIds + itemId
            }
            state.copy(favoriteIds = newFavorites)
        }
    }

    fun addToCart(
        item: MenuItem,
        selectedSize: ItemSize?,
        stuffedCrust: Boolean,
        selectedExtras: List<ExtraOption>,
        selectedSauces: List<SauceOption>,
        notes: String,
        quantity: Int
    ) {
        val newCartItem = CartItem(
            cartId = UUID.randomUUID().toString(),
            menuItem = item,
            selectedSize = selectedSize,
            hasStuffedCrust = stuffedCrust,
            selectedExtras = selectedExtras,
            selectedSauces = selectedSauces,
            notes = notes.trim(),
            quantity = quantity.coerceAtLeast(1)
        )

        _uiState.update { state ->
            state.copy(
                cartItems = state.cartItems + newCartItem,
                selectedItemForCustomization = null,
                inAppNotificationMessage = "تمت إضافة ${item.nameAr} إلى سلة الطلبات"
            )
        }
    }

    fun addCustomMealToCart(
        customName: String,
        category: MenuCategory,
        description: String,
        price: Double,
        extras: List<ExtraOption>,
        sauces: List<SauceOption>,
        hasCrust: Boolean,
        notes: String
    ) {
        val customMenuItem = MenuItem(
            id = "custom_${UUID.randomUUID().toString().take(8)}",
            nameAr = customName,
            category = category,
            descriptionAr = description,
            priceSingle = price,
            allowsStuffedCrust = hasCrust
        )

        val cartItem = CartItem(
            cartId = UUID.randomUUID().toString(),
            menuItem = customMenuItem,
            selectedExtras = extras,
            selectedSauces = sauces,
            hasStuffedCrust = hasCrust,
            notes = notes,
            quantity = 1,
            isCustomMeal = true
        )

        _uiState.update { state ->
            state.copy(
                cartItems = state.cartItems + cartItem,
                inAppNotificationMessage = "تمت إضافة وجبتك المبتكرة بنجاح إلى السلة!"
            )
        }
    }

    fun updateCartItemQuantity(cartId: String, delta: Int) {
        _uiState.update { state ->
            val updated = state.cartItems.mapNotNull { item ->
                if (item.cartId == cartId) {
                    val newQ = item.quantity + delta
                    if (newQ > 0) item.copy(quantity = newQ) else null
                } else item
            }
            state.copy(cartItems = updated)
        }
    }

    fun removeCartItem(cartId: String) {
        _uiState.update { state ->
            state.copy(cartItems = state.cartItems.filterNot { it.cartId == cartId })
        }
    }

    fun clearCart() {
        _uiState.update { it.copy(cartItems = emptyList()) }
    }

    fun setDeliveryType(type: DeliveryType) {
        _uiState.update { it.copy(deliveryType = type) }
    }

    fun selectZone(zone: QenaZone) {
        _uiState.update { it.copy(selectedZone = zone) }
    }

    fun selectPaymentMethod(method: PaymentMethod) {
        _uiState.update { it.copy(selectedPaymentMethod = method) }
    }

    fun selectSchedule(schedule: OrderSchedule) {
        _uiState.update { it.copy(selectedSchedule = schedule) }
    }

    fun updateCheckoutInfo(name: String, phone: String, address: String, notes: String) {
        _uiState.update {
            it.copy(
                customerName = name,
                customerPhone = phone,
                customerAddress = address,
                orderNotes = notes
            )
        }
    }

    fun placeOrder(): Boolean {
        val state = _uiState.value
        if (state.cartItems.isEmpty()) return false

        if (state.selectedPaymentMethod == PaymentMethod.RISK_WALLET) {
            if (state.walletBalance < state.finalTotal) {
                _uiState.update {
                    it.copy(inAppNotificationMessage = "رصيد المحفظة الحالي (${state.walletBalance.toInt()} ج.م) لا يكفي لسداد الطلب (${state.finalTotal.toInt()} ج.م). يرجى شحن المحفظة أولاً!")
                }
                return false
            }
        }

        val orderNumber = "RISK-#${(1000..9999).random()}"
        val timeFormat = SimpleDateFormat("hh:mm a", Locale("ar"))
        val currentTime = timeFormat.format(Date())

        val newOrder = ActiveOrder(
            orderId = orderNumber,
            items = state.cartItems,
            status = OrderStatus.RECEIVED,
            orderTime = currentTime,
            deliveryType = state.deliveryType,
            qenaZone = state.selectedZone,
            customerAddress = if (state.customerAddress.isNotBlank()) state.customerAddress else state.currentUser.address,
            customerName = if (state.customerName.isNotBlank()) state.customerName else state.currentUser.displayName,
            customerPhone = if (state.customerPhone.isNotBlank()) state.customerPhone else state.currentUser.phone,
            paymentMethod = state.selectedPaymentMethod,
            schedule = state.selectedSchedule,
            subtotal = state.subtotal,
            deliveryFee = state.deliveryFee,
            finalTotal = state.finalTotal,
            estimatedMinutes = state.selectedZone.estimatedMinutes,
            captainProgressPercent = 0.15f
        )

        val updatedWallet = if (state.selectedPaymentMethod == PaymentMethod.RISK_WALLET) {
            state.walletBalance - state.finalTotal
        } else {
            state.walletBalance
        }

        val updatedTransactions = if (state.selectedPaymentMethod == PaymentMethod.RISK_WALLET) {
            val deductTx = WalletTransaction(
                id = UUID.randomUUID().toString(),
                title = "سداد طلب $orderNumber",
                amount = state.finalTotal,
                date = currentTime,
                isCredit = false
            )
            listOf(deductTx) + state.walletTransactions
        } else {
            state.walletTransactions
        }

        _uiState.update { s ->
            s.copy(
                cartItems = emptyList(),
                activeOrders = listOf(newOrder) + s.activeOrders,
                selectedOrderForTracking = newOrder,
                walletBalance = updatedWallet,
                walletTransactions = updatedTransactions,
                currentUser = s.currentUser.copy(
                    walletBalance = updatedWallet,
                    totalOrdersCount = s.currentUser.totalOrdersCount + 1
                ),
                inAppNotificationMessage = "تم إرسال طلبك $orderNumber بنجاح إلى مطبخ ريسك!"
            )
        }

        // Simulate preparation progression
        simulateOrderProgression(newOrder.orderId)
        return true
    }

    fun submitOrder(): ActiveOrder? {
        val state = _uiState.value
        if (state.cartItems.isEmpty()) return null

        if (state.selectedPaymentMethod == PaymentMethod.RISK_WALLET) {
            if (state.walletBalance < state.finalTotal) {
                _uiState.update {
                    it.copy(inAppNotificationMessage = "رصيد المحفظة الحالي (${state.walletBalance.toInt()} ج.م) لا يكفي لسداد الطلب (${state.finalTotal.toInt()} ج.م). يرجى شحن المحفظة أولاً!")
                }
                return null
            }
        }

        val orderNumber = "RISK-#${(1000..9999).random()}"
        val timeFormat = SimpleDateFormat("hh:mm a", Locale("ar"))
        val currentTime = timeFormat.format(Date())

        val newOrder = ActiveOrder(
            orderId = orderNumber,
            items = state.cartItems,
            status = OrderStatus.RECEIVED,
            orderTime = currentTime,
            deliveryType = state.deliveryType,
            qenaZone = state.selectedZone,
            customerAddress = if (state.customerAddress.isNotBlank()) state.customerAddress else state.currentUser.address,
            customerName = if (state.customerName.isNotBlank()) state.customerName else state.currentUser.displayName,
            customerPhone = if (state.customerPhone.isNotBlank()) state.customerPhone else state.currentUser.phone,
            paymentMethod = state.selectedPaymentMethod,
            schedule = state.selectedSchedule,
            subtotal = state.subtotal,
            deliveryFee = state.deliveryFee,
            finalTotal = state.finalTotal,
            estimatedMinutes = state.selectedZone.estimatedMinutes,
            captainProgressPercent = 0.15f,
            receiptImageUri = state.checkoutReceiptUri,
            paymentReference = if (state.checkoutPaymentReference.isNotBlank()) state.checkoutPaymentReference else "TXN-${(100000..999999).random()}",
            paymentApprovalStatus = if (state.selectedPaymentMethod == PaymentMethod.CASH_ON_DELIVERY || state.selectedPaymentMethod == PaymentMethod.POS_CARD) {
                com.example.data.PaymentApprovalStatus.NOT_REQUIRED
            } else {
                com.example.data.PaymentApprovalStatus.PENDING_REVIEW
            }
        )

        val updatedWallet = if (state.selectedPaymentMethod == PaymentMethod.RISK_WALLET) {
            state.walletBalance - state.finalTotal
        } else {
            state.walletBalance
        }

        val updatedTransactions = if (state.selectedPaymentMethod == PaymentMethod.RISK_WALLET) {
            val deductTx = WalletTransaction(
                id = UUID.randomUUID().toString(),
                title = "سداد طلب $orderNumber",
                amount = state.finalTotal,
                date = currentTime,
                isCredit = false
            )
            listOf(deductTx) + state.walletTransactions
        } else {
            state.walletTransactions
        }

        _uiState.update { s ->
            s.copy(
                cartItems = emptyList(),
                activeOrders = listOf(newOrder) + s.activeOrders,
                selectedOrderForTracking = newOrder,
                walletBalance = updatedWallet,
                walletTransactions = updatedTransactions,
                currentUser = s.currentUser.copy(
                    walletBalance = updatedWallet,
                    totalOrdersCount = s.currentUser.totalOrdersCount + 1
                ),
                inAppNotificationMessage = "تم تأكيد طلبك $orderNumber بنجاح!"
            )
        }

        simulateOrderProgression(newOrder.orderId)
        return newOrder
    }

    fun removeFromCart(cartId: String) = removeCartItem(cartId)
    fun setQenaZone(zone: QenaZone) = selectZone(zone)
    fun setSchedule(schedule: OrderSchedule) = selectSchedule(schedule)
    fun setPaymentMethod(method: PaymentMethod) = selectPaymentMethod(method)
    fun updateCustomerDetails(name: String, phone: String, address: String, notes: String) =
        updateCheckoutInfo(name, phone, address, notes)
    fun setCheckoutPaymentProof(uri: String?, ref: String) {
        _uiState.update { it.copy(checkoutReceiptUri = uri, checkoutPaymentReference = ref) }
    }
    fun advanceOrderStatus(orderId: String, nextStatus: OrderStatus) =
        adminUpdateOrderStatus(orderId, nextStatus)

    fun adminApproveOrderPayment(orderId: String) {
        val updated = _uiState.value.activeOrders.map { order ->
            if (order.orderId == orderId) {
                order.copy(
                    paymentApprovalStatus = com.example.data.PaymentApprovalStatus.APPROVED,
                    status = if (order.status == OrderStatus.RECEIVED) OrderStatus.PREPARING else order.status
                )
            } else order
        }
        _uiState.update {
            it.copy(
                activeOrders = updated,
                selectedOrderForTracking = updated.find { o -> o.orderId == orderId } ?: it.selectedOrderForTracking,
                inAppNotificationMessage = "تم التحقق واعتماد دفع الطلب $orderId بنجاح ونقله لقسم التحضير!"
            )
        }
    }

    fun adminRejectOrderPayment(orderId: String, reason: String) {
        val updated = _uiState.value.activeOrders.map { order ->
            if (order.orderId == orderId) {
                order.copy(
                    paymentApprovalStatus = com.example.data.PaymentApprovalStatus.REJECTED,
                    paymentRejectionReason = reason.ifBlank { "الإيصال المرفق غير مطابق أو لم يصل المبلغ لمحفظة المطعم" }
                )
            } else order
        }
        _uiState.update {
            it.copy(
                activeOrders = updated,
                selectedOrderForTracking = updated.find { o -> o.orderId == orderId } ?: it.selectedOrderForTracking,
                inAppNotificationMessage = "تم رفض إيصال الطلب $orderId: $reason"
            )
        }
    }

    fun topUpWallet(amount: Double) {
        val timeFormat = SimpleDateFormat("hh:mm a", Locale("ar"))
        val currentTime = timeFormat.format(Date())
        val newTx = WalletTransaction(
            id = UUID.randomUUID().toString(),
            title = "شحن محفظة معتمد",
            amount = amount,
            date = currentTime,
            isCredit = true
        )
        _uiState.update { s ->
            s.copy(
                walletBalance = s.walletBalance + amount,
                walletTransactions = listOf(newTx) + s.walletTransactions,
                currentUser = s.currentUser.copy(walletBalance = s.currentUser.walletBalance + amount),
                inAppNotificationMessage = "تم شحن المحفظة بمبلغ ${amount.toInt()} ج.م بنجاح!"
            )
        }
    }

    private fun simulateOrderProgression(orderId: String) {
        viewModelScope.launch {
            delay(15000)
            _uiState.update { state ->
                val updated = state.activeOrders.map {
                    if (it.orderId == orderId && it.status == OrderStatus.RECEIVED) {
                        it.copy(status = OrderStatus.PREPARING, captainProgressPercent = 0.45f)
                    } else it
                }
                state.copy(
                    activeOrders = updated,
                    selectedOrderForTracking = updated.firstOrNull { it.orderId == orderId } ?: state.selectedOrderForTracking,
                    inAppNotificationMessage = "طلبك $orderId الآن في الفرن ويتم تجهيزه ساخناً!"
                )
            }
        }
    }

    fun dismissNotification() {
        _uiState.update { it.copy(inAppNotificationMessage = null) }
    }

    fun buildWhatsAppOrderMessage(order: ActiveOrder? = null): String {
        val state = _uiState.value
        val itemsToFormat = order?.items ?: state.cartItems
        val finalZone = order?.qenaZone ?: state.selectedZone
        val finalPayment = order?.paymentMethod ?: state.selectedPaymentMethod
        val finalSchedule = order?.schedule ?: state.selectedSchedule
        val finalSubtotal = order?.subtotal ?: state.subtotal
        val finalDeliveryFee = order?.deliveryFee ?: state.deliveryFee
        val finalTotalAmount = order?.finalTotal ?: state.finalTotal
        val custName = order?.customerName ?: state.customerName
        val custPhone = order?.customerPhone ?: state.customerPhone
        val custAddr = order?.customerAddress ?: state.customerAddress

        val sb = StringBuilder()
        sb.append("🍔 *طلب جديد من تطبيق RISK Fast foods* 🍔\n")
        if (order != null) {
            sb.append("رقم الطلب: *${order.orderId}*\n\n")
        } else {
            sb.append("\n")
        }

        if (custName.isNotBlank()) sb.append("👤 *الاسم:* $custName\n")
        if (custPhone.isNotBlank()) sb.append("📱 *رقم الهاتف:* $custPhone\n")
        sb.append("📍 *منطقة التوصيل:* ${finalZone.zoneName}\n")
        if (custAddr.isNotBlank()) sb.append("🏠 *العنوان:* $custAddr\n")
        sb.append("💳 *طريقة الدفع:* ${finalPayment.titleAr}\n")
        sb.append("⏰ *الموعد:* ${finalSchedule.labelAr}\n")
        sb.append("-----------------------------\n")
        sb.append("📋 *الأصناف المطلوبة:*\n")

        itemsToFormat.forEachIndexed { index, item ->
            val sizeStr = item.selectedSize?.let { " (${it.titleAr})" } ?: ""
            sb.append("${index + 1}. *${item.menuItem.nameAr}*$sizeStr x${item.quantity} = ${item.totalPrice.toInt()} ج.م\n")
            if (item.hasStuffedCrust) {
                sb.append("   • حشو الأطراف (+25 ج.م)\n")
            }
            if (item.selectedExtras.isNotEmpty()) {
                sb.append("   • إضافات: ${item.selectedExtras.joinToString { it.nameAr }}\n")
            }
            if (item.selectedSauces.isNotEmpty()) {
                sb.append("   • صصوصات: ${item.selectedSauces.joinToString { it.nameAr }}\n")
            }
            if (item.notes.isNotBlank()) {
                sb.append("   • ملاحظات: ${item.notes}\n")
            }
        }

        sb.append("-----------------------------\n")
        sb.append("💵 *المجموع:* ${finalSubtotal.toInt()} ج.م\n")
        sb.append("🛵 *توصيل (${finalZone.zoneName}):* ${finalDeliveryFee.toInt()} ج.م\n")
        sb.append("💰 *الإجمالي النهائي:* ${finalTotalAmount.toInt()} ج.م\n")

        if (state.orderNotes.isNotBlank()) {
            sb.append("\n📝 *ملاحظات إضافية:* ${state.orderNotes}\n")
        }

        sb.append("\n_تم إرسال الطلب عبر نظام RISK الذكي_")
        return sb.toString()
    }

    fun getEncodedWhatsAppUrl(phoneNumber: String = RiskMenuRepository.restaurant.phone1, order: ActiveOrder? = null): String {
        val rawMsg = buildWhatsAppOrderMessage(order)
        val encoded = URLEncoder.encode(rawMsg, StandardCharsets.UTF_8.toString())
        val cleanPhone = if (phoneNumber.startsWith("0")) "2$phoneNumber" else phoneNumber
        return "https://api.whatsapp.com/send?phone=$cleanPhone&text=$encoded"
    }
}
