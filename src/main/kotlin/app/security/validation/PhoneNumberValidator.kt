package app.security.validation

object PhoneNumberValidator {
    /**
     * Returns true if phone number is valid,
     * otherwise phone number is invalid.
     */
    fun validate(phoneNumber: String): Boolean { // TODO change regex to better
        if (phoneNumber.isBlank()) return false
        return phoneNumber.matches(Regex("^(?:\\d{3}){2}\\d{3,4}"))
    }
}
