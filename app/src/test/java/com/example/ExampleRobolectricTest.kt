package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.ItemSize
import com.example.data.MenuCategory
import com.example.data.OrderStatus
import com.example.data.QenaZone
import com.example.data.RiskMenuRepository
import com.example.viewmodel.RiskRestaurantViewModel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class ExampleRobolectricTest {

    @Test
    fun `read app name from context`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val appName = context.getString(R.string.app_name)
        assertEquals("RISK Fast Foods", appName)
    }

    @Test
    fun `verify menu items loaded properly`() {
        val pizzas = RiskMenuRepository.getItemsByCategory(MenuCategory.PIZZA)
        assertTrue(pizzas.isNotEmpty())

        val riskPizza = pizzas.find { it.id == "p_risk_special" }
        assertNotNull(riskPizza)
        assertEquals(170.0, riskPizza!!.sizePrices[ItemSize.SMALL] ?: 0.0, 0.01)
        assertEquals(220.0, riskPizza.sizePrices[ItemSize.MEDIUM] ?: 0.0, 0.01)
        assertEquals(270.0, riskPizza.sizePrices[ItemSize.LARGE] ?: 0.0, 0.01)

        val crepes = RiskMenuRepository.getItemsByCategory(MenuCategory.CREPE)
        assertTrue(crepes.isNotEmpty())
    }

    @Test
    fun `verify cart and total calculations with Mansheya delivery`() {
        val viewModel = RiskRestaurantViewModel(ApplicationProvider.getApplicationContext())
        val pizza = RiskMenuRepository.menuItems.first { it.id == "p_margherita" }

        viewModel.addToCart(
            item = pizza,
            selectedSize = ItemSize.MEDIUM,
            stuffedCrust = true,
            selectedExtras = emptyList(),
            selectedSauces = emptyList(),
            notes = "بدون بصل",
            quantity = 2
        )

        val state = viewModel.uiState.value
        assertEquals(1, state.cartItems.size)
        assertEquals(2, state.cartItemsCount)
        // Base medium: 100 + stuffed crust: 25 = 125 * 2 = 250
        assertEquals(250.0, state.subtotal, 0.01)
        // With Al-Mansheya delivery fee (10): 260
        assertEquals(260.0, state.finalTotal, 0.01)
    }

    @Test
    fun `verify order submission and tracking`() {
        val viewModel = RiskRestaurantViewModel(ApplicationProvider.getApplicationContext())
        val pizza = RiskMenuRepository.menuItems.first { it.id == "p_margherita" }

        viewModel.addToCart(
            item = pizza,
            selectedSize = ItemSize.SMALL,
            stuffedCrust = false,
            selectedExtras = emptyList(),
            selectedSauces = emptyList(),
            notes = "",
            quantity = 1
        )

        val newOrder = viewModel.submitOrder()
        assertNotNull(newOrder)
        assertEquals(OrderStatus.RECEIVED, newOrder!!.status)
        assertTrue(viewModel.uiState.value.cartItems.isEmpty())
        assertNotNull(viewModel.uiState.value.selectedOrderForTracking)
    }

    @Test
    fun `verify wallet top up strict deposit without fake cashback`() {
        val viewModel = RiskRestaurantViewModel(ApplicationProvider.getApplicationContext())
        assertEquals(0.0, viewModel.uiState.value.walletBalance, 0.01)
        viewModel.topUpWallet(100.0)
        // Exact 100 added, no fake cashback
        assertEquals(100.0, viewModel.uiState.value.walletBalance, 0.01)
    }
}
