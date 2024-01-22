package com.parthdave93.androidtesting

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.parthdave93.androidtesting.ui.LoginActivity

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.Rule

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class LoginInstrumentedTest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<LoginActivity>()

    @Test
    fun loginUsernameRequired() {
        composeTestRule.onNodeWithText("Login").performClick()

        composeTestRule.onNodeWithText("Username required").assertIsDisplayed()
    }

    @Test
    fun loginPasswordRequired() {
        composeTestRule.onNodeWithText("Username").performTextInput("asd")

        composeTestRule.onNodeWithText("Login").performClick()

        composeTestRule.onNodeWithText("Password required").assertIsDisplayed()
    }

    @Test
    fun loginPasswordLength() {
        composeTestRule.onNodeWithText("Username").performTextInput("asd")
        composeTestRule.onNodeWithText("Password").performTextInput("asd")

        composeTestRule.onNodeWithText("Login").performClick()

        composeTestRule.onNodeWithText("Password should be atleast 8 characters").assertIsDisplayed()
    }



    @Test
    fun loginPasswordSpecialCharactersRequired() {
        composeTestRule.onNodeWithText("Username").performTextInput("asd")
        composeTestRule.onNodeWithText("Password").performTextInput("asdasdasdasd")

        composeTestRule.onNodeWithText("Login").performClick()

        composeTestRule.onNodeWithText("Password should contain one special character from: [!, @, #, \$, %, ^, &, *]").assertIsDisplayed()
    }


    @Test
    fun loginPasswordShouldContainUpperCase() {
        composeTestRule.onNodeWithText("Username").performTextInput("asd")

        composeTestRule.onNodeWithText("Password").performTextInput("asdasdasda!")

        composeTestRule.onNodeWithText("Login").performClick()

        composeTestRule.onNodeWithText("Password should contain one uppercase").assertIsDisplayed()
    }


    @Test
    fun loginPasswordShouldContainLowerCase() {
        composeTestRule.onNodeWithText("Username").performTextInput("asd")

        composeTestRule.onNodeWithText("Password").performTextInput("ASDASDASDA!")

        composeTestRule.onNodeWithText("Login").performClick()

        composeTestRule.onNodeWithText("Password should contain one lowercase").assertIsDisplayed()
    }


    @Test
    fun loginPasswordShouldContainDigits() {
        composeTestRule.onNodeWithText("Username").performTextInput("asd")

        composeTestRule.onNodeWithText("Password").performTextInput("ASsddSDASDA!")

        composeTestRule.onNodeWithText("Login").performClick()

        composeTestRule.onNodeWithText("Password should contain one number").assertIsDisplayed()
    }
}