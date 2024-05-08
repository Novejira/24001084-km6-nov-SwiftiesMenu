package com.berkah.swiftiesmenu.feature.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.UUID

@Parcelize
data class Menu(
    var id: String? = UUID.randomUUID().toString(),
    var name: String,
    var imgUrl: String,
    var desc: String,
    val price: Double,
    val addres: String,
) : Parcelable
