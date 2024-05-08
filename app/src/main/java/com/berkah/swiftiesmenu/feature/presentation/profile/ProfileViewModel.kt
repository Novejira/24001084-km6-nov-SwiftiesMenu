package com.berkah.swiftiesmenu.feature.presentation.profile

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.berkah.swiftiesmenu.feature.data.model.Profile

class ProfileViewModel: ViewModel(){

    val profileData = MutableLiveData(
        Profile(
            name = "Novejira Angela Pello",
            username = "Nveep",
            email = "LopeyNope@gmail.com",
            profileImg = "https://scontent-cgk1-1.cdninstagram.com/v/t51.2885-19/431279539_952773186386204_7324822393593629659_n.jpg?stp=dst-jpg_s320x320&_nc_ht=scontent-cgk1-1.cdninstagram.com&_nc_cat=108&_nc_ohc=mDNAB1BaPPoAX_4f5An&edm=AOQ1c0wBAAAA&ccb=7-5&oh=00_AfCqkWbs2Uiw_Q_UyMiAAsjds-vMLg3g_BEeZoHlvFnmFA&oe=660E0395&_nc_sid=8b3546"
        )
    )

    val isEditMode = MutableLiveData(false)

    fun changeEditMode() {
        val currentValue = isEditMode.value ?: false
        isEditMode.postValue(!currentValue)
    }
}
