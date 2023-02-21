package app.model.security.credentials

import io.micronaut.security.authentication.AuthenticationRequest
import java.io.Serializable

interface Credentials: Serializable, AuthenticationRequest<String, String>
