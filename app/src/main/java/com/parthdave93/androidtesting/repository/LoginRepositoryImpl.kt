package com.parthdave93.androidtesting.repository

import com.parthdave93.androidtesting.models.LoginResponse
import com.parthdave93.androidtesting.models.User
import kotlinx.coroutines.delay

class LoginRepositoryImpl : ILoginRepository {
    override suspend fun login(username: String, password: String): LoginResponse {
        delay(3000)
        return LoginResponse(success = true, user = User(username))
    }
}