package com.berkah.swiftiesmenu.feature.data.repository

import com.berkah.swiftiesmenu.feature.data.datasource.category.CategoryDataSource
import com.berkah.swiftiesmenu.feature.data.mapper.toCategories
import com.berkah.swiftiesmenu.feature.data.model.Category
import com.berkah.swiftiesmenu.feature.data.utils.ResultWrapper
import com.berkah.swiftiesmenu.feature.data.utils.proceedFlow
import kotlinx.coroutines.flow.Flow

interface CategoryRepository {
    fun getCategories(): Flow<ResultWrapper<List<Category>>>
}

class CategoryRepositoryImpl(
    private val dataSource: CategoryDataSource,
) : CategoryRepository {
    override fun getCategories(): Flow<ResultWrapper<List<Category>>> {
        return proceedFlow { dataSource.getCategories().data.toCategories() }
    }
}
