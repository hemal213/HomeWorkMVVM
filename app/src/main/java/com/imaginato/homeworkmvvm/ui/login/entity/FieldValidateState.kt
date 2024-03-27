package com.imaginato.homeworkmvvm.ui.login.entity

// Field Validation State and Progress state
data class FieldValidateState(
    val isValid: Boolean,
    val msg: String,
    val status: Status
)

