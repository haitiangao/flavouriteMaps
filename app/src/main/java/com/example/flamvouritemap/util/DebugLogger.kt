package com.example.flamvouritemap.util

import android.util.Log
import com.example.flamvouritemap.util.Constants.Companion.ERROR_PREFIX
import com.example.flamvouritemap.util.Constants.Companion.TAG

object DebugLogger {

    @JvmStatic
    fun logError(throwable: Throwable) {
        Log.d(TAG, ERROR_PREFIX + throwable.localizedMessage)
    }

    @JvmStatic
    fun logDebug(message: String) {
        Log.d(TAG, message)
    }


}