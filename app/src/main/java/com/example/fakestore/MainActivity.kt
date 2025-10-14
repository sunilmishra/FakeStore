package com.example.fakestore

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.ExperimentalMaterial3Api
import com.example.fakestore.auth.AuthRepositoryImpl
import com.example.fakestore.auth.LoginScreen
import com.example.fakestore.auth.LoginViewModel
import com.example.fakestore.ui.theme.FakeStoreTheme

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val viewModel = LoginViewModel(AuthRepositoryImpl())
        setContent {
            FakeStoreTheme {
                LoginScreen(viewModel)
            }
        }
    }
}
