package com.imaginato.homeworkmvvm.ui.login.entity

// Remote API state
data class RemoteAPIState(
    val status: Status,
    val data: Any?,
    val apiStatus: Status,
) {
    companion object {
        @JvmStatic
        fun loading(apiStatus: Status): RemoteAPIState {
            return RemoteAPIState(Status.LOADING, null, apiStatus);
        }

        @JvmStatic
        fun success(data: Any, apiStatus: Status): RemoteAPIState {
            return RemoteAPIState(Status.SUCCESS, data, apiStatus);
        }

        @JvmStatic
        fun fail(data: Any, apiStatus: Status): RemoteAPIState {
            return RemoteAPIState(Status.FAIL, data, apiStatus);
        }
    }
}