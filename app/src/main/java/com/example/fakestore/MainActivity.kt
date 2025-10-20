package com.example.fakestore

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.fakestore.auth.AuthRepositoryImpl
import com.example.fakestore.database.createDatabase
import com.example.fakestore.products.ProductRepositoryImpl
import com.example.fakestore.products.ProductService
import com.example.fakestore.rest_service.RestClient
import com.example.fakestore.ui.theme.FakeStoreTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val roomDatabase = createDatabase(this.applicationContext)

        /// Auth Repository
        val authRepository = AuthRepositoryImpl()

        /// Product setup
        val productService = RestClient.create(ProductService::class.java)
        val productRepository = ProductRepositoryImpl(
            service = productService,
            dao = roomDatabase.productDao()
        )

        setContent {
            FakeStoreTheme {
//                LoginScreen(authRepository)
//                SignupScreen(authRepository)
//                ProductScreen(productRepository)
                AppRouter(
                    authRepository,
                    productRepository
                )
            }
        }
    }
}
