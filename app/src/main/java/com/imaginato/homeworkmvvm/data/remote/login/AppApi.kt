package com.imaginato.homeworkmvvm.data.remote.login

import com.imaginato.homeworkmvvm.data.remote.login.requset.LoginRequest
import com.imaginato.homeworkmvvm.data.remote.login.response.LoginResponse
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface AppApi {
    @POST("login")
    fun loginUser(
        @Header("IMSI") imsi: String,
        @Header("IMEI") imei: String,
        @Body loginReq: LoginRequest
    ): Deferred<Response<LoginResponse>>
}