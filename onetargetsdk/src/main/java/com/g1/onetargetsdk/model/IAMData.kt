package com.g1.onetargetsdk.model

import androidx.annotation.Keep
import java.io.Serializable

@Keep
data class IAMData(
    val actionClick: String? = null,
    val activeType: String? = null,
    val activeValue: Double? = null,//Giá trị thời gian (giây)/Phần trăm (%) cho (TIME, SCROLL_PERCENTAGE)
    var closingAfter: Double? = null,//Tự tắt popup nếu giá trị > 0 (Tính theo giây), bằng 0 là user tự tắt.
    val message: String? = null,//Nội dung HMTL cần hiển thị
    val name: String? = null,//Tên Journey
) : Serializable

//activeType: Có 3 option
const val IMMEDIATELY = "IMMEDIATELY" //Hiển thị ngay lập tức
const val TIME = "TIME" //Hiện thị sau 1 khoảng thời gian
const val SCROLL_PERCENTAGE = "SCROLL_PERCENTAGE" //Hiện thị sau khi scroll theo phần trăm
