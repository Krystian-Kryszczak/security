package app.security.validation

object EmailValidator {
    /**
     * Returns true if email is valid,
     * otherwise email is invalid.
     */
    fun validate(email: String): Boolean {
        if (email.isBlank()) return false
        return email.matches(Regex("^(.+)@(\\S+)$"))
    }
}
