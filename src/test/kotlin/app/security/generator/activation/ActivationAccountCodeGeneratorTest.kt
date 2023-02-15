package app.security.generator.activation

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class ActivationAccountCodeGeneratorTest: StringSpec({
    "activation account code generator test" {
        val code = ActivationAccountCodeGenerator.generateCode()
        code.length shouldBe 5
    }
})
