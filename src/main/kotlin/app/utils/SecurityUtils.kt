package app.utils

import io.micronaut.security.authentication.Authentication
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.util.UUID

class SecurityUtils {
    companion object {
        private val logger: Logger = LoggerFactory.getLogger(SecurityUtils::class.java)

        fun extractClientId(authentication: Authentication): UUID? {
            val stringId = authentication.attributes["id"] as String? ?: return null
            return try {
                UUID.fromString(stringId)
            } catch (e: IllegalArgumentException) {
                logger.error(e.message)
                null
            }
        }
    }
}
