package com.suhwan.earlybird_test.db.http.model

import com.google.gson.annotations.SerializedName

data class NpsCommentRequest (
    @SerializedName("comment")
    val comment : String?,
    @SerializedName("clientId")
    val clientId : String?,
    @SerializedName("createdAt")
    val createdAt : String?
)