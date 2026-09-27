package com.ashna.pasa.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ashna.pasa.domain.model.SecurityStatusLevel

@Composable
fun SecurityStatusItem(
    statusItem: com.ashna.pasa.domain.model.SecurityStatusItem,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = when (statusItem.status) {
                SecurityStatusLevel.OK -> MaterialTheme.colorScheme.primaryContainer
                SecurityStatusLevel.WARNING -> MaterialTheme.colorScheme.errorContainer
                SecurityStatusLevel.INFO -> MaterialTheme.colorScheme.secondaryContainer
            }
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = when (statusItem.status) {
                    SecurityStatusLevel.OK -> Icons.Default.CheckCircle
                    SecurityStatusLevel.WARNING -> Icons.Default.Warning
                    SecurityStatusLevel.INFO -> Icons.Default.Info
                },
                contentDescription = null,
                tint = when (statusItem.status) {
                    SecurityStatusLevel.OK -> MaterialTheme.colorScheme.primary
                    SecurityStatusLevel.WARNING -> MaterialTheme.colorScheme.error
                    SecurityStatusLevel.INFO -> MaterialTheme.colorScheme.secondary
                },
                modifier = Modifier.size(32.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    text = statusItem.title,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = statusItem.description,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}
