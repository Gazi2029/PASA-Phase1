package com.ashna.pasa.data.telegram

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.util.concurrent.TimeUnit

/**
 * Telegram Bot API client
 * Phase 1: Connection test ONLY
 */
class TelegramApi(private val botToken: String) {

    private val client = OkHttpClient.Builder()
        .connectTimeout(10, TimeUnit.SECONDS)
        .readTimeout(10, TimeUnit.SECONDS)
        .writeTimeout(10, TimeUnit.SECONDS)
        .build()

    private val baseUrl = "https://api.telegram.org/bot$botToken"

    /**
     * Test connection to Telegram Bot API
     * Phase 1: This is the ONLY Telegram method implemented
     */
    suspend fun testConnection(): Result<BotInfo> = withContext(Dispatchers.IO) {
        try {
            val request = Request.Builder()
                .url("$baseUrl/getMe")
                .get()
                .build()

            val response = client.newCall(request).execute()
            val body = response.body?.string() ?: ""

            if (!response.isSuccessful) {
                Log.w(TAG, "Telegram API returned HTTP ${response.code}")
                return@withContext Result.failure(
                    Exception("Connection failed")
                )
            }

            val json = JSONObject(body)
            if (!json.getBoolean("ok")) {
                Log.w(TAG, "Telegram API returned ok=false")
                return@withContext Result.failure(
                    Exception("Connection failed")
                )
            }

            val result = json.getJSONObject("result")
            val botInfo = BotInfo(
                id = result.getLong("id"),
                firstName = result.getString("first_name"),
                username = result.optString("username", "")
            )

            Log.i(TAG, "Bot connection successful: @${botInfo.username}")
            Result.success(botInfo)

        } catch (e: Exception) {
            Log.w(TAG, "Telegram API error")
            Result.failure(e)
        }
    }

    companion object {
        private const val TAG = "TelegramApi"
    }
}
