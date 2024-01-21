package com.parthdave93.androidtesting.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.parthdave93.androidtesting.repository.ILoginRepository
import kotlinx.coroutines.launch

class LoginViewModel (private val loginRepository: ILoginRepository) : ViewModel() {
    private val usernameErrorLiveData: MutableLiveData<String?> = MutableLiveData<String?>()

    private val passwordErrorLiveData: MutableLiveData<String?> = MutableLiveData<String?>()

    private val loginSuccessLiveData: MutableLiveData<Boolean> = MutableLiveData<Boolean>()

    private val loginErrorLiveData: MutableLiveData<String?> = MutableLiveData<String?>()

    fun login(username: String?, password: String?) {
        val usernameError = validateUsername(username)
        if(usernameError != null) {
            usernameErrorLiveData.postValue(usernameError)
            return
        } else {
            usernameErrorLiveData.postValue("")
        }

        val isValidPassword = validatePassword(password)
        if(isValidPassword != null) {
            passwordErrorLiveData.postValue(isValidPassword)
            return
        } else {
            passwordErrorLiveData.postValue("")
        }

        viewModelScope.launch {
            try {
                val response = loginRepository.login(username = username!!, password = password!!)

                if(response.success) {
                    loginSuccessLiveData.postValue(response.success)
                } else {
                    loginErrorLiveData.postValue(response.message)
                }
            } catch (e: Exception) {
                // Handle exceptions and create util to handle it
                e.printStackTrace()
            }
        }
    }

    private fun validateUsername(username: String?) : String? {
        if(username.isNullOrEmpty()) {
            return "Username required"
        }
        return null
    }

    private fun validatePassword(password: String?) : String? {
        if(password.isNullOrEmpty()) {
            return "Password required"
        }
        if(password.length < 8 ) {
            return "Password should be atleast 8 characters"
        }

        val specialCharacters = listOf('!', '@', '#', '$', '%', '^', '&', '*')
        val uppercaseLetters = ('A'..'Z').toList()
        val lowercaseLetters = ('a'..'z').toList()
        val digits = ('0'..'9').toList()

        val containsSpecialChar = password.any { it in specialCharacters }
        val containsUppercase = password.any { it in uppercaseLetters }
        val containsLowercase = password.any { it in lowercaseLetters }
        val containsDigit = password.any { it in digits }

        if(!containsSpecialChar) {
            return "Password should contain one special character from: ${specialCharacters.toList()}"
        }
        if(!containsUppercase) {
            return "Password should contain one uppercase"
        }
        if(!containsLowercase) {
            return "Password should contain one lowercase"
        }
        if(!containsDigit) {
            return "Password should contain one number"
        }
        return null
    }

    val usernameErrorStream: LiveData<String?>
        get() = usernameErrorLiveData
    val passwordErrorStream: LiveData<String?>
        get() = passwordErrorLiveData
    val loginSuccessStream: LiveData<Boolean>
        get() = loginSuccessLiveData
    val loginErrorStream: LiveData<String?>
        get() = loginErrorLiveData
}