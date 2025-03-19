package com.suhwan.earlybird_test.db.http

import com.suhwan.earlybird_test.db.http.model.NpsCommentRequest
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface NpsCommentApiService {
    @POST("api/v1/feedbacks/comments")
    fun npsCommentRequest(@Body json : NpsCommentRequest) : Call<Void>
}