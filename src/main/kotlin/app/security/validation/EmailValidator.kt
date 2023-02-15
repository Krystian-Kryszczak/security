package app.security.validation

object EmailValidator {
    /**
     * Returns true if email is valid,
     * otherwise email is invalid.
     */
    fun validate(email: String): Boolean {
        if (email.isBlank()) return false
        if (! email.matches(Regex("^(.+)@(\\S+)$"))) return false
        return email.hasOnlyOneAtSign()
                && email.hasOnlyOneDotAfterAtSign()
                    && email.endsWithCorrectSuffix() // TODO refactor
    }

    private fun String.hasOnlyOneAtSign() = this.count { it == '@' } == 1
    private fun String.hasOnlyOneDotAfterAtSign() = this.substringAfter('@').count { it == '.' } == 1
    private fun String.endsWithCorrectSuffix() = this.substringAfter('@').matches(Regex("[a-zA-Z]+\\.[a-zA-Z]+"))
}
