package com.ashna.pasa.domain.model

/**
 * Setup wizard state
 */
data class SetupState(
    val currentStep: SetupStep = SetupStep.WELCOME,
    val deviceId: String = "",
    val deviceName: String = "",
    val botToken: String = "",
    val chatId: String = "",
    val botUsername: String = "",
    val isTestingConnection: Boolean = false,
    val connectionTestPassed: Boolean = false,
    val errorMessage: String? = null
)

enum class SetupStep {
    WELCOME,
    DEVICE_INFO,
    TELEGRAM_CONFIG,
    TEST_CONNECTION,
    COMPLETE
}
