package app.security.validation

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class EmailValidatorTest: StringSpec({
    val correctEmails = listOf(
        "test@example.com",
        "ilovecats@gmail.com",
        "somebody@gmail.com"
    )

    val incorrectEmails = listOf(
        "test@example",
        "test@example@gmail.com",
        "xyz.@test@example@gmail.com",
        "xyz.@test@example@gmail.",
        "xyz.@test@.",
        "@gmail.com",
        "example.com"
    )

    "correct emails test" {
        val validatedEmailsResults = correctEmails.map { EmailValidator.validate(it) }
        validatedEmailsResults.forEach { isEmailValid -> isEmailValid shouldBe true }
    }

    "incorrect emails test" {
        val validatedEmailsResults = incorrectEmails.map { EmailValidator.validate(it) }
        validatedEmailsResults.forEach { isEmailValid -> isEmailValid shouldBe false }
    }
})
