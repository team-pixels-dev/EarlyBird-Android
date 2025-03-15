package com.suhwan.earlybird_test.db.http

import com.suhwan.earlybird_test.db.http.model.ClickRequest
import com.suhwan.earlybird_test.db.http.model.VisitRequest
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ClickApiService {
    @POST("api/v1/log/click")
    fun clickRequest(@Body json: ClickRequest) : Call<Void>
}