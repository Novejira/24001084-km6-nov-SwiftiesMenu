package com.berkah.swiftiesmenu.feature.data.repository

import com.berkah.swiftiesmenu.feature.data.datasource.menu.MenuDataSource
import com.berkah.swiftiesmenu.feature.data.mapper.toMenus
import com.berkah.swiftiesmenu.feature.data.model.Cart
import com.berkah.swiftiesmenu.feature.data.model.Menu
import com.berkah.swiftiesmenu.feature.data.source.network.model.checkout.CheckoutItemPayload
import com.berkah.swiftiesmenu.feature.data.source.network.model.checkout.CheckoutRequestPayload
import com.berkah.swiftiesmenu.feature.data.utils.ResultWrapper
import com.berkah.swiftiesmenu.feature.data.utils.proceedFlow
import kotlinx.coroutines.flow.Flow

interface MenuRepository {
    fun getMenus(categoryName: String? = null): Flow<ResultWrapper<List<Menu>>>

    fun createOrder(menus: List<Cart>): Flow<ResultWrapper<Boolean>>
}

class MenuRepositoryImpl(
    private val dataSource: MenuDataSource,
) : MenuRepository {
    override fun getMenus(categoryName: String?): Flow<ResultWrapper<List<Menu>>> {
        return proceedFlow {
            dataSource.getMenus(categoryName).data.toMenus()
        }
    }

    override fun createOrder(menus: List<Cart>): Flow<ResultWrapper<Boolean>> {
        return proceedFlow {
            dataSource.createOrder(
                CheckoutRequestPayload(
                    orders =
                        menus.map {
                            CheckoutItemPayload(
                                notes = it.itemNotes,
                                menuId = it.menuId.orEmpty(),
                                quantity = it.itemQuantity,
                            )
                        },
                ),
            ).status ?: false
        }
    }
}
