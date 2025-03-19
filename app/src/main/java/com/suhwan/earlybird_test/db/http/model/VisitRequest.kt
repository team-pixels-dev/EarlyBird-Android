package com.suhwan.earlybird_test.db.http.model

import com.google.gson.annotations.SerializedName

data class VisitRequest(
    @SerializedName("clientId")
    val clientId: String?
)