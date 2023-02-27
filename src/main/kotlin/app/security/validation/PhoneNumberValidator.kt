package app.security.validation

object PhoneNumberValidator {
    private val regex = Regex("^(?:\\d{3}){2}\\d{3,4}")

    fun validate(phoneNumber: String): Boolean = phoneNumber.matches(regex)
}
