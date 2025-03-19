package com.suhwan.earlybird_test.db.http

import retrofit2.Call
import com.suhwan.earlybird_test.db.http.model.VisitRequest
import retrofit2.http.Body
import retrofit2.http.POST

interface VisitApiService {
    @POST("api/v1/log/visit-event")
    fun visitRequest(@Body json: VisitRequest) : Call<Void>
}