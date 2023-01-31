package app.security.validation

object PasswordValidator {
    /**
     * Returns true if password is valid,
     * otherwise password is too weak.
     */
    fun validate(password: String?): Boolean {
        if (password.isNullOrBlank()) return false
        return password.matches(Regex("^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[!@#\$&*~]).{8,20}\$"))
    }
}
