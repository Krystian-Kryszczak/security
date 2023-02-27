package app.security.generator.reset

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain

class ResetPasswordCodeGeneratorTest: StringSpec({

    val generatedCode = ResetPasswordCodeGenerator.generateCode()

    "generated code length should be 8" {
        generatedCode.length shouldBe 8
    }

    "generated code should contain letters" {
        generatedCode shouldContain Regex("([a-z]|[A-Z])")
    }
})
