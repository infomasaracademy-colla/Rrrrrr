package com.example

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DeliveryDining
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.RestaurantMenu
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.RiskMenuRepository
import com.example.ui.components.ItemCustomizationSheet
import com.example.ui.components.RiskTopBar
import com.example.ui.screens.AccountScreen
import com.example.ui.screens.AdminDashboardScreen
import com.example.ui.screens.CartScreen
import com.example.ui.screens.FavoritesScreen
import com.example.ui.screens.MealBuilderScreen
import com.example.ui.screens.MenuScreen
import com.example.ui.screens.OrderTrackingScreen
import com.example.ui.screens.OriginalMenuBottomSheet
import com.example.ui.screens.PaymentUploadScreen
import com.example.ui.screens.RestaurantInfoScreen
import com.example.ui.screens.SmartAiScreen
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.theme.RiskBrightRed
import com.example.ui.theme.RiskDarkRed
import com.example.ui.theme.RiskGold
import com.example.ui.theme.RiskPrimaryRed
import com.example.util.configureAppCheck
import com.example.viewmodel.RiskRestaurantViewModel

enum class MainTab(val titleAr: String) {
    MENU("المنيو"),
    MEAL_BUILDER("اصنع وجبتك"),
    TRACKING("التتبع"),
    CART("السلة"),
    ACCOUNT("حسابي"),
    SMART_AI("ريسك AI"),
    INFO("المطعم")
}

class MainActivity : ComponentActivity() {

    private val viewModel: RiskRestaurantViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Initialize Firebase App Check Foundation before setContent or ViewModels operations
        configureAppCheck(this, intent)
        enableEdgeToEdge()

        setContent {
            MyApplicationTheme {
                CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
                    RiskApp(viewModel = viewModel, onCallRestaurant = {
                        val dialIntent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${RiskMenuRepository.restaurant.phone1}"))
                        startActivity(dialIntent)
                    })
                }
            }
        }
    }
}

@Composable
fun RiskApp(
    viewModel: RiskRestaurantViewModel,
    onCallRestaurant: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var currentTab by rememberSaveable { mutableStateOf(MainTab.MENU) }

    // Intercept back button to return to Menu or close modals
    BackHandler(enabled = uiState.showAdminDashboard || uiState.showPaymentUploadScreen || currentTab != MainTab.MENU) {
        when {
            uiState.showAdminDashboard -> viewModel.closeAdminDashboard()
            uiState.showPaymentUploadScreen -> viewModel.closePaymentUploadScreen()
            else -> currentTab = MainTab.MENU
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            RiskTopBar(
                cartCount = uiState.cartItemsCount,
                onCartClick = { currentTab = MainTab.CART },
                onCallClick = onCallRestaurant,
                onOpenMenuGallery = { viewModel.setOriginalMenuVisible(true) },
                onAiClick = { currentTab = MainTab.SMART_AI },
                onLogoClick = { currentTab = MainTab.INFO }
            )
        },
        bottomBar = {
            NavigationBar(
                tonalElevation = 8.dp
            ) {
                // 1. Menu tab
                NavigationBarItem(
                    selected = currentTab == MainTab.MENU,
                    onClick = { currentTab = MainTab.MENU },
                    icon = { Icon(Icons.Default.RestaurantMenu, contentDescription = null) },
                    label = {
                        Text(
                            MainTab.MENU.titleAr,
                            fontWeight = if (currentTab == MainTab.MENU) FontWeight.Bold else FontWeight.Normal,
                            fontSize = 11.sp
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = RiskBrightRed,
                        selectedTextColor = RiskBrightRed,
                        indicatorColor = RiskPrimaryRed.copy(alpha = 0.15f)
                    ),
                    modifier = Modifier.testTag("tab_menu")
                )

                // 2. Meal Builder tab ("اصنع وجبتك")
                NavigationBarItem(
                    selected = currentTab == MainTab.MEAL_BUILDER,
                    onClick = { currentTab = MainTab.MEAL_BUILDER },
                    icon = { Icon(Icons.Default.AutoAwesome, contentDescription = null) },
                    label = {
                        Text(
                            MainTab.MEAL_BUILDER.titleAr,
                            fontWeight = if (currentTab == MainTab.MEAL_BUILDER) FontWeight.Bold else FontWeight.Normal,
                            fontSize = 11.sp
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = RiskBrightRed,
                        selectedTextColor = RiskBrightRed,
                        indicatorColor = RiskPrimaryRed.copy(alpha = 0.15f)
                    ),
                    modifier = Modifier.testTag("tab_meal_builder")
                )

                // 3. Live Order Tracking tab with badge
                NavigationBarItem(
                    selected = currentTab == MainTab.TRACKING,
                    onClick = { currentTab = MainTab.TRACKING },
                    icon = {
                        BadgedBox(
                            badge = {
                                if (uiState.activeOrders.isNotEmpty()) {
                                    Badge(
                                        containerColor = RiskBrightRed,
                                        contentColor = Color.White
                                    ) {
                                        Text(text = "${uiState.activeOrders.size}", fontWeight = FontWeight.Bold)
                                    }
                                }
                            }
                        ) {
                            Icon(Icons.Default.DeliveryDining, contentDescription = null)
                        }
                    },
                    label = {
                        Text(
                            MainTab.TRACKING.titleAr,
                            fontWeight = if (currentTab == MainTab.TRACKING) FontWeight.Bold else FontWeight.Normal,
                            fontSize = 11.sp
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = RiskBrightRed,
                        selectedTextColor = RiskBrightRed,
                        indicatorColor = RiskPrimaryRed.copy(alpha = 0.15f)
                    ),
                    modifier = Modifier.testTag("tab_tracking")
                )

                // 4. Cart tab with badge
                NavigationBarItem(
                    selected = currentTab == MainTab.CART,
                    onClick = { currentTab = MainTab.CART },
                    icon = {
                        BadgedBox(
                            badge = {
                                if (uiState.cartItemsCount > 0) {
                                    Badge(
                                        containerColor = RiskBrightRed,
                                        contentColor = Color.White
                                    ) {
                                        Text(text = "${uiState.cartItemsCount}", fontWeight = FontWeight.Bold)
                                    }
                                }
                            }
                        ) {
                            Icon(Icons.Default.ShoppingCart, contentDescription = null)
                        }
                    },
                    label = {
                        Text(
                            MainTab.CART.titleAr,
                            fontWeight = if (currentTab == MainTab.CART) FontWeight.Bold else FontWeight.Normal,
                            fontSize = 11.sp
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = RiskBrightRed,
                        selectedTextColor = RiskBrightRed,
                        indicatorColor = RiskPrimaryRed.copy(alpha = 0.15f)
                    ),
                    modifier = Modifier.testTag("tab_cart")
                )

                // 5. My Account tab ("حسابي") - Replaced Wallet with Account, Wallet embedded inside
                NavigationBarItem(
                    selected = currentTab == MainTab.ACCOUNT,
                    onClick = { currentTab = MainTab.ACCOUNT },
                    icon = { Icon(Icons.Default.Person, contentDescription = null) },
                    label = {
                        Text(
                            MainTab.ACCOUNT.titleAr,
                            fontWeight = if (currentTab == MainTab.ACCOUNT) FontWeight.Bold else FontWeight.Normal,
                            fontSize = 11.sp
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = RiskBrightRed,
                        selectedTextColor = RiskBrightRed,
                        indicatorColor = RiskPrimaryRed.copy(alpha = 0.15f)
                    ),
                    modifier = Modifier.testTag("tab_account")
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (currentTab) {
                MainTab.MENU -> {
                    MenuScreen(
                        uiState = uiState,
                        onCategorySelect = { viewModel.selectCategory(it) },
                        onSearchChange = { viewModel.updateSearchQuery(it) },
                        onItemClick = { viewModel.openCustomizationDialog(it) },
                        onFavoriteToggle = { viewModel.toggleFavorite(it) },
                        onOpenOriginalMenu = { viewModel.setOriginalMenuVisible(true) },
                        onCallClick = onCallRestaurant
                    )
                }

                MainTab.MEAL_BUILDER -> {
                    MealBuilderScreen(
                        viewModel = viewModel,
                        onNavigateToCart = { currentTab = MainTab.CART }
                    )
                }

                MainTab.TRACKING -> {
                    OrderTrackingScreen(
                        uiState = uiState,
                        viewModel = viewModel,
                        onNavigateToMenu = { currentTab = MainTab.MENU }
                    )
                }

                MainTab.CART -> {
                    CartScreen(
                        uiState = uiState,
                        viewModel = viewModel,
                        onNavigateToMenu = { currentTab = MainTab.MENU },
                        onNavigateToTracking = { currentTab = MainTab.TRACKING }
                    )
                }

                MainTab.ACCOUNT -> {
                    AccountScreen(
                        uiState = uiState,
                        viewModel = viewModel,
                        onNavigateToPaymentUpload = { viewModel.openPaymentUploadScreen() },
                        onNavigateToAdmin = { viewModel.triggerSecretDashboardFromGesture() }
                    )
                }

                MainTab.SMART_AI -> {
                    SmartAiScreen(
                        viewModel = viewModel,
                        onNavigateToCart = { currentTab = MainTab.CART }
                    )
                }

                MainTab.INFO -> {
                    RestaurantInfoScreen(
                        onOpenOriginalMenu = { viewModel.setOriginalMenuVisible(true) }
                    )
                }
            }

            // Real-Time Animated Notification Banner
            AnimatedVisibility(
                visible = uiState.inAppNotificationMessage != null,
                enter = slideInVertically() + fadeIn(),
                exit = slideOutVertically() + fadeOut(),
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(horizontal = 16.dp, vertical = 10.dp)
            ) {
                uiState.inAppNotificationMessage?.let { msg ->
                    Card(
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1E1E)),
                        border = BorderStroke(1.5.dp, RiskGold),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                modifier = Modifier.weight(1f),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Surface(
                                    color = RiskGold.copy(alpha = 0.2f),
                                    shape = RoundedCornerShape(8.dp),
                                    modifier = Modifier.size(32.dp)
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Icon(
                                            imageVector = Icons.Default.Notifications,
                                            contentDescription = null,
                                            tint = RiskGold,
                                            modifier = Modifier.size(18.dp)
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier.width(10.dp))
                                Text(
                                    text = msg,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White,
                                    lineHeight = 16.sp
                                )
                            }

                            IconButton(
                                onClick = { viewModel.dismissNotification() },
                                modifier = Modifier.size(28.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "إغلاق الإشعار",
                                    tint = Color.White.copy(alpha = 0.7f),
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                    }
                }
            }

            // Customization Modal Sheet
            uiState.selectedItemForCustomization?.let { item ->
                ItemCustomizationSheet(
                    item = item,
                    onDismiss = { viewModel.closeCustomizationDialog() },
                    onAddToCart = { size, stuffedCrust, extras, sauces, notes, quantity ->
                        viewModel.addToCart(
                            item = item,
                            selectedSize = size,
                            stuffedCrust = stuffedCrust,
                            selectedExtras = extras,
                            selectedSauces = sauces,
                            notes = notes,
                            quantity = quantity
                        )
                    }
                )
            }

            // Original Physical Menu Gallery Bottom Sheet
            if (uiState.showOriginalMenuSheet) {
                OriginalMenuBottomSheet(
                    initialPage = uiState.originalMenuSelectedPage,
                    onDismiss = { viewModel.setOriginalMenuVisible(false) }
                )
            }

            // Hidden Admin Dashboard Modal Screen
            if (uiState.showAdminDashboard) {
                AdminDashboardScreen(
                    uiState = uiState,
                    viewModel = viewModel,
                    onNavigateBack = { viewModel.closeAdminDashboard() }
                )
            }

            // Payment Upload & AI Inspection Screen
            if (uiState.showPaymentUploadScreen) {
                PaymentUploadScreen(
                    uiState = uiState,
                    viewModel = viewModel,
                    onNavigateBack = { viewModel.closePaymentUploadScreen() }
                )
            }
        }
    }
}
