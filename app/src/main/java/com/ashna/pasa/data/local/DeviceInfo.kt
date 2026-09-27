package com.ashna.pasa.data.local

import android.content.Context
import android.os.Build
import android.provider.Settings
import java.util.UUID

/**
 * Device information utilities
 */
object DeviceInfo {

    fun getDeviceId(context: Context): String {
        return try {
            Settings.Secure.getString(
                context.contentResolver,
                Settings.Secure.ANDROID_ID
            )
        } catch (e: Exception) {
            UUID.randomUUID().toString()
        }
    }

    fun getDeviceName(): String {
        val manufacturer = Build.MANUFACTURER
        val model = Build.MODEL
        return if (model.startsWith(manufacturer)) {
            model.capitalize()
        } else {
            "${manufacturer.capitalize()} $model"
        }
    }

    fun getAndroidVersion(): String {
        return "${Build.VERSION.RELEASE} (API ${Build.VERSION.SDK_INT})"
    }

    private fun String.capitalize(): String {
        return this.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
    }
}
