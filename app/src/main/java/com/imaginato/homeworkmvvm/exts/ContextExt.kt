package com.imaginato.homeworkmvvm.exts

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.provider.Settings
import android.provider.Settings.Secure.ANDROID_ID
import java.util.UUID


/***
 * Get Device IMEI number
 */
@SuppressLint("HardwareIds")
fun Context.getIMEI(): String {
    val uniquePseudoID =
        "35" +
                Build.BOARD.length % 10 +
                Build.BRAND.length % 10 +
                Build.DEVICE.length % 10 +
                Build.DISPLAY.length % 10 +
                Build.HOST.length % 10 +
                Build.ID.length % 10 +
                Build.MANUFACTURER.length % 10 +
                Build.MODEL.length % 10 +
                Build.PRODUCT.length % 10 +
                Build.TAGS.length % 10 +
                Build.TYPE.length % 10 +
                Build.USER.length % 10

    val serial = Build.getRadioVersion()

    return UUID(uniquePseudoID.hashCode().toLong(), serial.hashCode().toLong()).toString();
}

/****
 * Get IMSI Number
 */
@SuppressLint("HardwareIds")
fun Context.getIMSI(): String {
    return Settings.Secure.getString(contentResolver, ANDROID_ID)
}