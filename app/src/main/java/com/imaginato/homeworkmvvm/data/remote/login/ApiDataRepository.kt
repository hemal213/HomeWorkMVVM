package com.imaginato.homeworkmvvm.data.remote.login

import com.imaginato.homeworkmvvm.data.remote.login.requset.LoginRequest
import com.imaginato.homeworkmvvm.data.remote.login.response.LoginResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.Response

class ApiDataRepository(private var api: AppApi) : ApiRepository {
    /***
     * Login User API call
     * @param imsi  -   imsi number
     * @param imei  -   imei number
     * @param loginRequest  -   Login API request
     */
    override suspend fun loginUser(imsi: String, imei: String, loginRequest: LoginRequest): Flow<Response<LoginResponse>> = flow {
        val response = api.loginUser(imsi, imei, loginRequest).await()
        emit(response)
    }.flowOn(Dispatchers.IO)
}