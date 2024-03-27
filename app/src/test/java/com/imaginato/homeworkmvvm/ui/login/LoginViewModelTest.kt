package com.imaginato.homeworkmvvm.ui.login

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.imaginato.homeworkmvvm.data.remote.login.ApiRepository
import com.imaginato.homeworkmvvm.ui.login.entity.FieldValidateState
import com.imaginato.homeworkmvvm.ui.login.entity.RemoteAPIState
import com.imaginato.homeworkmvvm.ui.login.entity.Status
import com.nhaarman.mockitokotlin2.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.koin.core.component.KoinApiExtension
import org.mockito.ArgumentMatchers.any
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations

@Suppress("OPT_IN_IS_NOT_ENABLED")
@OptIn(KoinApiExtension::class)
@ExperimentalCoroutinesApi
class LoginViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var apiRepository: ApiRepository

    private val testDispatcher = TestCoroutineDispatcher()

    private lateinit var loginViewModel: LoginViewModel

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        loginViewModel = LoginViewModel()
    }

    @Test
    fun `validateForm when username and password are empty should return error`() {
        // Given
        val observer = mock<Observer<FieldValidateState>>()
        loginViewModel.verifyData.observeForever(observer)

        // When
        loginViewModel.validateForm("", "")

        // Then
        verify(observer).onChanged(FieldValidateState(false, "Please enter username.", Status.ERROR_USER_NAME))
    }

    @Test
    fun `validateForm when username is empty should return error`() {
        // Given
        val observer = mock<Observer<FieldValidateState>>()
        loginViewModel.verifyData.observeForever(observer)

        // When
        loginViewModel.validateForm("", "password")

        // Then
        verify(observer).onChanged(FieldValidateState(false, "Please enter username.", Status.ERROR_USER_NAME))
    }

    @Test
    fun `validateForm when password is empty should return error`() {
        // Given
        val observer = mock<Observer<FieldValidateState>>()
        loginViewModel.verifyData.observeForever(observer)

        // When
        loginViewModel.validateForm("username", "")

        // Then
        verify(observer).onChanged(FieldValidateState(false, "Please enter password.", Status.ERROR_PASSWORD))
    }

    @Test
    fun `validateForm when both username and password are not empty should return success`() {
        // Given
        val observer = mock<Observer<FieldValidateState>>()
        loginViewModel.verifyData.observeForever(observer)

        // When
        loginViewModel.validateForm("username", "password")

        // Then
        verify(observer).onChanged(FieldValidateState(true, "", Status.SUCCESS))
    }

    @Test
    fun `loginUser should call login API`() = testDispatcher.runBlockingTest {
        // Given
        val imsi = "imsi"
        val imei = "imei"
        val username = "username"
        val password = "password"

        // When
        loginViewModel.loginUser(imsi, imei, username, password)

        // Then
        verify(apiRepository).loginUser(any(), any(), any())
    }

    // Add more test cases as needed
}
