package com.g1.onetargetsdk.model

import androidx.annotation.Keep
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

@Keep
class SOAnswersResponse : Serializable {
    @SerializedName("has_more")
    @Expose
    var hasMore: Boolean? = null

    @SerializedName("backoff")
    @Expose
    var backoff: Int? = null

    @SerializedName("quota_max")
    @Expose
    var quotaMax: Int? = null

    @SerializedName("quota_remaining")
    @Expose
    var quotaRemaining: Int? = null
}
