package com.suhwan.earlybird_test.db.http

import com.suhwan.earlybird_test.db.http.model.NpsScoreRequest
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface NpsScoreApiService {
    @POST("api/v1/feedbacks/scores")
    fun npsScoreRequest(@Body json: NpsScoreRequest) : Call<Void>
}