package com.berkah.swiftiesmenu.feature.presentation.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.berkah.swiftiesmenu.feature.data.repository.UserRepository
import com.berkah.swiftiesmenu.feature.data.utils.ResultWrapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginViewModel(private val repository: UserRepository) : ViewModel() {

    private val _loginResult = MutableLiveData<ResultWrapper<Boolean>>()
    val loginResult: LiveData<ResultWrapper<Boolean>>
        get() = _loginResult

    fun doLogin(email: String, password: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.doLogin(email, password).collect {
                _loginResult.postValue(it)
            }
        }
    }
}