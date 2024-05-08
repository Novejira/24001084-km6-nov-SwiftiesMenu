package com.berkah.swiftiesmenu.feature.presentation.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.berkah.swiftiesmenu.feature.data.repository.CategoryRepository
import com.berkah.swiftiesmenu.feature.data.repository.MenuRepository
import com.berkah.swiftiesmenu.feature.data.repository.PreferenceRepository
import kotlinx.coroutines.Dispatchers

class HomeViewModel(
    private val categoryRepository: CategoryRepository,
    private val menuRepository: MenuRepository,
    private val preferenceRepository: PreferenceRepository,
) : ViewModel() {
    fun isUsingGridMode() = preferenceRepository.isUsingGridMode()

    fun setUsingGridMode(isUsingGridMode: Boolean) = preferenceRepository.setUsingGridMode(isUsingGridMode)

    val isUsingGrid = MutableLiveData<Boolean>(false)

    fun changeGridMode() {
        val currentValue = isUsingGrid.value ?: false
        isUsingGrid.postValue(!currentValue)
    }

    fun getMenus(categoryName: String? = null) = menuRepository.getMenus(categoryName).asLiveData(Dispatchers.IO)

    fun getCategories() = categoryRepository.getCategories().asLiveData(Dispatchers.IO)
}
