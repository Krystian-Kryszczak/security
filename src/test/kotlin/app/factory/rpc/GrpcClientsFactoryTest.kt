package app.factory.rpc

import app.PopMailerServiceGrpc
import app.SmtpMailerServiceGrpc
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldNotBe
import io.micronaut.test.extensions.kotest.annotation.MicronautTest

@MicronautTest
class GrpcClientsFactoryTest(
    private val smtpMailerService: SmtpMailerServiceGrpc.SmtpMailerServiceStub,
    private val popMailerServiceGrpc: PopMailerServiceGrpc.PopMailerServiceStub
): StringSpec({

    "smtpMailerService inject test" {
        smtpMailerService shouldNotBe null
    }

    "popMailerService inject test" {
        popMailerServiceGrpc shouldNotBe null
    }
})
