package app.factory.rpc

import app.PopMailerServiceGrpc
import app.SmtpMailerServiceGrpc
import io.grpc.ManagedChannel
import io.grpc.ManagedChannelBuilder
import io.micronaut.context.annotation.Factory
import io.micronaut.context.annotation.Property
import jakarta.inject.Named
import jakarta.inject.Singleton

@Factory
class GrpcClientsFactory(
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
    @Named("mailer")
    fun getMailerServiceManagedChannel(): ManagedChannel {
        var builder = ManagedChannelBuilder.forTarget(mailerAddress)

        if (mailerUsePlaintext.toBooleanStrictOrNull() == true)
            builder = builder.usePlaintext()

        val mailerMaxRetryAttempts = mailerMaxRetryAttempts.toIntOrNull()
            ?: clientMaxRetryAttemptsAsInt
        if (mailerMaxRetryAttempts != null)
            builder = builder.maxRetryAttempts(mailerMaxRetryAttempts)

        return builder.build()
    }

    @Singleton
    fun smtpMailerServiceStub(@Named("mailer") channel: ManagedChannel): SmtpMailerServiceGrpc.SmtpMailerServiceStub =
        SmtpMailerServiceGrpc.newStub(channel)

    @Singleton
    fun popMailerServiceStub(@Named("mailer") channel: ManagedChannel): PopMailerServiceGrpc.PopMailerServiceStub =
        PopMailerServiceGrpc.newStub(channel)
}
