package com.berkah.swiftiesmenu.repository

import app.cash.turbine.test
import com.berkah.swiftiesmenu.feature.data.datasource.menu.MenuDataSource
import com.berkah.swiftiesmenu.feature.data.repository.MenuRepository
import com.berkah.swiftiesmenu.feature.data.repository.MenuRepositoryImpl
import com.berkah.swiftiesmenu.feature.data.source.network.model.menu.MenuItemResponse
import com.berkah.swiftiesmenu.feature.data.source.network.model.menu.MenuResponse
import com.berkah.swiftiesmenu.feature.data.utils.ResultWrapper
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class MenuRepositoryImplTest {
    @MockK
    lateinit var ds: MenuDataSource
    private lateinit var repo: MenuRepository

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        repo = MenuRepositoryImpl(ds)
    }

    @Test
    fun `get menus loading`() {
        val c1 =
            MenuItemResponse(
                imgUrl = "lo",
                name = "lo",
                formattedprice = "lo",
                price = 123.0,
                desc = "lo",
                address = "lo",
            )
        val c2 =
            MenuItemResponse(
                imgUrl = "lo",
                name = "lo",
                formattedprice = "lo",
                price = 123.0,
                desc = "lo",
                address = "lo",
            )
        val mockListMenu = listOf(c1, c2)
        val mockResponse = mockk<MenuResponse>()
        every { mockResponse.data } returns mockListMenu
        runTest {
            coEvery { ds.getMenus() } returns mockResponse
            repo.getMenus().map {
                delay(100)
                it
            }.test {
                delay(110)
                val data = expectMostRecentItem()
                assertTrue(data is ResultWrapper.Loading)
                coVerify { ds.getMenus() }
            }
        }
    }

    @Test
    fun `get menus success`() {
        val c1 =
            MenuItemResponse(
                imgUrl = "lo",
                name = "lo",
                formattedprice = "lo",
                price = 123.0,
                desc = "lo",
                address = "lo",
            )
        val c2 =
            MenuItemResponse(
                imgUrl = "lo",
                name = "lo",
                formattedprice = "lo",
                price = 123.0,
                desc = "lo",
                address = "lo",
            )
        val mockListMenu = listOf(c1, c2)
        val mockResponse = mockk<MenuResponse>()
        every { mockResponse.data } returns mockListMenu
        runTest {
            coEvery { ds.getMenus() } returns mockResponse
            repo.getMenus().map {
                delay(100)
                it
            }.test {
                delay(110)
                val data = expectMostRecentItem()
                assertTrue(data is ResultWrapper.Loading)
                coVerify { ds.getMenus() }
            }
        }
    }

    @Test
    fun `get menus error`() {
        runTest {
            coEvery { ds.getMenus() } throws IllegalStateException("Mock Error")
            repo.getMenus().map {
                delay(100)
                it
            }.test {
                delay(210)
                val data = expectMostRecentItem()
                assertTrue(data is ResultWrapper.Error)
                coVerify { ds.getMenus() }
            }
        }
    }

    @Test
    fun `get menus empty`() {
        val mockListMenu = listOf<MenuItemResponse>()
        val mockResponse = mockk<MenuResponse>()
        every { mockResponse.data } returns mockListMenu
        runTest {
            coEvery { ds.getMenus() } returns mockResponse
            repo.getMenus().map {
                delay(100)
                it
            }.test {
                delay(210)
                val data = expectMostRecentItem()
                assertTrue(data is ResultWrapper.Empty)
                coVerify { ds.getMenus() }
            }
        }
    }

    @Test
    fun createOrder() {
    }
}
