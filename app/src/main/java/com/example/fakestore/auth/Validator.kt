package com.example.fakestore.auth

data class ValidationResult(
    val successful: Boolean,
    val errorMessage: String? = null
)

object Validator {
    fun validateEmail(email: String): ValidationResult {
        return if (email.isBlank()) {
            ValidationResult(false, "Email cannot be empty")
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            ValidationResult(false, "Invalid email format")
        } else {
            ValidationResult(true)
        }
    }

    fun validatePassword(password: String): ValidationResult {
        return if (password.length < 6) {
            ValidationResult(false, "Password must be at least 6 characters")
        } else {
            ValidationResult(true)
        }
    }
}
