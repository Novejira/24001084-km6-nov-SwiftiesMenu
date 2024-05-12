package com.berkah.swiftiesmenu.feature.presentation.home

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.berkah.swiftiesmenu.feature.data.repository.CategoryRepository
import com.berkah.swiftiesmenu.feature.data.repository.MenuRepository
import com.berkah.swiftiesmenu.feature.data.repository.PreferenceRepository
import com.berkah.swiftiesmenu.feature.data.utils.ResultWrapper
import com.berkah.swiftiesmenu.tools.MainCoroutineRule
import com.berkah.swiftiesmenu.tools.getOrAwaitValue
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule

class HomeViewModelTest {
    @get:Rule
    val testRule: TestRule = InstantTaskExecutorRule()

    @OptIn(ExperimentalCoroutinesApi::class)
    @get:Rule
    val coroutineRule: TestRule = MainCoroutineRule(UnconfinedTestDispatcher())

    @MockK
    lateinit var categoryRepository: CategoryRepository

    @MockK
    lateinit var menuRepository: MenuRepository

    @MockK
    lateinit var preferenceRepository: PreferenceRepository

    private lateinit var viewModel: HomeViewModel

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        viewModel =
            spyk(
                HomeViewModel(
                    categoryRepository,
                    menuRepository,
                    preferenceRepository,
                ),
            )
    }

    @Test
    fun isUsingGridMode() {
        every { preferenceRepository.isUsingGridMode() } returns true
        val result = viewModel.isUsingGridMode()
        assertEquals(true, result)
        verify { preferenceRepository.isUsingGridMode() }
    }

    @Test
    fun setUsingGridMode() {
        every { preferenceRepository.setUsingGridMode(any()) } returns Unit
        viewModel.setUsingGridMode(false)
        verify { preferenceRepository.setUsingGridMode(any()) }
    }

    @Test
    fun isUsingGrid() {
    }

    @Test
    fun changeGridMode() {
        val currentValue = true
        every { viewModel.isUsingGrid.value } returns currentValue
        viewModel.changeGridMode()
    }

    @Test
    fun getMenus() {
        every { menuRepository.getMenus() } returns
            flow {
                emit(ResultWrapper.Success(listOf(mockk(), mockk())))
            }
        val result = viewModel.getMenus().getOrAwaitValue()
        assertEquals(2, result.payload?.size)
        verify { menuRepository.getMenus(any()) }
    }

    @Test
    fun getCategories() {
        every { categoryRepository.getCategories() } returns
            flow {
                emit(ResultWrapper.Success(listOf(mockk(), mockk())))
            }
        val result = viewModel.getCategories().getOrAwaitValue()
        assertEquals(2, result.payload?.size)
        verify { categoryRepository.getCategories() }
    }
}
