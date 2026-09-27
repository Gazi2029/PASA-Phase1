package com.ashna.pasa.data.security

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.ashna.pasa.domain.security.AuthorizationState
import com.ashna.pasa.domain.security.CommandPermission
import java.io.IOException

/**
 * Authorization state repository.
 *
 * Persists and retrieves authorization state securely.
 * Uses Android Keystore-backed encrypted storage.
 *
 * Phase 2A: Authorization state persistence only.
 */
interface AuthorizationRepository {
    /**
     * Get the current authorization state.
     *
     * @return Current authorization state, or unauthorized if not found
     */
    fun getAuthorizationState(): AuthorizationState

    /**
     * Set the authorization state.
     *
     * Validates internal state consistency before persisting.
     *
     * @param state New authorization state
     * @throws IllegalArgumentException if state is invalid
     */
    fun setAuthorizationState(state: AuthorizationState)

    /**
     * Clear authorization state (for revocation).
     */
    fun clearAuthorizationState()
}

/**
 * Encrypted shared preferences implementation of AuthorizationRepository.
 */
class EncryptedAuthorizationRepository(context: Context) : AuthorizationRepository {

    private val masterKey = MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    private val sharedPreferences = try {
        EncryptedSharedPreferences.create(
            context,
            "pasa_security_prefs",
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    } catch (e: IOException) {
        throw IllegalStateException("Failed to initialize encrypted preferences", e)
    }

    override fun getAuthorizationState(): AuthorizationState {
        val isAuthorized = sharedPreferences.getBoolean(
            KEY_IS_AUTHORIZED,
            false
        )

        return if (isAuthorized) {
            val principalId = sharedPreferences.getString(
                KEY_PRINCIPAL_ID,
                null
            )
            val permissionsString = sharedPreferences.getString(
                KEY_PERMISSIONS,
                ""
            ) ?: ""

            val permissions = if (permissionsString.isEmpty()) {
                emptySet()
            } else {
                permissionsString.split(",").mapNotNull {
                    try {
                        CommandPermission.valueOf(it.trim())
                    } catch (e: IllegalArgumentException) {
                        null
                    }
                }.toSet()
            }

            val updatedAt = sharedPreferences.getLong(
                KEY_UPDATED_AT,
                System.currentTimeMillis()
            )

            AuthorizationState(
                isAuthorized = true,
                authorizedPrincipalId = principalId,
                grantedPermissions = permissions,
                updatedAt = updatedAt
            )
        } else {
            AuthorizationState.unauthorized()
        }
    }

    override fun setAuthorizationState(state: AuthorizationState) {
        // Validate state consistency
        state.validate()

        sharedPreferences.edit().apply {
            putBoolean(KEY_IS_AUTHORIZED, state.isAuthorized)
            putString(KEY_PRINCIPAL_ID, state.authorizedPrincipalId)
            putString(
                KEY_PERMISSIONS,
                state.grantedPermissions.joinToString(",") { it.name }
            )
            putLong(KEY_UPDATED_AT, state.updatedAt)
        }.apply()
    }

    override fun clearAuthorizationState() {
        sharedPreferences.edit().apply {
            remove(KEY_IS_AUTHORIZED)
            remove(KEY_PRINCIPAL_ID)
            remove(KEY_PERMISSIONS)
            remove(KEY_UPDATED_AT)
        }.apply()
    }

    companion object {
        private const val KEY_IS_AUTHORIZED = "is_authorized"
        private const val KEY_PRINCIPAL_ID = "principal_id"
        private const val KEY_PERMISSIONS = "permissions"
        private const val KEY_UPDATED_AT = "updated_at"
    }
}
