package com.example.fakestore.auth

import kotlinx.coroutines.delay

interface AuthRepository {
    suspend fun login(email: String, password: String): String

    suspend fun register(email: String, password: String, name: String): Boolean

    suspend fun forgotPassword(email: String): Boolean
}

class AuthRepositoryImpl : AuthRepository {
    override suspend fun login(email: String, password: String): String {
        delay(2000)
        if (email != "s@s.com") {
            throw Exception("Email not found")
        }

        //TODO(sm): Validate using https://fakestoreapi.com/docs#tag/Auth
        return "Success"
    }

    override suspend fun register(email: String, password: String, name: String): Boolean {
        delay(3000)
        return true
    }

    override suspend fun forgotPassword(email: String): Boolean {
        delay(1000)
        return true
    }
}