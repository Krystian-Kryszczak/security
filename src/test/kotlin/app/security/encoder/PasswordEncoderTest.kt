package app.security.encoder

import io.kotest.core.spec.style.StringSpec
import io.micronaut.test.extensions.kotest.annotation.MicronautTest

@MicronautTest
class PasswordEncoderTest(private val passwordEncoder: PasswordEncoder): StringSpec({

    "the matches test" {
        val password = "1234"
        val bcryptHashString = passwordEncoder.encode(password)
        val result = passwordEncoder.matches(password, bcryptHashString)
        if (!result) throw RuntimeException()
    }
})
