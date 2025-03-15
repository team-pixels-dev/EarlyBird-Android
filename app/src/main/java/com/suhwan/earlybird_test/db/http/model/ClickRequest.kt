package com.suhwan.earlybird_test.db.http.model

import com.google.gson.annotations.SerializedName

data class ClickRequest(
    @SerializedName("clientId")
    val clientId: String?,
    @SerializedName("clickType")
    val clickType: String?,
    @SerializedName("clickTime")
    val clickTime: String?
)
