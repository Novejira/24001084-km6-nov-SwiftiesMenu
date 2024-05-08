package com.berkah.swiftiesmenu.feature.data.datasource.category

import com.berkah.swiftiesmenu.feature.data.source.network.model.category.CategoriesResponse
import com.berkah.swiftiesmenu.feature.data.source.network.services.SwiftiesMenuApiService

class CategoryApiDataSource(
    private val service: SwiftiesMenuApiService,
) : CategoryDataSource {
    override suspend fun getCategories(): CategoriesResponse {
        return service.getCategories()
    }
}
