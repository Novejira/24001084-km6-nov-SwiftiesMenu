package com.berkah.swiftiesmenu.feature.presentation.main

import androidx.lifecycle.ViewModel
import com.berkah.swiftiesmenu.feature.data.repository.UserRepository

class MainViewModel (private val repository: UserRepository) : ViewModel() {

    fun isUserLoggedIn() = repository.isLoggedIn()

}