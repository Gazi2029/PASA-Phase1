package com.ashna.pasa.features.setup

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.ashna.pasa.data.local.DeviceInfo
import com.ashna.pasa.data.local.PasaPreferences
import com.ashna.pasa.data.local.SecureStorage
import com.ashna.pasa.data.telegram.TelegramApi
import com.ashna.pasa.domain.model.SetupState
import com.ashna.pasa.domain.model.SetupStep
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SetupViewModel(application: Application) : AndroidViewModel(application) {

    private val secureStorage = SecureStorage(application)
    private val preferences = PasaPreferences(application)

    private val _state = MutableStateFlow(SetupState())
    val state: StateFlow<SetupState> = _state.asStateFlow()

    init {
        loadInitialDeviceInfo()
    }

    private fun loadInitialDeviceInfo() {
        val deviceId = DeviceInfo.getDeviceId(getApplication())
        val deviceName = DeviceInfo.getDeviceName()

        preferences.setDeviceId(deviceId)
        preferences.setDeviceName(deviceName)

        _state.value = _state.value.copy(
            deviceId = deviceId,
            deviceName = deviceName
        )
    }

    fun updateBotToken(token: String) {
        _state.value = _state.value.copy(
            botToken = token,
            errorMessage = null
        )
    }

    fun updateChatId(chatId: String) {
        _state.value = _state.value.copy(
            chatId = chatId,
            errorMessage = null
        )
    }

    fun testConnection() {
        val token = _state.value.botToken.trim()

        if (token.isEmpty()) {
            _state.value = _state.value.copy(
                errorMessage = "Bot token is required"
            )
            return
        }

        _state.value = _state.value.copy(
            isTestingConnection = true,
            errorMessage = null
        )

        viewModelScope.launch {
            val api = TelegramApi(token)
            val result = api.testConnection()

            result.fold(
                onSuccess = { botInfo ->
                    _state.value = _state.value.copy(
                        isTestingConnection = false,
                        connectionTestPassed = true,
                        botUsername = botInfo.username,
                        errorMessage = null
                    )
                },
                onFailure = {
                    _state.value = _state.value.copy(
                        isTestingConnection = false,
                        connectionTestPassed = false,
                        errorMessage = "Connection failed. Check your bot token."
                    )
                }
            )
        }
    }

    fun completeSetup() {
        val chatId = _state.value.chatId.trim()

        if (chatId.isEmpty()) {
            _state.value = _state.value.copy(
                errorMessage = "Chat ID is required"
            )
            return
        }

        secureStorage.saveTelegramToken(_state.value.botToken)
        secureStorage.saveChatId(chatId)
        preferences.setSetupComplete(true)

        _state.value = _state.value.copy(
            currentStep = SetupStep.COMPLETE
        )
    }

    fun nextStep() {
        val currentStep = _state.value.currentStep
        val nextStep = when (currentStep) {
            SetupStep.WELCOME -> SetupStep.DEVICE_INFO
            SetupStep.DEVICE_INFO -> SetupStep.TELEGRAM_CONFIG
            SetupStep.TELEGRAM_CONFIG -> SetupStep.TEST_CONNECTION
            SetupStep.TEST_CONNECTION -> SetupStep.COMPLETE
            SetupStep.COMPLETE -> SetupStep.COMPLETE
        }

        _state.value = _state.value.copy(
            currentStep = nextStep,
            errorMessage = null
        )
    }

    fun previousStep() {
        val currentStep = _state.value.currentStep
        val prevStep = when (currentStep) {
            SetupStep.WELCOME -> SetupStep.WELCOME
            SetupStep.DEVICE_INFO -> SetupStep.WELCOME
            SetupStep.TELEGRAM_CONFIG -> SetupStep.DEVICE_INFO
            SetupStep.TEST_CONNECTION -> SetupStep.TELEGRAM_CONFIG
            SetupStep.COMPLETE -> SetupStep.TEST_CONNECTION
        }

        _state.value = _state.value.copy(
            currentStep = prevStep,
            errorMessage = null
        )
    }
}
