package app.security.generator.activation

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain

class ActivationAccountCodeGeneratorTest: StringSpec({

    val generatedCode = ActivationAccountCodeGenerator.generateCode()

    "generated activation account code length should be 5" {
        generatedCode.length shouldBe 5
    }

    "generated code should contain letters" {
        generatedCode shouldContain Regex("([a-z]|[A-Z])")
    }
})
