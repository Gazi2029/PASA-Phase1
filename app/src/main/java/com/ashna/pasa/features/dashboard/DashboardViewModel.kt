package com.ashna.pasa.features.dashboard

import android.app.Application
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.BatteryManager
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.ashna.pasa.data.local.DeviceInfo
import com.ashna.pasa.data.local.PasaPreferences
import com.ashna.pasa.data.local.SecureStorage
import com.ashna.pasa.domain.model.DeviceStatus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DashboardViewModel(application: Application) : AndroidViewModel(application) {

    private val secureStorage = SecureStorage(application)
    private val preferences = PasaPreferences(application)

    private val _deviceStatus = MutableStateFlow<DeviceStatus?>(null)
    val deviceStatus: StateFlow<DeviceStatus?> = _deviceStatus.asStateFlow()

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing.asStateFlow()

    init {
        refreshStatus()
    }

    fun refreshStatus() {
        viewModelScope.launch {
            _isRefreshing.value = true
            try {
                val context = getApplication<Application>()
                val deviceId = preferences.getDeviceId() ?: DeviceInfo.getDeviceId(context)
                val deviceName = preferences.getDeviceName() ?: DeviceInfo.getDeviceName()
                val telegramToken = secureStorage.getTelegramToken()
                val chatId = secureStorage.getChatId()

                val batteryStatus = context.registerReceiver(null, IntentFilter(Intent.ACTION_BATTERY_CHANGED))
                val batteryLevel = batteryStatus?.let {
                    val level = it.getIntExtra(BatteryManager.EXTRA_LEVEL, -1)
                    val scale = it.getIntExtra(BatteryManager.EXTRA_SCALE, -1)
                    (level * 100 / scale.toFloat()).toInt()
                } ?: 0

                val isCharging = batteryStatus?.let {
                    val status = it.getIntExtra(BatteryManager.EXTRA_STATUS, -1)
                    status == BatteryManager.BATTERY_STATUS_CHARGING ||
                            status == BatteryManager.BATTERY_STATUS_FULL
                } ?: false

                val networkConnected = isNetworkConnected(context)

                _deviceStatus.value = DeviceStatus(
                    deviceId = deviceId,
                    deviceName = deviceName,
                    telegramConfigured = !telegramToken.isNullOrEmpty() && !chatId.isNullOrEmpty(),
                    registrationComplete = preferences.isSetupComplete(),
                    batteryLevel = batteryLevel,
                    isCharging = isCharging,
                    networkConnected = networkConnected
                )
            } finally {
                _isRefreshing.value = false
            }
        }
    }

    private fun isNetworkConnected(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
        return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) &&
                capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
    }
}
