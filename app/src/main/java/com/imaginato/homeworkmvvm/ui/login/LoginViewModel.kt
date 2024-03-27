package com.imaginato.homeworkmvvm.ui.login

import android.annotation.SuppressLint
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.imaginato.homeworkmvvm.data.local.login.AppDBDao
import com.imaginato.homeworkmvvm.data.local.login.AppUser
import com.imaginato.homeworkmvvm.data.remote.login.ApiRepository
import com.imaginato.homeworkmvvm.data.remote.login.requset.LoginRequest
import com.imaginato.homeworkmvvm.ui.base.BaseViewModel
import com.imaginato.homeworkmvvm.ui.login.entity.FieldValidateState
import com.imaginato.homeworkmvvm.ui.login.entity.RemoteAPIState
import com.imaginato.homeworkmvvm.ui.login.entity.Status
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.core.component.KoinApiExtension
import org.koin.core.component.inject

@KoinApiExtension
class LoginViewModel : BaseViewModel() {
    private val repo: ApiRepository by inject()
    private val dao: AppDBDao by inject()

    var verifyData = MutableLiveData<FieldValidateState>()
    var remoteAPIState = MutableLiveData<RemoteAPIState>()

    /***
     * Validate form before making API call
     */
    fun validateForm(userName: String, password: String) {
        if (userName.isEmpty()) {
            verifyData.value =
                FieldValidateState(false, "Please enter username.", Status.ERROR_USER_NAME)
        } else if (password.isEmpty()) {
            verifyData.value =
                FieldValidateState(true, "Please enter password.", Status.ERROR_PASSWORD)
        } else {
            verifyData.value = FieldValidateState(true, "", Status.SUCCESS)
        }
    }

    /***
     * Login user API call
     */
    @SuppressLint("LogNotTimber")
    fun loginUser(imsi: String, imei: String, userName: String, password: String) {
        val req = LoginRequest(userName, password)

        viewModelScope.launch {
            repo.loginUser(imsi, imei, req)
                .onStart {
                    verifyData.value = FieldValidateState(true, "", Status.SHOW_PROGRESS)
                    remoteAPIState.value = RemoteAPIState.loading(Status.LOGIN_API)
                }.catch {
                    verifyData.value = FieldValidateState(false, "", Status.SHOW_PROGRESS)
                    remoteAPIState.value = RemoteAPIState.fail(it.toString(), Status.LOGIN_API)
                }.onCompletion {
                }.collect {
                    // Hide Loader
                    verifyData.value = FieldValidateState(false, "", Status.SHOW_PROGRESS)

                    /// Get API call response
                    remoteAPIState.value = if (it.isSuccessful) {
                        if (it.body() !== null && it.body()?.errorCode == "00") {
                            // Save user data in database with token
                            it.body()?.data?.let { user ->
                                // Get User token from response header
                                val token = try {
                                    it.headers()["X-Acc"]
                                } catch (e: Exception) {
                                    ""
                                }

                                // Create user details model
                                val appUser =
                                    AppUser(
                                        user.userId!!,
                                        user.userName!!,
                                        user.isDeleted!!,
                                        token!!
                                    )

                                // Save model in database
                                withContext(Dispatchers.IO) {
                                    this@LoginViewModel.dao.insertUser(
                                        appUser
                                    )
                                }
                            }

                            RemoteAPIState.success(it.body()!!, Status.LOGIN_API)
                        } else {
                            RemoteAPIState.fail(it.message(), Status.LOGIN_API)
                        }
                    } else {
                        RemoteAPIState.fail(it.errorBody().toString(), Status.LOGIN_API)
                    }
                }
        }
    }
}