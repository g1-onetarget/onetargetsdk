package com.g1.onetargetsdk.model

import androidx.annotation.Keep
import java.io.Serializable

@Keep
class Input : Serializable {
    var workspaceId: String? = null
    var identityId: HashMap<String, Any>? = null
    var eventName: String? = null
    var eventDate: Long? = null
    var eventData: HashMap<String, Any>? = null
}
