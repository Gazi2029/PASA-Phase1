package com.ashna.pasa.features.setup

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ashna.pasa.domain.model.SetupStep

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SetupScreen(
    onSetupComplete: () -> Unit,
    viewModel: SetupViewModel = viewModel()
) {
    val state by viewModel.state.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("PASA Setup") },
                navigationIcon = {
                    if (state.currentStep != SetupStep.WELCOME) {
                        IconButton(onClick = { viewModel.previousStep() }) {
                            Icon(Icons.Default.ArrowBack, "Back")
                        }
                    }
                }
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
            when (state.currentStep) {
                SetupStep.WELCOME -> WelcomeStep(
                    onNext = { viewModel.nextStep() }
                )
                SetupStep.DEVICE_INFO -> DeviceInfoStep(
                    deviceId = state.deviceId,
                    deviceName = state.deviceName,
                    onNext = { viewModel.nextStep() }
                )
                SetupStep.TELEGRAM_CONFIG -> TelegramConfigStep(
                    botToken = state.botToken,
                    chatId = state.chatId,
                    onBotTokenChange = { viewModel.updateBotToken(it) },
                    onChatIdChange = { viewModel.updateChatId(it) },
                    onNext = { viewModel.nextStep() }
                )
                SetupStep.TEST_CONNECTION -> TestConnectionStep(
                    botUsername = state.botUsername,
                    connectionTestPassed = state.connectionTestPassed,
                    isTestingConnection = state.isTestingConnection,
                    errorMessage = state.errorMessage,
                    onTestConnection = { viewModel.testConnection() },
                    onNext = { viewModel.completeSetup() }
                )
                SetupStep.COMPLETE -> {
                    LaunchedEffect(Unit) {
                        onSetupComplete()
                    }
                }
            }
        }
    }
}

@Composable
fun WelcomeStep(onNext: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Welcome to PASA",
            style = MaterialTheme.typography.headlineLarge
        )
        Text(
            text = "Private Android Security Agent",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "PASA will help you secure and monitor your Android device using Telegram.",
            style = MaterialTheme.typography.bodyLarge
        )
        Spacer(modifier = Modifier.height(8.dp))
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.errorContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.Warning,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.error
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Phase 1 Limitations",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.error
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "This is Phase 1: Setup and configuration only. No remote commands or monitoring yet.",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
        Spacer(modifier = Modifier.weight(1f))
        Button(
            onClick = onNext,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Get Started")
            Spacer(modifier = Modifier.width(8.dp))
            Icon(Icons.Default.ArrowForward, contentDescription = null)
        }
    }
}

@Composable
fun DeviceInfoStep(
    deviceId: String,
    deviceName: String,
    onNext: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Device Information",
            style = MaterialTheme.typography.headlineMedium
        )
        OutlinedCard {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Device Name",
                    style = MaterialTheme.typography.labelMedium
                )
                Text(
                    text = deviceName,
                    style = MaterialTheme.typography.bodyLarge
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Device ID",
                    style = MaterialTheme.typography.labelMedium
                )
                Text(
                    text = deviceId,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
        Spacer(modifier = Modifier.weight(1f))
        Button(
            onClick = onNext,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Continue")
            Icon(Icons.Default.ArrowForward, contentDescription = null)
        }
    }
}

@Composable
fun TelegramConfigStep(
    botToken: String,
    chatId: String,
    onBotTokenChange: (String) -> Unit,
    onChatIdChange: (String) -> Unit,
    onNext: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Telegram Configuration",
            style = MaterialTheme.typography.headlineMedium
        )
        OutlinedTextField(
            value = botToken,
            onValueChange = onBotTokenChange,
            label = { Text("Bot Token") },
            placeholder = { Text("123456:ABC-DEF1234ghIkl-zyx57W2v1u123ew11") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
        )
        OutlinedTextField(
            value = chatId,
            onValueChange = onChatIdChange,
            label = { Text("Chat ID") },
            placeholder = { Text("123456789") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "⚠️ Chat ID Security Warning",
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Entering a Chat ID alone does NOT prove you own that Telegram account. Phase 2 will add proper verification.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
            }
        }
        Spacer(modifier = Modifier.weight(1f))
        Button(
            onClick = onNext,
            modifier = Modifier.fillMaxWidth(),
            enabled = botToken.isNotBlank() && chatId.isNotBlank()
        ) {
            Text("Test Connection")
            Icon(Icons.Default.ArrowForward, contentDescription = null)
        }
    }
}

@Composable
fun TestConnectionStep(
    botUsername: String,
    connectionTestPassed: Boolean,
    isTestingConnection: Boolean,
    errorMessage: String?,
    onTestConnection: () -> Unit,
    onNext: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Test Connection",
            style = MaterialTheme.typography.headlineMedium
        )
        
        if (!connectionTestPassed && !isTestingConnection) {
            Button(
                onClick = onTestConnection,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Test Telegram Connection")
            }
        }
        
        if (isTestingConnection) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
            Text("Testing connection...", modifier = Modifier.align(Alignment.CenterHorizontally))
        }
        
        if (connectionTestPassed) {
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Default.Check,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Connection Successful",
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                    if (botUsername.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Bot: @$botUsername")
                    }
                }
            }
        }
        
        errorMessage?.let { error ->
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer
                )
            ) {
                Text(
                    text = error,
                    modifier = Modifier.padding(16.dp),
                    color = MaterialTheme.colorScheme.onErrorContainer
                )
            }
        }
        
        Spacer(modifier = Modifier.weight(1f))
        
        Button(
            onClick = onNext,
            modifier = Modifier.fillMaxWidth(),
            enabled = connectionTestPassed
        ) {
            Text("Complete Setup")
            Icon(Icons.Default.Check, contentDescription = null)
        }
    }
}
