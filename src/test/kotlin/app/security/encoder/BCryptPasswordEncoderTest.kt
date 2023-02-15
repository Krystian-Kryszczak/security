package app.security.encoder

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.micronaut.test.extensions.kotest.annotation.MicronautTest
import jakarta.inject.Named

@MicronautTest
class BCryptPasswordEncoderTest(
    @Named("bcrypt")
    private val passwordEncoder: PasswordEncoder
): StringSpec({

    "the encoded password by BCrypt matches to plain password test" {
        val plainPassword = "somePassword!785"
        val bcryptEncodedPassword = passwordEncoder.encode(plainPassword)
        val passwordMatches = passwordEncoder.matches(plainPassword, bcryptEncodedPassword)

        passwordMatches shouldBe true
    }
})
