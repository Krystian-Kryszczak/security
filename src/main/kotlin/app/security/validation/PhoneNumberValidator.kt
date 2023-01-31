package app.security.validation

object PhoneNumberValidator {
    /**
     * Returns true if phone number is valid,
     * otherwise phone number is invalid.
     */
    fun validate(phoneNumber: String): Boolean {
        if (phoneNumber.isBlank()) return false
        return phoneNumber.matches(Regex("^(\\\\+\\\\d{1,3}( )?)?((\\\\(\\\\d{3}\\\\))|\\\\d{3})[- .]?\\\\d{3}[- .]?\\\\d{4}\$"))
    }
}
