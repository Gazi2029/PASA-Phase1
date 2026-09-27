package com.ashna.pasa.domain.model

/**
 * Security status model
 * Phase 1: Configuration checks only
 */
data class SecurityStatus(
    val level: SecurityStatusLevel,
    val message: String,
    val items: List<SecurityStatusItem>
)

enum class SecurityStatusLevel {
    OK,
    INFO,
    WARNING
}

data class SecurityStatusItem(
    val title: String,
    val description: String,
    val status: SecurityStatusLevel
)
