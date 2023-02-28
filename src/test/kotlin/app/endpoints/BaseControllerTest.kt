package app.endpoints

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.micronaut.security.authentication.Authentication
import java.util.UUID

class BaseControllerTest: StringSpec({
    val controller = object : BaseController() {
        fun extractClientId(authentication: Authentication) = authentication.extractClientId()
    }
    val uuid = UUID.randomUUID()

    "extract client id will return non null UUID object" {
        val authentication = Authentication.build("sherlock", mapOf("id" to uuid.toString()))
        controller.extractClientId(authentication) shouldNotBe null
    }

    "extract client id will return null" {
        val authentication = Authentication.build("sherlock")
        controller.extractClientId(authentication) shouldBe null
    }
})
