package app.endpoints

import app.utils.SecurityUtils
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.security.annotation.Secured
import io.micronaut.security.authentication.Authentication
import io.micronaut.security.rules.SecurityRule

@Secured(SecurityRule.IS_AUTHENTICATED)
@Controller("/extract-attributes")
class ExtractJwtAttributesController {
    @Get
    fun readAttributes(authentication: Authentication): Map<String, Any> {
        val attributes = authentication.attributes
        val outputData = mutableMapOf<String, Any>()

        val clientId = SecurityUtils.extractClientId(authentication)
        if (clientId != null)
            outputData["id"] = clientId

        val extracted = extractAttributes(attributes, attributesToExtract)
        outputData.putAll(extracted)

        return outputData
    }

    private fun extractAttributes(allAttributes: Map<String, Any>, attributesToExtract: List<String>): Map<String, Any> =
        allAttributes.filter {
            singleFromAllAttributes -> attributesToExtract.contains(singleFromAllAttributes.key)
        }

    companion object {
        val attributesToExtract = listOf("email", "name", "lastname")
    }
}
