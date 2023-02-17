package app.endpoints

import app.utils.SecurityUtils
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.security.annotation.Secured
import io.micronaut.security.authentication.Authentication
import io.micronaut.security.rules.SecurityRule

@Secured(SecurityRule.IS_AUTHENTICATED)
@Controller("/user-attributes")
class JwtAttributesController {
    @Get
    fun readAttributes(authentication: Authentication): Map<String, Any> {
        val attributes = authentication.attributes
        val outputData = mutableMapOf<String, Any>()

        val clientId = SecurityUtils.getClientId(authentication)
        if (clientId.isPresent)
            outputData["id"] = clientId.get()

        val attributesToExtract = listOf("email", "name", "lastname")
        val extracted = extractAttributes(attributes, attributesToExtract)
        outputData.putAll(extracted)

        return outputData
    }

    private fun extractAttributes(allAttributes: Map<String, Any>, attributesToExtract: List<String>): Map<String, Any> =
        allAttributes.filter { attributesToExtract.contains(it.key) }
}
