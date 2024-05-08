package com.berkah.swiftiesmenu.feature.data.datasource.category

import com.berkah.swiftiesmenu.feature.data.source.network.model.category.CategoriesResponse

interface CategoryDataSource {
    suspend fun getCategories(): CategoriesResponse
}
