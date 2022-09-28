package com.g1.onetargetsdk.model

import androidx.annotation.Keep

@Keep
data class JsonContent(
    val htmlContent: String? = null,
    val jsonContent: JsonContentX? = null,
) : java.io.Serializable
