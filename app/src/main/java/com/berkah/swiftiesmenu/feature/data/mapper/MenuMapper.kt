package com.berkah.swiftiesmenu.feature.data.mapper

import com.berkah.swiftiesmenu.feature.data.model.Menu
import com.berkah.swiftiesmenu.feature.data.source.network.model.menu.MenuItemResponse

fun MenuItemResponse?.toMenu() =
    Menu(
        name = this?.name.orEmpty(),
        price = this?.price ?: 0.0,
        imgUrl = this?.imgUrl.orEmpty(),
        desc = this?.desc.orEmpty(),
        addres = this?.address.orEmpty(),
    )

fun Collection<MenuItemResponse>?.toMenus() =
    this?.map {
        it.toMenu()
    } ?: listOf()
