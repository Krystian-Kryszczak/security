package app.endpoints

import app.model.being.user.UserModel
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.annotation.Client
import io.micronaut.http.client.exceptions.HttpClientResponseException
import io.micronaut.rxjava3.http.client.Rx3HttpClient
import io.micronaut.test.extensions.kotest.annotation.MicronautTest
import java.net.URI

@MicronautTest
class RegisterControllerTest(
    @Client("/register")
    private val rx3HttpRegisterClient: Rx3HttpClient,
) : StringSpec({
    val userModel = UserModel(
        "firstname",
        "lastname",
        "socialmedia.app.mailer@gmail.com",
        "",
        "Password#456",
        ((2000*365)+31+5)-(1970*365), // TODO Refactor
        1
    )

    "/register endpoint response should throw exception HttpClientResponseException: BAD_REQUEST" {
        shouldThrow<HttpClientResponseException> {
            val request = HttpRequest.POST("/", "")
            rx3HttpRegisterClient.exchange(request).blockingFirst()
        }
    }

    "/register endpoint response status should be ACCEPTED" {
        val request = HttpRequest.POST("/", userModel)
        val response = rx3HttpRegisterClient.exchange(request).blockingFirst()
        response.status shouldBe HttpStatus.ACCEPTED
    }
})
