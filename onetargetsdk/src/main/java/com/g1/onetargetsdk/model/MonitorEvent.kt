package com.g1.onetargetsdk.model

import androidx.annotation.Keep
import java.io.Serializable

/**
 * Created by Loitp on 12.09.2022
 * Galaxy One company,
 * Vietnam
 * +840766040293
 * freuss47@gmail.com
 */
@Keep
class MonitorEvent : Serializable {
    var workspaceId: String? = null
    var identityId: HashMap<String, Any>? = null
    var profile: HashMap<String, Any>? = null
    var eventName: String? = null
    var eventDate: Long? = null
    var eventData: HashMap<String, Any>? = null
}
