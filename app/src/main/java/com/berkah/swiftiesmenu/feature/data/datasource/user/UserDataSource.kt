package com.berkah.swiftiesmenu.feature.data.datasource.user

import com.berkah.swiftiesmenu.feature.data.source.local.pref.UserPreference

interface UserDataSource {
    fun isUsingGridMode(): Boolean

    fun setUsingGridMode(isUsingGridMode: Boolean)
}

class UserDataSourceImpl(private val userPreference: UserPreference) : UserDataSource {
    override fun isUsingGridMode(): Boolean {
        return userPreference.isUsingGridMode()
    }

    override fun setUsingGridMode(isUsingGridMode: Boolean) {
        userPreference.setUsingGridMode(isUsingGridMode)
    }
}
