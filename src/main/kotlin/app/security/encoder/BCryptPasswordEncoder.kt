package app.security.encoder;

import at.favre.lib.crypto.bcrypt.BCrypt
import jakarta.inject.Singleton

@Singleton
class BCryptPasswordEncoder: PasswordEncoder {
    override fun encode(rawPassword: String): String {
        return BCrypt.withDefaults().hashToString(12, rawPassword.toCharArray())
    }
    override fun matches(rawPassword: String, encodedPassword: String): Boolean {
        val result: BCrypt.Result = BCrypt.verifyer().verify(rawPassword.toCharArray(), encodedPassword)
        return result.verified
    }
}
