package app.security.validation

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class PasswordValidatorTest: StringSpec({
    val correctPasswords = listOf(
        "some_Password#456",
        "correct#!24Password",
        "Password!0"
    )

    val incorrectPasswords = listOf(
        "Aa@!1",
        "Aa@!123456789123456789",
        "password",
        "Password987",
        "12345678",
        "PASSWORD",
        "PASSWORD@#!",
    )

    "correct passwords test" {
        val validatedPasswordsResults = correctPasswords.map { PasswordValidator.validate(it) }
        validatedPasswordsResults.forEach { isValid -> isValid shouldBe true }
    }

    "incorrect passwords test" {
        val validatedPasswordsResults = incorrectPasswords.map { PasswordValidator.validate(it) }
        validatedPasswordsResults.forEach { isValid -> isValid shouldBe false }
    }
})
