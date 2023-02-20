package app.security.authentication

import com.nimbusds.jwt.JWTParser
import com.nimbusds.jwt.SignedJWT
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.matchers.types.shouldBeInstanceOf
import io.micronaut.context.annotation.Property
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.http.MediaType.TEXT_PLAIN
import io.micronaut.http.client.annotation.Client
import io.micronaut.http.client.exceptions.HttpClientResponseException
import io.micronaut.rxjava3.http.client.Rx3HttpClient
import io.micronaut.security.authentication.UsernamePasswordCredentials
import io.micronaut.security.token.jwt.render.BearerAccessRefreshToken
import io.micronaut.test.extensions.kotest.annotation.MicronautTest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@MicronautTest
class JwtAuthenticationProviderTest(
    @Client("/") private val client: Rx3HttpClient,
    @Property(name = "testing.username") private val usernameForTest: String,
    @Property(name = "testing.password") private val passwordForTest: String,
): StringSpec({

    "accessing a secured url without authenticating returns unauthorized" {
        val exception = shouldThrow<HttpClientResponseException> {
            client.toBlocking().exchange<Any, Any>(HttpRequest.GET<Any>("/").accept(TEXT_PLAIN))
        }
        exception.status shouldBe HttpStatus.UNAUTHORIZED
    }

    "upon successful authentication a JsonWebToken is issued to the user" {
        val credentials = UsernamePasswordCredentials(usernameForTest, passwordForTest)
        val request: HttpRequest<*> = HttpRequest.POST("/login", credentials)
        val rsp: HttpResponse<BearerAccessRefreshToken> = withContext(Dispatchers.IO) {
            client.toBlocking().exchange(request, BearerAccessRefreshToken::class.java)
        }

        rsp.status.code shouldBe 200

        val bearerAccessRefreshToken: BearerAccessRefreshToken? = rsp.body()
        usernameForTest shouldBe bearerAccessRefreshToken?.username
        bearerAccessRefreshToken?.accessToken shouldNotBe null
        JWTParser.parse(bearerAccessRefreshToken?.accessToken).shouldBeInstanceOf<SignedJWT>()

        val accessToken: String? = bearerAccessRefreshToken?.accessToken
        val requestWithAuthorization = HttpRequest.GET<Any>("/")
            .accept(TEXT_PLAIN)
            .bearerAuth(accessToken)
        val response: HttpResponse<String> = withContext(Dispatchers.IO) {
            client.toBlocking().exchange(requestWithAuthorization, String::class.java)
        }

        rsp.status.code shouldBe 200
        response.body() shouldBe usernameForTest
    }
})
