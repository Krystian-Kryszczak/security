package app.security.generator.reset

import app.security.generator.CodeGenerator
import org.apache.commons.lang3.RandomStringUtils

object ResetPasswordCodeGenerator: CodeGenerator<String> {
    override fun generateCode(): String = RandomStringUtils.random(8, true, true)
}
