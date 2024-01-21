package com.parthdave93.androidtesting.ui

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LiveData
import com.parthdave93.androidtesting.repository.LoginRepositoryImpl
import com.parthdave93.androidtesting.viewmodels.LoginViewModel
import com.parthdave93.androidtesting.viewmodels.ViewModelFactory

class LoginActivity : ComponentActivity() {

    private val viewModel: LoginViewModel by viewModels {
        //Can be replaced by koin/dagger dependency injection
        ViewModelFactory(LoginRepositoryImpl())
    }

    private val usernameState = mutableStateOf("")
    private val passwordState = mutableStateOf("")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Scaffold { _ ->
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                ) {
                    textTitle("Login Screen")
                    editText(
                        label = "Username",
                        usernameState,
                        errorMessage = viewModel.usernameErrorStream
                    )
                    editText(
                        label = "Password",
                        passwordState,
                        errorMessage = viewModel.passwordErrorStream
                    )
                    button(label = "Login", onClick = ::onLoginClick)
                }
            }
        }
        setupListeners()
    }

    private fun setupListeners() {
        viewModel.loginErrorStream.observe(this) {
            //show snackbar
            showMessage(it)
        }
        viewModel.loginSuccessStream.observe(this) {
            //show success
            showMessage("Login Success")
        }
    }

    @Composable
    fun textTitle(message: String) {
        Text(
            text = message
        )
    }

    @Composable
    fun editText(label: String, state: MutableState<String>, errorMessage: LiveData<String?>) {
        var text by remember { state }
        val errorMessageText = errorMessage.observeAsState().value

        Column {
            OutlinedTextField(
                value = text,
                onValueChange = { text = it },
                label = { Text(label) }
            )
            if (errorMessageText != null) {
                Text(
                    text = errorMessageText,
                    color = MaterialTheme.colorScheme.error
                )
            }
        }
    }

    @Composable
    fun button(label: String, onClick: () -> Unit) {
        Button(onClick = onClick) {
            Text(label)
        }
    }


    fun showMessage(message: String?) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    private fun onLoginClick() {
        viewModel.login(usernameState.value, passwordState.value)
    }
}