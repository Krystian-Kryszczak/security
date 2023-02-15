package app.factory.rpc

import app.MailerServiceGrpc
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldNotBe
import io.micronaut.test.extensions.kotest.annotation.MicronautTest

@MicronautTest
class GrpcClientsTest(
    private val mailerService: MailerServiceGrpc.MailerServiceStub
): StringSpec({

    "mailerService inject test" {
        mailerService shouldNotBe null
    }
})
