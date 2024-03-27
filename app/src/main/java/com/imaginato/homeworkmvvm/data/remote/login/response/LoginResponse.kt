package com.imaginato.homeworkmvvm.data.remote.login.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @SerializedName("errorCode") @Expose var errorCode: String? = null,
    @SerializedName("errorMessage") @Expose var errorMessage: String? = null,
    @SerializedName("data") @Expose var data: User? = User()
)

data class User(
    @SerializedName("userId") @Expose var userId: String? = null,
    @SerializedName("userName") @Expose var userName: String? = null,
    @SerializedName("isDeleted") @Expose var isDeleted: Boolean? = null
)