package com.berkah.swiftiesmenu.feature.data.source.network.model.menu

import com.google.gson.annotations.SerializedName

data class MenuItemResponse(
    @SerializedName("image_url")
    val imgUrl: String?,
    @SerializedName("nama")
    val name: String?,
    @SerializedName("harga_format")
    val formattedprice: String?,
    @SerializedName("harga")
    val price: Double?,
    @SerializedName("detail")
    val desc: String?,
    @SerializedName("alamat_resto")
    val address: String?,
)
