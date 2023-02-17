package app.utils

import io.micronaut.security.authentication.Authentication
import java.util.Optional
import java.util.UUID

object SecurityUtils {
    fun getClientId(authentication: Authentication): Optional<UUID> {
        val stringId = authentication.attributes["id"] as String? ?: return Optional.empty()
        try {
            val uuid = UUID.fromString(stringId)
            return Optional.of(uuid)
        } catch (_: IllegalArgumentException) {}
        return Optional.empty()
    }
}
