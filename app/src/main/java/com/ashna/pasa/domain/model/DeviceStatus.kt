package com.ashna.pasa.domain.model

/**
 * Device status model
 * Phase 1: Configuration and connectivity only
 */
data class DeviceStatus(
    val deviceId: String,
    val deviceName: String,
    val telegramConfigured: Boolean,
    val registrationComplete: Boolean,
    val batteryLevel: Int,
    val isCharging: Boolean,
    val networkConnected: Boolean
)
