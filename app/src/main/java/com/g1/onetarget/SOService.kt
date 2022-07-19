package com.g1.onetarget

import com.g1.onetarget.model.SOAnswersResponse
import retrofit2.Call
import retrofit2.http.GET

interface SOService {
    @get:GET("/answers?order=desc&sort=activity&site=stackoverflow")
    val answers: Call<SOAnswersResponse>
}
