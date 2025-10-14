package com.example.fakestore.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

// State
data class SignupState(
    val name: String = "",
    val email: String = "",
    val password: String = "",
    val loading: Boolean = false
)

// Event
sealed class SignupEvent {
    data object Signup : SignupEvent()

    sealed class Input {
        data class EnterName(val name: String) : SignupEvent()
        data class EnterEmail(val email: String) : SignupEvent()
        data class EnterPassword(val password: String) : SignupEvent()
    }

    data object NavigateToLogin : SignupEvent()
    data class ErrorMessage(val error: String) : SignupEvent()
}

class SignupViewModel(private val repository: AuthRepository) : ViewModel() {
    private var _signupState = MutableStateFlow(SignupState())
    val state: StateFlow<SignupState> = _signupState.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = SignupState()
    )

    private var _event = Channel<SignupEvent>()
    val event = _event.receiveAsFlow()

    fun onEvent(event: SignupEvent) {
        when (event) {
            is SignupEvent.Input.EnterEmail -> {
                _signupState.update { it.copy(email = event.email) }
            }

            is SignupEvent.Input.EnterPassword -> {
                _signupState.update { it.copy(password = event.password) }
            }

            is SignupEvent.Input.EnterName -> {
                _signupState.update { it.copy(name = event.name) }
            }

            is SignupEvent.Signup -> {
                signup()
            }

            else -> {}
        }
    }

    private fun signup() {
        viewModelScope.launch {
            val email = _signupState.value.email
            val password = _signupState.value.password
            val name = _signupState.value.name

            if (email.isEmpty()) {
                _event.send(SignupEvent.ErrorMessage("Enter a valid email."))
                return@launch
            }

            if (password.isEmpty()) {
                _event.send(SignupEvent.ErrorMessage("Your password must be 6 character long."))
                return@launch
            }

            if (name.isEmpty()) {
                _event.send(SignupEvent.ErrorMessage("Enter your first and last name"))
                return@launch
            }

            _signupState.update { it.copy(loading = true) }
            val result = repository.register(email, password, name)
            if (result) {
                _event.send(SignupEvent.NavigateToLogin)
            } else {
                _event.send(SignupEvent.ErrorMessage("Signup Failed"))
            }
            _signupState.update { it.copy(loading = false) }
        }
    }
}