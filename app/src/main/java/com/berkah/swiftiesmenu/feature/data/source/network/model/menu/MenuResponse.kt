package com.berkah.swiftiesmenu.feature.data.source.network.model.menu

import com.berkah.swiftiesmenu.feature.data.source.network.model.menu.MenuItemResponse
import com.google.gson.annotations.SerializedName

data class MenuResponse(
    @SerializedName("status")
    val status: Boolean?,
    @SerializedName("code")
    val code: Int?,
    @SerializedName("message")
    val message: String?,
    @SerializedName("data")
    val data: List<MenuItemResponse>?,
)
