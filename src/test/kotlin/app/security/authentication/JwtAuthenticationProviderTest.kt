package app.security.authentication

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.micronaut.security.authentication.UsernamePasswordCredentials
import io.micronaut.test.extensions.kotest.annotation.MicronautTest
import io.reactivex.rxjava3.core.Flowable

@MicronautTest
class JwtAuthenticationProviderTest(private val provider: JwtAuthenticationProvider): StringSpec({
    "jwt authentication provider test" {
        val username = "" // TODO enter exist user name and password
        val password = ""

        val credentials = UsernamePasswordCredentials(username, password)
        val authenticateResult = provider.authenticate(null, credentials)
        val authenticateResponse = Flowable.fromPublisher(authenticateResult).blockingFirst()
        authenticateResponse.isAuthenticated shouldBe true
    }
})
