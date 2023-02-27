package app.security.validation

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class EmailValidatorTest: StringSpec({
    val correctEmails = listOf(
        "test@example.com",
        "ilovecats@gmail.com",
        "somebody@gmail.com",
        "contact-admin-hello-webmaster-info-services-peter-crazy-but-oh-so-ubber-cool-english-alphabet-loverer-abcdefghijklmnopqrstuvwxyz@please-try-to.send-me-an-email-if-you-can-possibly-begin-to-remember-this-coz.this-is-the-longest-email-address-known-to-man-but-to-be-honest.this-is-such-a-stupidly-long-sub-domain-it-could-go-on-forever.pacraig.com"
    )

    val incorrectEmails = listOf(
        "test@example",
        "iloveCats@gmail..com",
        "test@example@gmail.com",
        "xyz.@test@example@gmail.com",
        "xyz.@test@example@gmail.",
        "xyz.@test@.",
        "@gmail.com",
        "example.com"
    )

    "correct emails test" {
        val validatedEmailsResults = correctEmails.map { EmailValidator.validate(it) }
        validatedEmailsResults.forEach { it shouldBe true }
    }

    "incorrect emails test" {
        val validatedEmailsResults = incorrectEmails.map { EmailValidator.validate(it) }
        validatedEmailsResults.forEach { it shouldBe false }
    }
})
