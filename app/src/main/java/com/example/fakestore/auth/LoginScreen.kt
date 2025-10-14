package com.example.fakestore.auth

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.fakestore.ResultState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(viewModel: LoginViewModel) {
    val state by viewModel.loginState.collectAsState()
    val snackBarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is LoginEffect.Failed -> {
                    snackBarHostState.showSnackbar(effect.message)
                }

                is LoginEffect.Success -> {
                    snackBarHostState.showSnackbar("Login: ${effect.token}")
                }
            }
        }
    }

    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackBarHostState)
        },
        topBar = {
            TopAppBar(
                colors = topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = { Text("Login") },
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            OutlinedTextField(
                value = state.email,
                onValueChange = { viewModel.onEvent(LoginEvent.Input.EnterEmail(it)) },
                label = { Text("Enter Email") },
                singleLine = true,
                isError = state.emailError != null,
                modifier = Modifier.fillMaxWidth()
            )
            if (state.emailError != null) {
                Text(state.emailError ?: "", color = Color.Red)
            }
            Spacer(modifier = Modifier.size(16.dp))
            OutlinedTextField(
                value = state.password,
                onValueChange = { viewModel.onEvent(LoginEvent.Input.EnterPassword(it)) },
                label = { Text("Enter password") },
                singleLine = true,
                isError = state.passwordError != null,
                modifier = Modifier.fillMaxWidth()
            )
            if (state.passwordError != null) {
                Text(state.passwordError ?: "", color = Color.Red)
            }
            Spacer(modifier = Modifier.size(16.dp))
            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = { viewModel.onEvent(LoginEvent.SignIn) },
            ) {
                if (state.resultState == ResultState.Processing) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp), color = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    Text("Login".uppercase(), fontWeight = FontWeight.W600)
                }
            }
        }
    }
}
