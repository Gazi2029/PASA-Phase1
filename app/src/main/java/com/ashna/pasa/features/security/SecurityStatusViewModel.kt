package com.ashna.pasa.features.security

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.ashna.pasa.data.local.PasaPreferences
import com.ashna.pasa.data.local.SecureStorage
import com.ashna.pasa.domain.model.SecurityStatus
import com.ashna.pasa.domain.model.SecurityStatusItem
import com.ashna.pasa.domain.model.SecurityStatusLevel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SecurityStatusViewModel(application: Application) : AndroidViewModel(application) {

    private val secureStorage = SecureStorage(application)
    private val preferences = PasaPreferences(application)

    private val _securityStatus = MutableStateFlow<SecurityStatus?>(null)
    val securityStatus: StateFlow<SecurityStatus?> = _securityStatus.asStateFlow()

    init {
        checkSecurityStatus()
    }

    fun checkSecurityStatus() {
        viewModelScope.launch {
            val items = mutableListOf<SecurityStatusItem>()

            // Check Telegram configuration
            val hasToken = !secureStorage.getTelegramToken().isNullOrEmpty()
            val hasChatId = !secureStorage.getChatId().isNullOrEmpty()
            val telegramConfigured = hasToken && hasChatId

            items.add(
                SecurityStatusItem(
                    title = "Telegram Bot",
                    description = if (telegramConfigured) "Configured" else "Not configured",
                    status = if (telegramConfigured) SecurityStatusLevel.OK else SecurityStatusLevel.WARNING
                )
            )

            // Check setup completion
            val setupComplete = preferences.isSetupComplete()
            items.add(
                SecurityStatusItem(
                    title = "Initial Setup",
                    description = if (setupComplete) "Complete" else "Incomplete",
                    status = if (setupComplete) SecurityStatusLevel.OK else SecurityStatusLevel.WARNING
                )
            )

            // Phase 1: Configuration checks only
            items.add(
                SecurityStatusItem(
                    title = "Security Monitoring",
                    description = "Phase 2 feature (not implemented)",
                    status = SecurityStatusLevel.INFO
                )
            )

            val overallLevel = when {
                items.any { it.status == SecurityStatusLevel.WARNING } -> SecurityStatusLevel.WARNING
                items.all { it.status == SecurityStatusLevel.OK } -> SecurityStatusLevel.OK
                else -> SecurityStatusLevel.INFO
            }

            val message = when (overallLevel) {
                SecurityStatusLevel.OK -> "All configuration checks passed"
                SecurityStatusLevel.WARNING -> "Configuration incomplete"
                SecurityStatusLevel.INFO -> "Review configuration status"
            }

            _securityStatus.value = SecurityStatus(
                level = overallLevel,
                message = message,
                items = items
            )
        }
    }
}
