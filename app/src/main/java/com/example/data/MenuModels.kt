package com.example.data

enum class ItemSize(val titleAr: String, val shortCode: String) {
    SMALL("صغير", "S"),
    MEDIUM("وسط", "M"),
    LARGE("كبير", "L")
}

enum class MenuCategory(val titleAr: String, val iconName: String) {
    ALL("الكل", "restaurant"),
    PIZZA("البيتزا الإيطالي", "local_pizza"),
    FETEER_ORIENTAL("فطير شرقي", "bakery_dining"),
    FETEER_SALTY("فطير حادق", "lunch_dining"),
    FETEER_SWEET("فطائر حلو", "cake"),
    SAROUKH("الصاروخ", "fastfood"),
    HAWAWSHI_ITALIAN("حواوشي إيطالي", "local_pizza"),
    HAWAWSHI_BALADI("حواوشي بلدي", "dinner_dining"),
    SYRIAN("السوري", "kebab_dining"),
    CREPE("الكريب", "lunch_dining"),
    PASTA("المكرونات", "ramen_dining"),
    EXTRAS_SAUCES("الإضافات والصوصات", "local_dining")
}

data class ExtraOption(
    val id: String,
    val nameAr: String,
    val price: Double
)

data class SauceOption(
    val id: String,
    val nameAr: String,
    val price: Double
)

data class MenuItem(
    val id: String,
    val nameAr: String,
    val category: MenuCategory,
    val descriptionAr: String,
    val priceSingle: Double? = null,
    val sizePrices: Map<ItemSize, Double> = emptyMap(),
    val isRiskSpecial: Boolean = false,
    val allowsStuffedCrust: Boolean = false,
    val isAvailable: Boolean = true
) {
    fun getStartingPrice(): Double {
        if (priceSingle != null) return priceSingle
        return sizePrices.values.minOrNull() ?: 0.0
    }
}

data class CartItem(
    val cartId: String,
    val menuItem: MenuItem,
    val selectedSize: ItemSize? = null,
    val hasStuffedCrust: Boolean = false,
    val stuffedCrustType: String? = null,
    val selectedExtras: List<ExtraOption> = emptyList(),
    val selectedSauces: List<SauceOption> = emptyList(),
    val notes: String = "",
    val quantity: Int = 1,
    val isCustomMeal: Boolean = false
) {
    val singleItemPrice: Double
        get() {
            var price = if (selectedSize != null) {
                menuItem.sizePrices[selectedSize] ?: menuItem.getStartingPrice()
            } else {
                menuItem.priceSingle ?: menuItem.getStartingPrice()
            }
            if (hasStuffedCrust) price += 25.0
            price += selectedExtras.sumOf { it.price }
            price += selectedSauces.sumOf { it.price }
            return price
        }

    val totalPrice: Double
        get() = singleItemPrice * quantity
}

enum class DeliveryType(val titleAr: String) {
    DELIVERY("توصيل للمنازل (دليفري)"),
    TAKEAWAY("استلام ساخن من المطعم (تيك أواي)")
}

enum class QenaZone(val zoneName: String, val deliveryFee: Double, val estimatedMinutes: Int) {
    AL_MANSHEYA("المنشية (أمام كافتيريا الزهراء)", 10.0, 15),
    AL_MASAKEN("المساكن الشعبية وميدان المحطة", 15.0, 20),
    UNIVERSITY("جامعة جنوب الوادي ومساكن الطلاب", 20.0, 25),
    AL_SHUOON("منطقة الشئون ومستشفى قنا العام", 15.0, 20),
    AL_NAHAL("حي النحال وحوض 10", 15.0, 25),
    SIDI_ABD_EL_RAHIM("سيدي عبد الرحيم القنائي وما حوله", 15.0, 20),
    AL_HASAWEYA("الحصواية وكوبري دندرة", 20.0, 30),
    AL_MAANA("حي المعنا والمدخل الشمالي", 25.0, 35),
    DANDARA("مدينة دندرة والقرى المجاورة", 30.0, 40)
}

enum class PaymentMethod(val titleAr: String, val subtitleAr: String, val iconName: String) {
    CASH_ON_DELIVERY("كاش عند الاستلام", "ادفع نقداً عند استلام وجبتك", "payments"),
    VODAFONE_CASH("فودافون كاش (01019043229)", "تحويل فوري لمحفظة المطعم مع تأكيد سريع", "phone_android"),
    INSTAPAY("إنستا باي (InstaPay)", "تحويل لحظي عبر شبكة المدفوعات اللحظية", "account_balance"),
    RISK_WALLET("محفظة ريسك (Risk Wallet)", "خصم فوري من رصيدك المشحون مع كاش باك", "wallet"),
    POS_CARD("فيزا / كارت عند الاستلام", "الكابتن يحمل ماكينة دفع إلكتروني POS", "credit_card")
}

enum class OrderSchedule(val labelAr: String) {
    NOW("الآن في أسرع وقت (تسوية فورية)"),
    IN_30_MIN("خلال 30 دقيقة (عند الخروج)"),
    IN_1_HOUR("خلال ساعة (وقت الغداء/العشاء)"),
    EVENING_GATHERING("سهرة الليلة ومباريات الكرة (9:00 مساءً)")
}

enum class OrderStatus(val titleAr: String, val descriptionAr: String, val progress: Float) {
    RECEIVED("تم استلام الطلب", "طلبك وصل مطبخ ريسك وجاري مراجعته", 0.25f),
    PREPARING("قيد التحضير في الفرن", "فرد العجين، وضع الحشو، والطهي في الفرن الحجري", 0.55f),
    ON_THE_WAY("مع كابتن التوصيل / جاهز للاستلام", "الكابتن يتحرك الآن في شوارع قنا باتجاهك", 0.85f),
    DELIVERED("تم التسليم بنجاح", "بالهناء والشفاء! استمتع بأشهى أكل من ريسك", 1.0f)
}

enum class PaymentApprovalStatus(val labelAr: String) {
    PENDING_REVIEW("قيد مراجعة إيصال الدفع ⏳"),
    APPROVED("تم التحقق واعتماد الدفع ✓"),
    REJECTED("مرفوض - الإيصال غير مطابق ✗"),
    NOT_REQUIRED("دفع نقدي عند الاستلام")
}

data class ActiveOrder(
    val orderId: String,
    val items: List<CartItem>,
    var status: OrderStatus = OrderStatus.RECEIVED,
    val orderTime: String,
    val deliveryType: DeliveryType,
    val qenaZone: QenaZone,
    val customerAddress: String,
    val customerName: String,
    val customerPhone: String,
    val paymentMethod: PaymentMethod,
    val schedule: OrderSchedule,
    val subtotal: Double,
    val deliveryFee: Double,
    val finalTotal: Double,
    val captainName: String = "كابتن أحمد عبد الرحيم (ريسك)",
    val captainPhone: String = "01019043229",
    val estimatedMinutes: Int = 25,
    val captainProgressPercent: Float = 0.2f,
    val receiptImageUri: String? = null,
    val paymentReference: String? = null,
    val paymentApprovalStatus: PaymentApprovalStatus = if (paymentMethod == PaymentMethod.CASH_ON_DELIVERY || paymentMethod == PaymentMethod.POS_CARD) PaymentApprovalStatus.NOT_REQUIRED else PaymentApprovalStatus.PENDING_REVIEW,
    val paymentRejectionReason: String? = null
)

data class WalletTransaction(
    val id: String,
    val title: String,
    val amount: Double,
    val date: String,
    val isCredit: Boolean
)

data class RestaurantContact(
    val name: String = "RISK Fast foods",
    val slogan: String = "Since 2025",
    val location: String = "قنا - المنشية - أمام كافتيريا الزهراء",
    val phone1: String = "01019043229",
    val phone2: String = "01101137601",
    val workingHours: String = "يومياً من 12:00 ظهراً حتى 3:00 صباحاً",
    val deliveryAvailable: Boolean = true
)

enum class UserRole(val titleAr: String) {
    CUSTOMER("عميل"),
    SUPERVISOR("مشرف ريسك"),
    OWNER("المالك الرسمي (Owner)")
}

data class AppUser(
    val uid: String = "guest_user",
    val email: String = "guest@riskfoods.com",
    val displayName: String = "عميل ريسك المميّز",
    val photoUrl: String? = null,
    val phone: String = "01019043229",
    val address: String = "قنا - المنشية",
    val walletBalance: Double = 0.0,
    val role: UserRole = UserRole.CUSTOMER,
    val isBanned: Boolean = false,
    val banReason: String? = null,
    val appealMessage: String? = null,
    val hasPendingAppeal: Boolean = false,
    val joinedDate: String = "أكتوبر 2026",
    val totalOrdersCount: Int = 1
)

enum class AiVerificationStatus(val labelAr: String) {
    PENDING("قيد الفحص الذكي"),
    APPROVED_AUTOMATIC("معتمد تلقائياً بالذكاء الاصطناعي"),
    FLAGGED_FRAUD("مشتبه به - محظور لاشتباه في التزوير"),
    MANUALLY_APPROVED("معتمد يدوياً من المالك"),
    MANUALLY_REJECTED("مرفوض ومحظور نهائياً")
}

data class PaymentReceiptRequest(
    val id: String = "",
    val userId: String = "",
    val userName: String = "",
    val userEmail: String = "",
    val userPhone: String = "",
    val method: PaymentMethod = PaymentMethod.VODAFONE_CASH,
    val targetAccount: String = "01019043229",
    val amount: Double = 0.0,
    val referenceNumber: String = "",
    val dateStr: String = "",
    val receiptImageUri: String? = null,
    val isSampleFake: Boolean = false,
    val status: AiVerificationStatus = AiVerificationStatus.PENDING,
    val aiConfidence: Float = 0f,
    val aiExplanation: String = "",
    val reviewedBy: String? = null,
    val timestamp: Long = System.currentTimeMillis()
)

data class MenuPriceOverride(
    val itemId: String = "",
    val priceSingle: Double? = null,
    val sizePrices: Map<String, Double> = emptyMap(),
    val isAvailable: Boolean = true
)

data class RestaurantConfig(
    val ownerUserId: String? = null,
    val ownerEmail: String? = null,
    val ownerName: String? = null,
    val supervisorUids: List<String> = emptyList(),
    val supervisorSecretCode: String = "RISK2025"
)

