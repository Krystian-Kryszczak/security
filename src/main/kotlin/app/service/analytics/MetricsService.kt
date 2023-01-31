package app.service.analytics

import app.storage.broker.analytics.AnalyticsClient
import io.micronaut.scheduling.annotation.Scheduled
import jakarta.inject.Singleton
import java.time.Instant

@Singleton
class MetricsService(private val analyticsClient: AnalyticsClient) {
    private var loginAttempts: Int = 0
    private var loginSuccessful: Int = 0
    private var generatedActivationCodes: Int = 0
    private var activatedAccounts: Int = 0
    private var activationAccountFails: Int = 0

    fun incrementLoginAttempts() {
        loginAttempts++
    }
    fun incrementLoginSuccessful() {
        loginSuccessful++
    }
    fun incrementGeneratedActivationCodes() {
        generatedActivationCodes++
    }
    fun incrementActivatedAccounts() {
        activatedAccounts++
    }
    fun incrementActivationAccountFails() {
        activationAccountFails++
    }

    @Scheduled(fixedRate = "5m") // Executing every five minutes
    internal fun task() {
        val now = Instant.now().toString()
        analyticsClient.sendLoginAttempts(now, loginAttempts.toString())
        loginAttempts = 0
        analyticsClient.sendLoginSuccessful(now, loginSuccessful.toString())
        loginSuccessful = 0
        analyticsClient.sendGeneratedActivationCodes(now, generatedActivationCodes.toString())
        generatedActivationCodes = 0
        analyticsClient.sendActivatedAccounts(now, activatedAccounts.toString())
        activatedAccounts = 0
    }
}
