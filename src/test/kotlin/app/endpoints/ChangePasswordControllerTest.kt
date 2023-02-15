package app.endpoints

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.ints.shouldNotBeInRange
import io.micronaut.http.HttpRequest
import io.micronaut.http.client.annotation.Client
import io.micronaut.rxjava3.http.client.Rx3HttpClient
import io.micronaut.test.extensions.kotest.annotation.MicronautTest
import java.net.URI


@MicronautTest
class ChangePasswordControllerTest(
    @Client("/") private val rx3HttpClient: Rx3HttpClient,
) : StringSpec({
    "change-password endpoint test" {
        val uri = URI.create("/change-password")
        val request = HttpRequest.POST(uri, linkedMapOf<String, String>())
        // TODO
        val response = rx3HttpClient.exchange(request).blockingFirst()
        response.code() shouldNotBeInRange IntRange(200, 299)
    }
})
