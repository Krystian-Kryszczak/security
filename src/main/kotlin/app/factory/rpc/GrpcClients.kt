package app.factory.rpc

import app.MailerServiceGrpc
import io.grpc.ManagedChannelBuilder
import io.micronaut.context.annotation.Factory
import io.micronaut.context.annotation.Property
import jakarta.inject.Singleton

@Factory
class GrpcClients(
    @Property(name = "grpc.client.max-retry-attempts")
    clientMaxRetryAttempts: String,
    private val clientMaxRetryAttemptsAsInt: Int? = clientMaxRetryAttempts.toIntOrNull(),

    @Property(name = "grpc.channels.mailer.for-address")
    private val mailerAddress: String,
    @Property(name = "grpc.channels.mailer.plaintext")
    private val mailerUsePlaintext: String,
    @Property(name = "grpc.channels.mailer.max-retry-attempts")
    private val mailerMaxRetryAttempts: String,
) {
    @Singleton
    fun mailer(): MailerServiceGrpc.MailerServiceStub {
        var builder = ManagedChannelBuilder.forTarget(mailerAddress)

        if (mailerUsePlaintext.toBooleanStrictOrNull() == true)
            builder = builder.usePlaintext()

        val mailerMaxRetryAttempts = mailerMaxRetryAttempts.toIntOrNull()
            ?: clientMaxRetryAttemptsAsInt
        if (mailerMaxRetryAttempts != null)
            builder = builder.maxRetryAttempts(mailerMaxRetryAttempts)

        return MailerServiceGrpc.newStub(builder.build())
    }
}
