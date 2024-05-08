package com.berkah.swiftiesmenu.feature.data.repository

import com.berkah.swiftiesmenu.feature.data.datasource.user.UserDataSource

interface PreferenceRepository {
    fun isUsingGridMode(): Boolean

    fun setUsingGridMode(isUsingGridMode: Boolean)
}

class PreferenceRepositoryImpl(private val dataSource: UserDataSource) : PreferenceRepository {
    override fun isUsingGridMode(): Boolean {
        return dataSource.isUsingGridMode()
    }

    override fun setUsingGridMode(isUsingGridMode: Boolean) {
        dataSource.setUsingGridMode(isUsingGridMode)
    }
}
