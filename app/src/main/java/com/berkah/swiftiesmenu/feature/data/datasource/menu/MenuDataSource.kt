package com.berkah.swiftiesmenu.feature.data.datasource.menu

import com.berkah.swiftiesmenu.feature.data.source.network.model.checkout.CheckoutRequestPayload
import com.berkah.swiftiesmenu.feature.data.source.network.model.checkout.CheckoutResponse
import com.berkah.swiftiesmenu.feature.data.source.network.model.menu.MenuResponse

interface MenuDataSource {
    suspend fun getMenus(categoryName: String? = null): MenuResponse

    suspend fun createOrder(payload: CheckoutRequestPayload): CheckoutResponse
}
