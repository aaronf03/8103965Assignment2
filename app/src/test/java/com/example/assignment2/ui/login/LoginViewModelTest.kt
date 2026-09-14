package com.example.assignment2.ui.login

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.assignment2.data.repository.AuthRepository
import com.example.assignment2.util.Resource
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class LoginViewModelTest {

    // Makes LiveData execute synchronously in unit tests.
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var authRepository: AuthRepository
    private lateinit var viewModel: LoginViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        authRepository = mockk()
        viewModel = LoginViewModel(authRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `login with blank username emits error without calling repository`() = runTest {
        viewModel.login("", "password")

        val state = viewModel.loginState.value
        assertTrue(state is Resource.Error)
    }

    @Test
    fun `login success emits keypass`() = runTest {
        coEvery { authRepository.login("8103965", "Aaron") } returns Resource.Success("topicName")

        viewModel.login("8103965", "Aaron")
        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.loginState.value
        assertTrue(state is Resource.Success)
        assertEquals("topicName", (state as Resource.Success).data)
    }

    @Test
    fun `login failure emits error message`() = runTest {
        coEvery { authRepository.login(any(), any()) } returns Resource.Error("Invalid credentials")

        viewModel.login("8103965", "wrongpass")
        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.loginState.value
        assertTrue(state is Resource.Error)
        assertEquals("Invalid credentials", (state as Resource.Error).message)
    }
}
