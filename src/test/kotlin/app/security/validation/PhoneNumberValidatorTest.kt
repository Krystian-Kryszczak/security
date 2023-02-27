package app.security.validation

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class PhoneNumberValidatorTest: StringSpec({
    fun List<String>.withoutSpaces(): List<String> = map { it.replace(" ", "") }

    val correctPhoneNumbers = listOf(
        "500 500 500",
        "123 456 123",
        "505 444 100"
    )
    .withoutSpaces()

    val incorrectPhoneNumbers = listOf(
        "112",
        "111 111",
        "909 909 909 909",
        "505 a6b 45%",
        "w2x 505 475 111",
        "505.505.505",
        "00 909 505 505",
    )
    .withoutSpaces()

    "correct phone numbers test" {
        val validatedPhoneNumbersResults = correctPhoneNumbers.map { PhoneNumberValidator.validate(it) }
        validatedPhoneNumbersResults.forEach { it shouldBe true }
    }

    "incorrect phone numbers test" {
        val validatedPhoneNumbersResults = incorrectPhoneNumbers.map { PhoneNumberValidator.validate(it) }
        validatedPhoneNumbersResults.forEach { it shouldBe false }
    }
})
