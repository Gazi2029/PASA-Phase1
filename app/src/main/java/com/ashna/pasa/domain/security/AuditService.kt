package com.ashna.pasa.domain.security

/**
 * Audit service for security events.
 *
 * Records security-relevant events for audit trail.
 * Events are validated to prevent logging secrets.
 *
 * Phase 2A: Logging foundation. Persistent storage in Phase 2B+.
 */
interface AuditService {
    /**
     * Record a security event.
     *
     * @param event SecurityEvent to record
     * @throws SecurityException if event contains suspicious data
     */
    fun recordEvent(event: SecurityEvent)

    /**
     * Get recent events (in-memory for Phase 2A).
     *
     * @return List of recent events
     */
    fun getRecentEvents(limit: Int = 100): List<SecurityEvent>
}

/**
 * In-memory audit service (Phase 2A).
 *
 * Suitable for testing and Phase 2A development.
 * Phase 2B+ may implement persistent storage (database, secure logs).
 */
class InMemoryAuditService : AuditService {
    private val events = mutableListOf<SecurityEvent>()
    private val maxEvents = 1000

    override fun recordEvent(event: SecurityEvent) {
        try {
            event.validate()
        } catch (e: SecurityException) {
            throw e
        }

        events.add(event)

        // Keep only recent events to prevent unbounded growth
        if (events.size > maxEvents) {
            events.removeAt(0)
        }
    }

    override fun getRecentEvents(limit: Int): List<SecurityEvent> {
        return events.takeLast(limit)
    }
}

/**
 * No-op audit service for testing.
 *
 * Discards all events. Useful for tests that don't care about audit trail.
 */
class NoOpAuditService : AuditService {
    override fun recordEvent(event: SecurityEvent) {
        // Discard
    }

    override fun getRecentEvents(limit: Int): List<SecurityEvent> {
        return emptyList()
    }
}
