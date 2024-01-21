package com.parthdave93.androidtesting

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.parthdave93.androidtesting.models.LoginResponse
import com.parthdave93.androidtesting.repository.ILoginRepository
import com.parthdave93.androidtesting.repository.LoginRepositoryImpl
import com.parthdave93.androidtesting.viewmodels.LoginViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.spy
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(MockitoJUnitRunner::class)
class LoginViewModelUnitTest {
    @Mock
    lateinit var loginRepository: ILoginRepository

    //Since we have live data we have to add instant executor rule so that it will post all the values instantly than waiting for main looper
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    lateinit var viewModel : LoginViewModel
    @Before
    fun setupTest() {
        viewModel = LoginViewModel(loginRepository)

        // Set the main dispatcher for coroutines
        Dispatchers.setMain(Dispatchers.Default)
    }

    @Test
    fun whenUsernameIsBlank() {
        viewModel.login("", "")
        assertNotNull(viewModel.usernameErrorStream.value)
        assertEquals(viewModel.usernameErrorStream.value, "Username required")
    }

    @Test
    fun whenPasswordIsBlank() {
        viewModel.login("asdasd", "")
        assertEquals(viewModel.usernameErrorStream.value, "")
        assertNotNull(viewModel.passwordErrorStream.value)
        assertEquals(viewModel.passwordErrorStream.value, "Password required")
    }

    @Test
    fun whenPasswordLengthIsLessThan8Characters() {
        viewModel.login("asdasd", "asd")
        assertEquals(viewModel.usernameErrorStream.value, "")
        assertNotNull(viewModel.passwordErrorStream.value)
        assertEquals(viewModel.passwordErrorStream.value, "Password should be atleast 8 characters")
    }

    @Test
    fun passwordWithoutSpecialCharacters() {
        viewModel.login("asdasd", "asdasdasdasd")
        assertEquals(viewModel.usernameErrorStream.value, "")
        assertNotNull(viewModel.passwordErrorStream.value)
        assertEquals(viewModel.passwordErrorStream.value, "Password should contain one special character from: [!, @, #, \$, %, ^, &, *]")
    }

    @Test
    fun passwordWithoutCapitalCharacter() {
        viewModel.login("asdasd", "asdasdasda!")
        assertEquals(viewModel.usernameErrorStream.value, "")
        assertNotNull(viewModel.passwordErrorStream.value)
        assertEquals(viewModel.passwordErrorStream.value, "Password should contain one uppercase")
    }
    @Test
    fun passwordWithoutLowerCaseCharacter() {
        viewModel.login("asdasd", "ASDASDASDA!")
        assertEquals(viewModel.usernameErrorStream.value, "")
        assertNotNull(viewModel.passwordErrorStream.value)
        assertEquals(viewModel.passwordErrorStream.value, "Password should contain one lowercase")
    }
    @Test
    fun passwordWithoutDigit() {
        viewModel.login("asdasd", "ASsddSDASDA!")
        assertEquals(viewModel.usernameErrorStream.value, "")
        assertNotNull(viewModel.passwordErrorStream.value)
        assertEquals(viewModel.passwordErrorStream.value, "Password should contain one number")
    }
    @Test
    fun CheckLoginMethodRepositoryCalledOrNot() = runBlocking {
        val username = "asdasd"
        val password = "tesTing123!"

        `when`(loginRepository.login(username, password)).thenReturn(LoginResponse(true, null, null))

        viewModel.login(username, password)
        assertEquals(viewModel.usernameErrorStream.value, "")
        assertEquals(viewModel.passwordErrorStream.value, "")

        verify(loginRepository, times(1)).login(username, password)
        assertTrue(viewModel.loginSuccessStream.value!!)
    }


    @Test(expected = NullPointerException::class)
    fun CheckLoginMethodRepositoryCalledWithCorrectParameters() = runBlocking {
        val username = "asdasd"
        val password = "tesTing123!"

        //we are mocking for different parameters and will throw Unnecessary stubbing error
        `when`(loginRepository.login("asd", "testing1234443$")).thenReturn(LoginResponse(true, null, null))

        //and login calls with different parameters then this method will fail
        viewModel.login(username, password)
        assertEquals(viewModel.usernameErrorStream.value, "")
        assertEquals(viewModel.passwordErrorStream.value, "")

        verify(loginRepository, times(1)).login(username, password)
        assertTrue(viewModel.loginSuccessStream.value!!)
    }
}