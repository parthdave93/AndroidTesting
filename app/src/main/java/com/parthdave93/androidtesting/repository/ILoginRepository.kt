package com.parthdave93.androidtesting.repository

import com.parthdave93.androidtesting.models.LoginResponse

interface ILoginRepository {
    suspend fun login(username: String, password: String): LoginResponse
}