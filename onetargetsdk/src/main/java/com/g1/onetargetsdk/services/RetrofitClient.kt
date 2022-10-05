package com.g1.onetargetsdk.services

import android.util.Log
import com.moczul.ok2curl.CurlInterceptor
import com.moczul.ok2curl.logger.Logger
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Created by Loitp on 12.09.2022
 * Galaxy One company,
 * Vietnam
 * +840766040293
 * freuss47@gmail.com
 */
object RetrofitClient {
    private var retrofitTracking: Retrofit? = null
    private var retrofitIAM: Retrofit? = null

    private fun getClient(retrofit: Retrofit?, baseUrl: String, isShowLogAPI: Boolean?): Retrofit {
        var tmpRetrofit = retrofit
        if (tmpRetrofit == null) {
            val interceptor = HttpLoggingInterceptor()
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)

            val builder = OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
            if (isShowLogAPI == true) {
                builder.addInterceptor(interceptor)
                builder.addInterceptor(CurlInterceptor(object : Logger {
                    override fun log(message: String) {
                        Log.e("CurlInterceptor", message)
                    }
                }))
            }
            val client = builder.build()

            tmpRetrofit = Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
        return tmpRetrofit!!
    }

    fun getClientTracking(baseUrl: String, isShowLogAPI: Boolean?): Retrofit {
        return getClient(retrofitTracking, baseUrl, isShowLogAPI)
    }

    fun getClientIAM(baseUrl: String, isShowLogAPI: Boolean?): Retrofit {
        return getClient(retrofitIAM, baseUrl, isShowLogAPI)
    }
}
