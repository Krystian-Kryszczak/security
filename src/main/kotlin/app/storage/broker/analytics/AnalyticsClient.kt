package app.storage.broker.analytics

import io.micronaut.configuration.kafka.annotation.KafkaClient
import io.micronaut.configuration.kafka.annotation.KafkaKey
import io.micronaut.configuration.kafka.annotation.Topic

@KafkaClient
interface AnalyticsClient {
    @Topic("login_attempts")
    fun sendLoginAttempts(@KafkaKey datetime: String, value: String)
    @Topic("login_successful")
    fun sendLoginSuccessful(@KafkaKey datetime: String, value: String)
    @Topic("generated_activation_codes")
    fun sendGeneratedActivationCodes(@KafkaKey datetime: String, value: String)
    @Topic("activated_accounts")
    fun sendActivatedAccounts(@KafkaKey datetime: String, value: String)
}
