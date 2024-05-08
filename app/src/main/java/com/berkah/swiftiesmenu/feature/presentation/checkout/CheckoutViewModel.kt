package com.berkah.swiftiesmenu.feature.presentation.checkout

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.berkah.swiftiesmenu.feature.data.repository.CartRepository
import com.berkah.swiftiesmenu.feature.data.repository.MenuRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class CheckoutViewModel(
    private val cartRepository: CartRepository,
    private val menuRepository: MenuRepository,
) : ViewModel() {
    val checkoutData = cartRepository.getCheckoutData().asLiveData(Dispatchers.IO)

    fun checkoutCart() =
        menuRepository.createOrder(
            checkoutData.value?.payload?.first.orEmpty(),
        ).asLiveData(Dispatchers.IO)

    fun deleteAllCart() {
        viewModelScope.launch(Dispatchers.IO) {
            cartRepository.deleteAll().collect()
        }
    }
}
