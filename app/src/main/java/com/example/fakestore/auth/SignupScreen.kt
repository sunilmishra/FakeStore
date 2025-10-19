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
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.fakestore.ResultState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignupScreen(repository: AuthRepository) {

    val factory = remember { SignupViewModelProvider(repository) }
    val viewModel: SignupViewModel = viewModel(factory = factory)

    val state by viewModel.state.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                colors = topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = {
                    Text("Signup")
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            OutlinedTextField(
                value = state.email,
                onValueChange = { viewModel.onEvent(SignupEvent.Input.EnterName(it)) },
                label = { Text("Enter Name") },
                singleLine = true,
                isError = state.nameError != null,
                modifier = Modifier.fillMaxWidth()
            )
            if (state.nameError != null) {
                Text(state.nameError ?: "", color = Color.Red)
            }
            OutlinedTextField(
                value = state.email,
                onValueChange = { viewModel.onEvent(SignupEvent.Input.EnterEmail(it)) },
                label = { Text("Enter Email") },
                singleLine = true,
                isError = state.emailError != null,
                modifier = Modifier.fillMaxWidth()
            )
            if (state.emailError != null) {
                Text(state.emailError ?: "", color = Color.Red)
            }
            OutlinedTextField(
                value = state.password,
                onValueChange = { viewModel.onEvent(SignupEvent.Input.EnterPassword(it)) },
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
                onClick = { viewModel.onEvent(SignupEvent.Signup) },
            ) {
                if (state.resultState == ResultState.Processing) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp), color = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    Text("Signup".uppercase(), fontWeight = FontWeight.W600)
                }
            }
        }
    }

}