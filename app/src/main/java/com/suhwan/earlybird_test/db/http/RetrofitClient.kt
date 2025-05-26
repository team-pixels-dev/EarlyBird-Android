package com.suhwan.earlybird_test.db.http

import com.suhwan.earlybird_test.db.http.model.NpsCommentRequest
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val BASE_URL = "https://earlybirdteam.com/"
    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    //방문 로그
    val visitInstance: VisitApiService by lazy {
        retrofit.create(VisitApiService::class.java)
    }
    val clickInstance: ClickApiService by lazy {
        retrofit.create(ClickApiService::class.java)
    }
    val npsScoreInstance: NpsScoreApiService by lazy {
        retrofit.create(NpsScoreApiService::class.java)
    }
    val npsCommentInstance: NpsCommentApiService by lazy {
        retrofit.create(NpsCommentApiService::class.java)
    }
}