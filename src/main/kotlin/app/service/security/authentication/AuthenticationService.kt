package app.service.security.authentication

import io.micronaut.security.authentication.AuthenticationRequest
import io.micronaut.security.authentication.AuthenticationResponse
import org.reactivestreams.Publisher

interface AuthenticationService {
    fun authenticate(authenticationRequest: AuthenticationRequest<*, *>): Publisher<AuthenticationResponse>
}
