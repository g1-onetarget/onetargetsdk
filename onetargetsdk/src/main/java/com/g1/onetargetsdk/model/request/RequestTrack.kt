package com.g1.onetargetsdk.model.request

import androidx.annotation.Keep

@Keep
data class RequestTrack(
    var eventData: String? = null,
    var event_date: Long? = null,
    var event_name: String? = null,
    var identity_id: String? = null,
    var profile: String? = null,
    var workspace_id: String? = null,
) : java.io.Serializable
