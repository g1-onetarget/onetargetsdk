package com.g1.onetargetsdk

import com.g1.onetargetsdk.model.IAMResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Created by Loitp on 12.09.2022
 * Galaxy One company,
 * Vietnam
 * +840766040293
 * freuss47@gmail.com
 */
interface OneTargetService {

    @GET("/")
    fun track(
        @Query("workspace_id") workspaceId: String?,
        @Query("identity_id") identityId: String?,
        @Query("event_name") eventName: String?,
        @Query("event_date") eventDate: String?,
        @Query("eventData") eventData: String?,
    ): Call<Void>


    @GET("/push/push/pull/{workspace_id}/{identity_id}/")
    fun checkIAM(
        @Path("workspace_id") workspaceId: String,
        @Path("identity_id") identityId: String,
    ): Call<IAMResponse>
}
