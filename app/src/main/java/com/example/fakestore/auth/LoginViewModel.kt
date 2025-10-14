package com.example.fakestore.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fakestore.ResultState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

// Login Screen State
data class LoginState(
    val email: String = "",
    val password: String = "",
    val emailError: String? = null,
    val passwordError: String? = null,

    val resultState: ResultState<String> = ResultState.Idle
)

/// Login Screen Actions
sealed class LoginEvent {
    sealed class Input {
        data class EnterEmail(val email: String) : LoginEvent()
        data class EnterPassword(val password: String) : LoginEvent()
    }

    data object SignIn : LoginEvent()
}

/// One-time effects: navigation, snack bars, etc.
sealed class LoginEffect {
    data class Success(val token: String) : LoginEffect()
    data class Failed(val message: String) : LoginEffect()
}

/// Login View Model
class LoginViewModel(private val repository: AuthRepository) : ViewModel() {
    private var _loginState = MutableStateFlow(LoginState())
    val loginState: StateFlow<LoginState> = _loginState

    private val _effect = MutableSharedFlow<LoginEffect>()
    val effect: SharedFlow<LoginEffect> = _effect.asSharedFlow()

    /// Event Handler
    fun onEvent(uiEvent: LoginEvent) {
        when (uiEvent) {
            is LoginEvent.Input.EnterEmail -> {
                _loginState.update { it.copy(email = uiEvent.email, emailError = null) }
            }

            is LoginEvent.Input.EnterPassword -> {
                _loginState.update { it.copy(password = uiEvent.password, passwordError = null) }
            }

            is LoginEvent.SignIn -> {
                signIn()
            }
        }
    }

    /// Sign In
    private fun signIn() {
        viewModelScope.launch {
            val myEmail = _loginState.value.email
            val myPassword = _loginState.value.password

            /// Validate Email and Password
            val emailResult = Validator.validateEmail(myEmail)
            val passwordResult = Validator.validatePassword(myPassword)
            if (!emailResult.successful || !passwordResult.successful) {
                _loginState.update {
                    it.copy(
                        emailError = emailResult.errorMessage,
                        passwordError = passwordResult.errorMessage
                    )
                }
                return@launch
            }

            /// Proceed with Login
            try {
                _loginState.update { it.copy(resultState = ResultState.Processing) }
                val result = repository.login(myEmail, myPassword)
                _loginState.update { it.copy(resultState = ResultState.Success(result)) }
                _effect.emit(LoginEffect.Success(result))
            } catch (e: Exception) {
                _loginState.update { it.copy(resultState = ResultState.Idle) }
                _effect.emit(LoginEffect.Failed(e.message ?: "Login Failed"))
            }
        }
    }
}