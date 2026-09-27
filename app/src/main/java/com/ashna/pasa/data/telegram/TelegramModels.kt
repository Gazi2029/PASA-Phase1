package com.ashna.pasa.data.telegram

/**
 * Telegram Bot API data models
 */
data class BotInfo(
    val id: Long,
    val firstName: String,
    val username: String
)

data class TelegramMessage(
    val messageId: Long,
    val chatId: Long,
    val text: String?,
    val from: TelegramUser?
)

data class TelegramUser(
    val id: Long,
    val firstName: String,
    val username: String?
)
