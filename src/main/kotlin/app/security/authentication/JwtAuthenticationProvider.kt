package app.security.authentication

import app.service.analytics.MetricsService
import app.model.being.user.User
import app.security.encoder.PasswordEncoder
import app.service.being.user.UserService
import io.micronaut.http.HttpRequest
import io.micronaut.security.authentication.*
import jakarta.inject.Singleton
import org.reactivestreams.Publisher
import org.slf4j.LoggerFactory

import io.micronaut.security.authentication.AuthenticationFailureReason.*
import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.core.Single

@Singleton
class JwtAuthenticationProvider(
    private val userService: UserService,
    private val passwordEncoder: PasswordEncoder,
    private val authMetrics: MetricsService
): AuthenticationProvider {
    override fun authenticate(httpRequest: HttpRequest<*>?, authenticationRequest: AuthenticationRequest<*, *>): Publisher<AuthenticationResponse> {
        authMetrics.incrementLoginAttempts()

        val login: String = authenticationRequest.identity.toString() // username
        val password: String = authenticationRequest.secret.toString() // password

        return userService.findByEmailReactive(login)
            .flatMap { user ->
                if (user.password != null && passwordEncoder.matches(password, user.password!!)) Maybe.just(user) else Maybe.empty()
            }
            .map { user ->
                run {
                    authMetrics.incrementLoginSuccessful()
                    logger.info("New successful login user with email $login")
                    return@run AuthenticationResponse.success(login, getAttributes(user))
                }
            }.switchIfEmpty(Single.just(AuthenticationFailed(CREDENTIALS_DO_NOT_MATCH))).toFlowable()
    }

    private fun getAttributes(user: User): Map<String, String> = mapOf(
            "id" to user.id.toString(),
            "name" to (user.name ?: ""),
            "lastname" to (user.lastname ?: "")
        )

    companion object {
        private val logger = LoggerFactory.getLogger(JwtAuthenticationProvider::class.java)
    }
}
