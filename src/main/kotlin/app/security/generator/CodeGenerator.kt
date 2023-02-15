package app.security.generator

interface CodeGenerator<T: Any> {
    fun generateCode(): T
}
