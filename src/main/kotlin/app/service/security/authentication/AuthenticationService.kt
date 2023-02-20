package app.service.security.authentication

import io.micronaut.security.authentication.AuthenticationRequest
import io.micronaut.security.authentication.AuthenticationResponse
import io.reactivex.rxjava3.core.Flowable

interface AuthenticationService {
    fun authenticate(authenticationRequest: AuthenticationRequest<*, *>): Flowable<AuthenticationResponse>
}
