package com.imaginato.homeworkmvvm.data.remote.login

import com.imaginato.homeworkmvvm.data.remote.login.requset.LoginRequest
import com.imaginato.homeworkmvvm.data.remote.login.response.LoginResponse
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

interface ApiRepository {
    suspend fun loginUser(
        imsi: String,
        imei: String,
        loginRequest: LoginRequest
    ): Flow<Response<LoginResponse>>
}