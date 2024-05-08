package com.berkah.swiftiesmenu.feature.data.datasource.menu

import com.berkah.swiftiesmenu.feature.data.source.network.model.checkout.CheckoutRequestPayload
import com.berkah.swiftiesmenu.feature.data.source.network.model.checkout.CheckoutResponse
import com.berkah.swiftiesmenu.feature.data.source.network.model.menu.MenuResponse
import com.berkah.swiftiesmenu.feature.data.source.network.services.SwiftiesMenuApiService

class MenuApiDataSource(
    private val service: SwiftiesMenuApiService,
) : MenuDataSource {
    override suspend fun getMenus(categoryName: String?): MenuResponse {
        return service.getProducts(categoryName)
    }

    override suspend fun createOrder(payload: CheckoutRequestPayload): CheckoutResponse {
        return service.createOrder(payload)
    }
}
