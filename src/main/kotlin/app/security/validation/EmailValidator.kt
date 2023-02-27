package app.security.validation

object EmailValidator {
    /**
     * [a-zA-Z0-9-.]{1,128} - first part
     * @{1} - one at separator
     * [a-zA-Z0-9-.]{0,211} - domain
     * [a-zA-Z0-9]{2,13} - anti double ".." dot
     * \\.{1} - one dot separator
     * [a-z]{2,13} - domain extension (for example: en, international)
     */
    private val regex = Regex("^[a-zA-Z0-9-.]{1,128}@{1}[a-zA-Z0-9-.]{0,211}[a-zA-Z0-9]{2,13}\\.{1}[a-z]{2,13}\$")

    fun validate(email: String): Boolean = email.matches(regex)
}
