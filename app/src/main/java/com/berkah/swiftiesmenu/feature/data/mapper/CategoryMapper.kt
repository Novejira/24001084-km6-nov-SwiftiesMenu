package com.berkah.swiftiesmenu.feature.data.mapper

import com.berkah.swiftiesmenu.feature.data.model.Category
import com.berkah.swiftiesmenu.feature.data.source.network.model.category.CategoryItemResponse

fun CategoryItemResponse?.toCategory() =
    Category(
        name = this?.name.orEmpty(),
        imgUrl = this?.imgUrl.orEmpty(),
        categoryDesc = "",
    )

fun Collection<CategoryItemResponse>?.toCategories() = this?.map { it.toCategory() } ?: listOf()
