package com.ashna.pasa.util

import android.content.Context
import android.widget.Toast

fun Context.showToast(message: String, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, message, duration).show()
}

fun String.isValidBotToken(): Boolean {
    return this.matches(Regex("^[0-9]{8,10}:[a-zA-Z0-9_-]{35}\$"))
}

fun String.isValidChatId(): Boolean {
    return this.matches(Regex("^-?[0-9]+\$"))
}
