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

    fun getClientTracking(
        baseUrl: String,
        isShowLogAPI: Boolean?,
        timeout: Long,
        onMsg: ((
            msg: String,
        ) -> Unit)? = null,
    ): Retrofit {
        if (retrofitTracking == null) {
            val interceptor = HttpLoggingInterceptor()
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)

            val builder = OkHttpClient.Builder()
                .connectTimeout(timeout, TimeUnit.SECONDS)
                .readTimeout(timeout, TimeUnit.SECONDS)
            if (isShowLogAPI == true) {
                builder.addInterceptor(interceptor)
                builder.addInterceptor(CurlInterceptor(object : Logger {
                    override fun log(message: String) {
                        Log.e("CurlInterceptor", message)
                        onMsg?.invoke(message)
                    }
                }))
            }
            val client = builder.build()

            retrofitTracking = Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
        return retrofitTracking!!
    }

    fun getClientIAM(
        baseUrl: String, isShowLogAPI: Boolean?,
        timeout: Long,
        onMsg: ((
            msg: String,
        ) -> Unit)? = null,
    ): Retrofit {
        if (retrofitIAM == null) {
            val interceptor = HttpLoggingInterceptor()
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)

            val builder = OkHttpClient.Builder()
                .connectTimeout(timeout, TimeUnit.SECONDS)
                .readTimeout(timeout, TimeUnit.SECONDS)
            if (isShowLogAPI == true) {
                builder.addInterceptor(interceptor)
                builder.addInterceptor(CurlInterceptor(object : Logger {
                    override fun log(message: String) {
                        Log.e("CurlInterceptor", message)
                        onMsg?.invoke(message)
                    }
                }))
            }
            val client = builder.build()

            retrofitIAM = Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
        return retrofitIAM!!
    }
}
