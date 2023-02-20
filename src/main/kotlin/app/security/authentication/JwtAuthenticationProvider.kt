package app.security.authentication

import app.service.analytics.MetricsService
import app.service.security.authentication.AuthenticationService
import io.micronaut.http.HttpRequest
import io.micronaut.security.authentication.*
import jakarta.inject.Singleton
import org.reactivestreams.Publisher
import org.slf4j.LoggerFactory

@Singleton
class JwtAuthenticationProvider(
    private val authenticationService: AuthenticationService,
    private val metricsService: MetricsService
): AuthenticationProvider {
    override fun authenticate(httpRequest: HttpRequest<*>?, authenticationRequest: AuthenticationRequest<*, *>): Publisher<AuthenticationResponse> =
        authenticationService.authenticate(authenticationRequest)
            .doOnNext {
                metricsService.incrementLoginAttempts()
                if (it.isAuthenticated) {
                    metricsService.incrementLoginSuccessful()
                    logger.info("User with email address ${authenticationRequest.identity} login successfully logged in.")
                }
            }

    companion object {
        private val logger = LoggerFactory.getLogger(JwtAuthenticationProvider::class.java)
    }
}
