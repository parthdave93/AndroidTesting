package com.parthdave93.androidtesting

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.parthdave93.androidtesting.repository.ILoginRepository
import com.parthdave93.androidtesting.repository.LoginRepositoryImpl
import com.parthdave93.androidtesting.viewmodels.LoginViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class SpyLoginViewModelUnitTest {
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

    //Spy is used when we don't want to replace the original implementation but still have to verify the methods calls
    @Test
    fun testLoginViewModelWithOriginalRepository() = runBlocking {
        val loginRepository = LoginRepositoryImpl()
        val spyLoginRepositoryImpl = Mockito.spy(loginRepository)
        viewModel = LoginViewModel(spyLoginRepositoryImpl)
        val username = "asdasd"
        val password = "tesTing123!"

        viewModel.login(username, password)
        Assert.assertEquals(viewModel.usernameErrorStream.value, "")
        Assert.assertEquals(viewModel.passwordErrorStream.value, "")
        //as we are delaying in method for 3000 mili seconds
        delay(3100)

        Mockito.verify(spyLoginRepositoryImpl, Mockito.times(1)).login(username, password)


        Assert.assertTrue(viewModel.loginSuccessStream.value!!)
    }
}