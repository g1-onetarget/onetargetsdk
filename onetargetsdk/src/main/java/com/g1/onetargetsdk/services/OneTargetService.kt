package com.g1.onetargetsdk.services

import com.g1.onetargetsdk.model.IAMResponse
import com.g1.onetargetsdk.model.request.RequestTrack
import retrofit2.Call
import retrofit2.http.*

/**
 * Created by Loitp on 12.09.2022
 * Galaxy One company,
 * Vietnam
 * +840766040293
 * freuss47@gmail.com
 */
interface OneTargetService {

    @Suppress("unused")
    @GET("/")
    fun trackGet(
        @Query("workspace_id") workspaceId: String?,
        @Query("identity_id") identityId: String?,
        @Query("profile") profile: String?,
        @Query("event_name") eventName: String?,
        @Query("event_date") eventDate: String?,
        @Query("eventData") eventData: String?,
    ): Call<Void>

    @POST("/")
    fun trackPost(
        @Body body: RequestTrack
    ): Call<Void>

    @POST("/tracking/tracking/events/")
    fun trackPostStg(
        @Body body: RequestTrack
    ): Call<Void>

    @GET("/push/push/pull/{workspace_id}/{identity_id}/")
    fun checkIAM(
        @Path("workspace_id") workspaceId: String,
        @Path("identity_id") identityId: String,
    ): Call<IAMResponse>
}
