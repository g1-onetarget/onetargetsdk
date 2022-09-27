package com.g1.onetargetsdk.model

import androidx.annotation.Keep
import java.io.Serializable

@Keep
data class IAMData(
    val actionClick: String? = null,
    val activeType: String? = null,
    val activeValue: Double? = null,
    val closingAfter: Double? = null,
    val message: String? = null,
    val name: String? = null,
) : Serializable

//activeType: Có 3 option
const val IMMEDIATELY = "IMMEDIATELY" //Hiển thị ngay lập tức
const val TIME = "TIME" //Hiện thị sau 1 khoảng thời gian
const val SCROLL_PERCENTAGE = "SCROLL_PERCENTAGE" //Hiện thị sau khi scroll theo phần trăm
