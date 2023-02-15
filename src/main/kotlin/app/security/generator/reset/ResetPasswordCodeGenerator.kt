package app.security.generator.reset

import app.security.generator.CodeGenerator
import java.util.UUID
import kotlin.random.Random

object ResetPasswordCodeGenerator: CodeGenerator<String> {
    override fun generateCode(): String { // TODO refactor
        return UUID.randomUUID().toString()
            .split(Regex("-"))
            .map {
                it[Random.nextInt(it.length-1)]
            }.joinToString("")
    }
}
