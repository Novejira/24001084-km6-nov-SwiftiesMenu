package com.berkah.swiftiesmenu.datasource

import com.berkah.swiftiesmenu.feature.data.datasource.menu.MenuApiDataSource
import com.berkah.swiftiesmenu.feature.data.datasource.menu.MenuDataSource
import com.berkah.swiftiesmenu.feature.data.source.network.model.checkout.CheckoutRequestPayload
import com.berkah.swiftiesmenu.feature.data.source.network.model.checkout.CheckoutResponse
import com.berkah.swiftiesmenu.feature.data.source.network.model.menu.MenuResponse
import com.berkah.swiftiesmenu.feature.data.source.network.services.SwiftiesMenuApiService
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class MenuApiDataSourceTest {
    @MockK
    lateinit var service: SwiftiesMenuApiService

    private lateinit var ds: MenuDataSource

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        ds = MenuApiDataSource(service)
    }

    @Test
    fun getMenus() {
        runTest {
            val mockResponse = mockk<MenuResponse>(relaxed = true)
            coEvery { service.getProducts(any()) } returns mockResponse
            val actualResponse = ds.getMenus("makanan")
            coVerify { service.getProducts(any()) }
            assertEquals(actualResponse, mockResponse)
        }
    }

    @Test
    fun createOrder() {
        runTest {
            val mockRequest = mockk<CheckoutRequestPayload>(relaxed = true)
            val mockResponse = mockk<CheckoutResponse>(relaxed = true)
            coEvery { service.createOrder(any()) } returns mockResponse
            val actualResponse = ds.createOrder(mockRequest)
            coVerify { service.createOrder(any()) }
            assertEquals(actualResponse, mockResponse)
        }
    }
}
