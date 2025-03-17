package com.suhwan.earlybird_test.db.http.model

import com.google.gson.annotations.SerializedName

data class NpsScoreRequest(
    @SerializedName("score")
    val score : Int,
    @SerializedName("clientId")
    val clientId : String?,
    @SerializedName("createdAt")
    val createAt : String?,
    @SerializedName("dayCount")
    val dayCount : Int,
)
