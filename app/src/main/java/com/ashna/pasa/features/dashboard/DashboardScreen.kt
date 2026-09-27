package com.ashna.pasa.features.dashboard

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BatteryChargingFull
import androidx.compose.material.icons.filled.BatteryFull
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.WifiOff
import androidx.compose.material.icons.outlined.Wifi
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ashna.pasa.ui.components.FeatureCard
import com.ashna.pasa.ui.components.StatusCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    onNavigateToSecurity: () -> Unit,
    viewModel: DashboardViewModel = viewModel()
) {
    val deviceStatus by viewModel.deviceStatus.collectAsState()
    val isRefreshing by viewModel.isRefreshing.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("PASA Dashboard") }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            if (deviceStatus == null) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else {
                val status = deviceStatus!!

                // Device Info Card
                StatusCard(
                    title = "Device",
                    subtitle = status.deviceName,
                    icon = Icons.Default.Security
                )

                // Telegram Status
                StatusCard(
                    title = "Telegram",
                    subtitle = if (status.telegramConfigured) "Configured" else "Not Configured",
                    icon = Icons.Default.Security,
                    containerColor = if (status.telegramConfigured)
                        MaterialTheme.colorScheme.primaryContainer
                    else
                        MaterialTheme.colorScheme.errorContainer
                )

                // Network Status
                StatusCard(
                    title = "Network",
                    subtitle = if (status.networkConnected) "Connected" else "Disconnected",
                    icon = if (status.networkConnected) Icons.Outlined.Wifi else Icons.Default.WifiOff,
                    containerColor = if (status.networkConnected)
                        MaterialTheme.colorScheme.primaryContainer
                    else
                        MaterialTheme.colorScheme.errorContainer
                )

                // Battery Status
                StatusCard(
                    title = "Battery",
                    subtitle = "${status.batteryLevel}%" + if (status.isCharging) " (Charging)" else "",
                    icon = if (status.isCharging) Icons.Default.BatteryChargingFull else Icons.Default.BatteryFull
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Security Status Feature Card
                FeatureCard(
                    title = "Security Status",
                    description = "View device security configuration",
                    icon = Icons.Default.Security,
                    onClick = onNavigateToSecurity
                )

                // Refresh Button
                Button(
                    onClick = { viewModel.refreshStatus() },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !isRefreshing
                ) {
                    if (isRefreshing) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                    }
                    Text(if (isRefreshing) "Refreshing..." else "Refresh Status")
                }
            }
        }
    }
}
