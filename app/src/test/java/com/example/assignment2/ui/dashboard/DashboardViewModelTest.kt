package com.example.assignment2.ui.dashboard

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.assignment2.data.repository.DashboardRepository
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
class DashboardViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var dashboardRepository: DashboardRepository
    private lateinit var viewModel: DashboardViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        dashboardRepository = mockk()
        viewModel = DashboardViewModel(dashboardRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `loadDashboard success emits list of entities`() = runTest {
        val fakeEntities = listOf(mapOf("name" to "Item A", "description" to "Desc A"))
        coEvery { dashboardRepository.getDashboard("topicName") } returns Resource.Success(fakeEntities)

        viewModel.loadDashboard("topicName")
        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.dashboardState.value
        assertTrue(state is Resource.Success)
        assertEquals(1, (state as Resource.Success).data.size)
    }

    @Test
    fun `loadDashboard failure emits error`() = runTest {
        coEvery { dashboardRepository.getDashboard(any()) } returns Resource.Error("Network error")

        viewModel.loadDashboard("badKey")
        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.dashboardState.value
        assertTrue(state is Resource.Error)
        assertEquals("Network error", (state as Resource.Error).message)
    }
}
