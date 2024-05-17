package com.berkah.swiftiesmenu.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.berkah.swiftiesmenu.feature.data.repository.CartRepository
import com.berkah.swiftiesmenu.feature.data.repository.UserRepository
import com.berkah.swiftiesmenu.feature.data.utils.ResultWrapper
import com.berkah.swiftiesmenu.feature.presentation.cart.CartViewModel
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

class CartViewModelTest {
    @get:Rule
    val testRule: TestRule = InstantTaskExecutorRule()

    @OptIn(ExperimentalCoroutinesApi::class)
    @get:Rule
    val coroutineRule: TestRule = MainCoroutineRule(UnconfinedTestDispatcher())

    @MockK
    private lateinit var cartrepo: CartRepository

    @MockK
    private lateinit var userrepo: UserRepository
    private lateinit var viewModel: CartViewModel

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        viewModel = spyk(CartViewModel(cartrepo, userrepo))
    }

    @Test
    fun getAllCarts() {
        every { cartrepo.getUserCartData() } returns
            flow {
                emit(
                    ResultWrapper.Success(
                        Pair(listOf(mockk(relaxed = true), mockk(relaxed = true)), 8000.0),
                    ),
                )
            }
        val result = viewModel.getAllCarts().getOrAwaitValue()
        assertEquals(2, result.payload?.first?.size)
        assertEquals(8000.0, result.payload?.second)
    }

    @Test
    fun decreaseCart() {
        every { cartrepo.decreaseCart(any()) } returns
            flow {
                emit(ResultWrapper.Success(true))
            }
        viewModel.decreaseCart(mockk())
        verify { cartrepo.decreaseCart(any()) }
    }

    @Test
    fun increaseCart() {
        every { cartrepo.increaseCart(any()) } returns
            flow {
                emit(ResultWrapper.Success(true))
            }
        viewModel.increaseCart(mockk())
        verify { cartrepo.increaseCart(any()) }
    }

    @Test
    fun removeCart() {
        every { cartrepo.deleteCart(any()) } returns
            flow {
                emit(ResultWrapper.Success(true))
            }
        viewModel.removeCart(mockk())
        verify { cartrepo.deleteCart(any()) }
    }

    @Test
    fun setCartNotes() {
        every { cartrepo.setCartNotes(any()) } returns
            flow {
                emit(ResultWrapper.Success(true))
            }
        viewModel.setCartNotes(mockk())
        verify { cartrepo.setCartNotes(any()) }
    }

    @Test
    fun isUserLoggedIn() {
        every { userrepo.isLoggedIn() } returns true
        val result = viewModel.isUserLoggedIn()
        assertEquals(true, result)
        verify { userrepo.isLoggedIn() }
    }
}
