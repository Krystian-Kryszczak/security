package app.endpoints

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.ints.shouldNotBeInRange
import io.kotest.matchers.shouldBe
import io.micronaut.core.type.Argument
import io.micronaut.http.HttpRequest
import io.micronaut.http.client.annotation.Client
import io.micronaut.rxjava3.http.client.Rx3HttpClient
import io.micronaut.test.extensions.kotest.annotation.MicronautTest
import java.net.URI

@MicronautTest
class ExtractJwtAttributesControllerTest(
    @Client private val rx3HttpClient: Rx3HttpClient,
) : StringSpec({
    "attributes extraction test" {
        val extractAttributesUri = URI.create("/extract-attributes")
        val request = HttpRequest.GET<Map<String, String>>(extractAttributesUri)

        // TODO auth
        val response = rx3HttpClient.exchange(request).blockingFirst()
        val respBody = response.getBody(Argument.of(Map::class.java))
        respBody.isPresent shouldBe true

        val map = respBody.get()
        ExtractJwtAttributesController.attributesToExtract
            .all { map.containsKey(it) } shouldBe true

        response.code() shouldNotBeInRange IntRange(200, 299)
    }
})
