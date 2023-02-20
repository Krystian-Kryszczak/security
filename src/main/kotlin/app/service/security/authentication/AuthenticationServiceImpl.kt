package app.service.security.authentication

import app.model.being.user.User
import app.security.encoder.PasswordEncoder
import app.service.being.user.UserService
import app.storage.cassandra.dao.security.credentials.UserCredentialsDao
import io.micronaut.security.authentication.AuthenticationFailed
import io.micronaut.security.authentication.AuthenticationFailureReason
import io.micronaut.security.authentication.AuthenticationRequest
import io.micronaut.security.authentication.AuthenticationResponse
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.core.Single
import jakarta.inject.Singleton

@Singleton
class AuthenticationServiceImpl(
    private val userCredentialsDao: UserCredentialsDao,
    private val userService: UserService,
    private val passwordEncoder: PasswordEncoder,
): AuthenticationService {
    override fun authenticate(authenticationRequest: AuthenticationRequest<*, *>): Flowable<AuthenticationResponse> {
        val login: String = authenticationRequest.identity.toString() // username
        val password: String = authenticationRequest.secret.toString() // password

        return Maybe.fromPublisher(userCredentialsDao.findByEmailReactive(login))
            .flatMap { credentials ->
                if (credentials.hashedPassword != null && passwordEncoder.matches(password, credentials.hashedPassword)) Maybe.just(credentials) else Maybe.empty()
            }.flatMap {
                userService.findByEmailReactive(login).map {
                    user -> AuthenticationResponse.success(login, extractUserAttributes(user))
                }
            }.switchIfEmpty(Single.just(AuthenticationFailed(AuthenticationFailureReason.CREDENTIALS_DO_NOT_MATCH)))
            .toFlowable()
    }

    private fun extractUserAttributes(user: User): Map<String, String> = mapOf(
        "id" to user.id.toString(),
        "name" to (user.name ?: ""),
        "lastname" to (user.lastname ?: "")
    )
}
