package com.g1.onetargetsdk.model

import android.support.annotation.Keep
import java.io.Serializable

@Keep
class Input : Serializable {
    var workspaceId: String? = null
    var identityId: String? = null
    var eventName: String? = null
    var eventDate: String? = null
    var eventData: String? = null
}
