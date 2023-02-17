package app.utils

import io.micronaut.security.authentication.Authentication
import java.util.UUID

object SecurityUtils {
    fun getClientId(authentication: Authentication): UUID? {
        return UUID.fromString(authentication.attributes["id"] as String? ?: return null)
    }
}
