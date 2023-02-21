package app.endpoints

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.ints.shouldNotBeInRange
import io.kotest.matchers.shouldBe
import io.micronaut.core.type.Argument
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.annotation.Client
import io.micronaut.http.client.exceptions.HttpClientResponseException
import io.micronaut.rxjava3.http.client.Rx3HttpClient
import io.micronaut.test.extensions.kotest.annotation.MicronautTest
import java.net.URI

@MicronautTest
class ExtractJwtAttributesControllerTest(
    @Client("/")
    private val rx3HttpClient: Rx3HttpClient,
) : StringSpec({
    val extractAttributesUri = URI.create("/extract-attributes")

    "extract-attributes endpoint should throw UNAUTHORIZED exception" {
        shouldThrow<HttpClientResponseException> {
            val request = HttpRequest.POST(extractAttributesUri, mapOf<String, String>())
            rx3HttpClient.exchange(request).blockingFirst()
        }
    }

    "extract-attributes endpoint response status should be OK" +
        "and body contains all (ExtractJwtAttributesController.attributesToExtract ) keys and 'id' key" {
        val request = HttpRequest.GET<Map<String, String>>(extractAttributesUri)

        // TODO auth
        val response = rx3HttpClient.exchange(request).blockingFirst()
        response.status shouldBe HttpStatus.OK

        val responseBody = response.getBody(Argument.of(Map::class.java))
        responseBody.isPresent shouldBe true

        val responseMap = responseBody.get()
        ExtractJwtAttributesController.attributesToExtract.all { responseMap.containsKey(it) } shouldBe true
        responseMap.containsKey("id") shouldBe true
    }
})
