package com.ashna.pasa.features.security

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ashna.pasa.domain.model.SecurityStatusLevel
import com.ashna.pasa.ui.components.SecurityStatusItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SecurityStatusScreen(
    onNavigateBack: () -> Unit,
    viewModel: SecurityStatusViewModel = viewModel()
) {
    val securityStatus by viewModel.securityStatus.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Security Status") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
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
            if (securityStatus == null) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else {
                val status = securityStatus!!

                // Overall Status Card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = when (status.level) {
                            SecurityStatusLevel.OK -> MaterialTheme.colorScheme.primaryContainer
                            SecurityStatusLevel.WARNING -> MaterialTheme.colorScheme.errorContainer
                            SecurityStatusLevel.INFO -> MaterialTheme.colorScheme.secondaryContainer
                        }
                    )
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = status.message,
                            style = MaterialTheme.typography.titleMedium,
                            color = when (status.level) {
                                SecurityStatusLevel.OK -> MaterialTheme.colorScheme.onPrimaryContainer
                                SecurityStatusLevel.WARNING -> MaterialTheme.colorScheme.onErrorContainer
                                SecurityStatusLevel.INFO -> MaterialTheme.colorScheme.onSecondaryContainer
                            }
                        )
                    }
                }

                // Individual Status Items
                status.items.forEach { item ->
                    SecurityStatusItem(statusItem = item)
                }

                // Refresh Button
                Button(
                    onClick = { viewModel.checkSecurityStatus() },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Refresh Status")
                }
            }
        }
    }
}
