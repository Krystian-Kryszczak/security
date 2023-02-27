package app.factory.rpc

import app.SmtpMailerServiceGrpc
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldNotBe
import io.micronaut.test.extensions.kotest.annotation.MicronautTest

@MicronautTest
class GrpcClientsTest(
    private val mailerService: SmtpMailerServiceGrpc.SmtpMailerServiceStub
): StringSpec({

    "mailerService inject test" {
        mailerService shouldNotBe null
    }
})
