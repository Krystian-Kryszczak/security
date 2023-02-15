package app.service.analytics

interface MetricsService {
    fun incrementLoginAttempts()
    fun incrementLoginSuccessful()
    fun incrementGeneratedActivationCodes()
    fun incrementActivatedAccounts()
    fun incrementActivationAccountFails()
}
