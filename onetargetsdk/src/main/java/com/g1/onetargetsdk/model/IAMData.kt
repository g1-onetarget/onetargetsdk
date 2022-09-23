package com.g1.onetargetsdk.model

import androidx.annotation.Keep

@Keep
data class IAMData(
    val actionClick: String? = null,
    val activeType: String? = null,
    val activeValue: Double? = null,
    val closingAfter: Double? = null,
    val message: String? = null,
    val name: String? = null,
)