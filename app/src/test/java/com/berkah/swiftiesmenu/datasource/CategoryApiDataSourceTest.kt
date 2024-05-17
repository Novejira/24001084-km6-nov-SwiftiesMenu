package com.berkah.swiftiesmenu.datasource

import com.berkah.swiftiesmenu.feature.data.datasource.category.CategoryApiDataSource
import com.berkah.swiftiesmenu.feature.data.datasource.category.CategoryDataSource
import com.berkah.swiftiesmenu.feature.data.source.network.model.category.CategoriesResponse
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

class CategoryApiDataSourceTest {
    @MockK
    lateinit var service: SwiftiesMenuApiService

    private lateinit var dataSource: CategoryDataSource

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        dataSource = CategoryApiDataSource(service)
    }

    @Test
    fun getCategories() {
        runTest {
            val mockResponse = mockk<CategoriesResponse>(relaxed = true)
            coEvery { service.getCategories() } returns mockResponse
            val actualResult = dataSource.getCategories()
            coVerify { service.getCategories() }
            assertEquals(mockResponse, actualResult)
        }
    }
}
