package app.service.analytics

import app.storage.kafka.analytics.KafkaAnalyticsClient
import io.micronaut.scheduling.annotation.Scheduled
import jakarta.inject.Singleton
import java.time.Instant

@Singleton
class MetricServiceImpl(private val kafkaAnalyticsClient: KafkaAnalyticsClient): MetricsService  {
    private var loginAttempts = 0
    private var loginSuccessful = 0
    private var generatedActivationCodes = 0
    private var activatedAccounts = 0
    private var activationAccountFails = 0

    override fun incrementLoginAttempts() {
        loginAttempts++
    }
    override fun incrementLoginSuccessful() {
        loginSuccessful++
    }
    override fun incrementGeneratedActivationCodes() {
        generatedActivationCodes++
    }
    override fun incrementActivatedAccounts() {
        activatedAccounts++
    }
    override fun incrementActivationAccountFails() {
        activationAccountFails++
    }

    @Scheduled(fixedRate = "5m") // Executing every <value> (for example: 5m - 5 minutes)
    internal fun updateAllAndResetTask() {
        val now = Instant.now().toString()
        kafkaAnalyticsClient.sendLoginAttempts(now, loginAttempts.toString())
        loginAttempts = 0
        kafkaAnalyticsClient.sendLoginSuccessful(now, loginSuccessful.toString())
        loginSuccessful = 0
        kafkaAnalyticsClient.sendGeneratedActivationCodes(now, generatedActivationCodes.toString())
        generatedActivationCodes = 0
        kafkaAnalyticsClient.sendActivatedAccounts(now, activatedAccounts.toString())
        activatedAccounts = 0
    }
}
