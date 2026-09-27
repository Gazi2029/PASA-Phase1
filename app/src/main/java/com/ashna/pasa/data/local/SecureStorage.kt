package com.ashna.pasa.data.local

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey

/**
 * Secure storage using Android Keystore
 */
class SecureStorage(context: Context) {

    private val masterKey = MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    private val sharedPreferences = EncryptedSharedPreferences.create(
        context,
        "pasa_secure_prefs",
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    fun saveTelegramToken(token: String) {
        sharedPreferences.edit().putString(KEY_TELEGRAM_TOKEN, token).apply()
    }

    fun getTelegramToken(): String? {
        return sharedPreferences.getString(KEY_TELEGRAM_TOKEN, null)
    }

    fun saveChatId(chatId: String) {
        sharedPreferences.edit().putString(KEY_CHAT_ID, chatId).apply()
    }

    fun getChatId(): String? {
        return sharedPreferences.getString(KEY_CHAT_ID, null)
    }

    fun clearAll() {
        sharedPreferences.edit().clear().apply()
    }

    companion object {
        private const val KEY_TELEGRAM_TOKEN = "telegram_bot_token"
        private const val KEY_CHAT_ID = "telegram_chat_id"
    }
}
