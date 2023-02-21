package app.endpoints

import app.security.SecurityClient
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.annotation.Client
import io.micronaut.http.client.exceptions.HttpClientResponseException
import io.micronaut.rxjava3.http.client.Rx3HttpClient
import io.micronaut.security.authentication.UsernamePasswordCredentials
import io.micronaut.test.extensions.kotest.annotation.MicronautTest
import java.net.URI

@MicronautTest
class ChangePasswordControllerTest(
    @Client("/") private val rx3HttpClient: Rx3HttpClient,
    private val securityClient: SecurityClient
) : StringSpec({
    val username = "sherlock"; val password = "Password#456"
    val credentials = UsernamePasswordCredentials(username, password)
    val newPassword: String = password + "789"
    var code: String = ""  // TODO

    fun <T> postAuthorized(uri: String, body: T): HttpResponse<*> {
        securityClient.login(credentials)
        HttpRequest.POST("/login", body)
        val request = HttpRequest.POST(URI.create(uri), body)
        return rx3HttpClient.exchange(request).blockingFirst()
    }

    fun <T> postUnauthorized(uri: String, body: T): HttpResponse<*> {
        val request = HttpRequest.POST(URI.create(uri), body)
        return rx3HttpClient.exchange(request).blockingFirst()
    }

    "change-password endpoint should throw UNAUTHORIZED exception" {
        shouldThrow<HttpClientResponseException> {
            postUnauthorized("/change-password", linkedMapOf<String, String>())
        }
    }
    "change-password endpoint should return response with http status ACCEPTED" {
        val response = postAuthorized("/change-password", linkedMapOf("actualPassword" to password))
        response.status shouldBe HttpStatus.ACCEPTED
    }
    "change-password endpoint should return response with http status CONFLICT" {
        val response = postAuthorized("/change-password", linkedMapOf("actualPassword" to password))
        response.status shouldBe HttpStatus.CONFLICT
    }

    "reset-password endpoint should return response with http status UNAUTHORIZED" {
        shouldThrow<HttpClientResponseException> {
            postUnauthorized("/reset-password", linkedMapOf<String, String>())
        }
    }
    "reset-password endpoint should return response with http status CONFLICT" {
        val response = postAuthorized("/reset-password", linkedMapOf<String, String>())
        response.status shouldBe HttpStatus.CONFLICT
    }
    "reset-password endpoint should return response with http status ACCEPTED" {
        val response = postAuthorized("/reset-password", linkedMapOf("code" to code, "password" to newPassword))
        response.status shouldBe HttpStatus.ACCEPTED
    }
})
