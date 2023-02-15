package app.security.generator.reset

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class ResetPasswordCodeGeneratorTest: StringSpec({
    "reset password code generator test" {
        val code = ResetPasswordCodeGenerator.generateCode()
        code.length shouldBe 5
    }
})
