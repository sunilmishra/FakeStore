package com.example.fakestore

import androidx.compose.runtime.Composable
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.example.fakestore.auth.AuthRepository
import com.example.fakestore.products.ProductDetailsScreen
import com.example.fakestore.products.ProductRepository
import com.example.fakestore.products.ProductScreen
import kotlinx.serialization.Serializable

sealed interface ScreenKey : NavKey {

    @Serializable
    data object Product : ScreenKey

    @Serializable
    data class ProductDetails(val id: Int) : ScreenKey
}

@Composable
fun AppRouter(
    authRepository: AuthRepository,
    productRepository: ProductRepository
) {
    val backStack: NavBackStack<NavKey> = rememberNavBackStack(ScreenKey.Product)
    NavDisplay(
        backStack,
        // Specify what should happen when the user goes back
        onBack = { backStack.removeLastOrNull() },
        entryProvider = entryProvider {

            /// Product Screen
            entry<ScreenKey.Product> {
                ProductScreen(
                    repository = productRepository,
                    onProductClick = {
                        backStack.add(ScreenKey.ProductDetails(it))
                    },
                )
            }

            /// Product Details Screen
            entry<ScreenKey.ProductDetails> {
                ProductDetailsScreen(
                    it.id,
                    navigateBack = {
                        backStack.removeLastOrNull()
                    }
                )
            }
        }
    )
}