package app.security.generator.activation

import app.security.generator.CodeGenerator
import org.apache.commons.lang3.RandomStringUtils

object ActivationAccountCodeGenerator: CodeGenerator<String> {
    override fun generateCode(): String = RandomStringUtils.random(5, true, false)
}
