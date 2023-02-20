package app.endpoints

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.ints.shouldBeInRange
import io.micronaut.http.HttpRequest
import io.micronaut.http.client.annotation.Client
import io.micronaut.rxjava3.http.client.Rx3HttpClient
import io.micronaut.test.extensions.kotest.annotation.MicronautTest
import java.net.URI

@MicronautTest
class RegisterControllerTest(
    @Client("/register") private val rx3HttpRegisterClient: Rx3HttpClient,
    @Client("/activate-account") private val rx3HttpActivateAccountClient: Rx3HttpClient,
) : StringSpec({

    "" {
        val registerUri = URI.create("/register")
        val activateAccountUri = URI.create("/activate-account")
        val request = HttpRequest.POST(registerUri, linkedMapOf<String, String>())
        // TODO
        val response = rx3HttpRegisterClient.exchange(request).blockingFirst()
        response.code() shouldBeInRange IntRange(200, 299)
    }
})
