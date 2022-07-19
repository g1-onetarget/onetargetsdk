package com.g1.onetargetsdk.model

import androidx.annotation.Keep
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

@Keep
class Owner : Serializable {
    @SerializedName("reputation")
    @Expose
    var reputation: Int? = null

    @SerializedName("user_id")
    @Expose
    var userId: Int? = null

    @SerializedName("user_type")
    @Expose
    var userType: String? = null

    @SerializedName("profile_image")
    @Expose
    var profileImage: String? = null

    @SerializedName("display_name")
    @Expose
    var displayName: String? = null

    @SerializedName("link")
    @Expose
    var link: String? = null

    @SerializedName("accept_rate")
    @Expose
    var acceptRate: Int? = null
}
