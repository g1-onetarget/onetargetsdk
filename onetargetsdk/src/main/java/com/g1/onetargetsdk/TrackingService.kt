package com.g1.onetargetsdk

import retrofit2.Call
import retrofit2.http.GET

interface TrackingService {
    @get:GET("/answers?order=desc&sort=activity&site=stackoverflow")
    val answers: Call<Any>
}
