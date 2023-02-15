package app.endpoints

import io.kotest.core.spec.style.StringSpec
import io.micronaut.http.client.annotation.Client
import io.micronaut.rxjava3.http.client.Rx3HttpClient
import io.micronaut.test.extensions.kotest.annotation.MicronautTest

@MicronautTest
class RegisterController(
    @Client("/register") private val rx3HttpRegisterClient: Rx3HttpClient,
    @Client("/activate-account") private val rx3HttpActivateAccountClient: Rx3HttpClient,
) : StringSpec({

    "" {
        // TODO
    }
})
