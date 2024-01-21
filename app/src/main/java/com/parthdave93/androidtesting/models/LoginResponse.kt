package com.parthdave93.androidtesting.models

class LoginResponse(success: Boolean, message: String? = null, val user: User?) : CommonResponse(success, message) {
}