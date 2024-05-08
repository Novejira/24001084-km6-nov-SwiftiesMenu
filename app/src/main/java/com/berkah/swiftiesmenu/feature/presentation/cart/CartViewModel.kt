package com.berkah.swiftiesmenu.feature.presentation.cart

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.berkah.swiftiesmenu.feature.data.model.Cart
import com.berkah.swiftiesmenu.feature.data.repository.CartRepository
import com.berkah.swiftiesmenu.feature.data.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class CartViewModel(
    private val cartRepository: CartRepository,
    private val repository: UserRepository
) : ViewModel() {

    fun getAllCarts() = cartRepository.getUserCartData().asLiveData(Dispatchers.IO)

    fun decreaseCart(item: Cart) {
        viewModelScope.launch(Dispatchers.IO) {
            cartRepository.decreaseCart(item).collect()
        }
    }
    fun increaseCart(item: Cart) {
        viewModelScope.launch(Dispatchers.IO) {
            cartRepository.increaseCart(item).collect()
        }
    }
    fun removeCart(item: Cart) {
        viewModelScope.launch(Dispatchers.IO) {
            cartRepository.deleteCart(item).collect()
        }
    }
    fun setCartNotes(item: Cart) {
        viewModelScope.launch(Dispatchers.IO) {
            cartRepository.setCartNotes(item).collect{
                Log.d("Set Notes", "setCartNotes: $it")
            }
        }
    }
    fun isUserLoggedIn() = repository.isLoggedIn()
}
