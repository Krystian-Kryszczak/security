package app.service.mailer

import app.MailerReply
import app.MailerRequest
import app.MailerServiceGrpc.MailerServiceStub
import io.grpc.stub.StreamObserver
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import jakarta.inject.Singleton
import org.slf4j.Logger
import org.slf4j.LoggerFactory

@Singleton
class MailerServiceGrpc(private val mailerServiceStub: MailerServiceStub): MailerService {
    private fun requestWithAddressAndContent(receiverAddress: String, content: String): MailerRequest =
        MailerRequest.newBuilder().setAddress(receiverAddress).setContent(content).build()

    override fun sendUserActivationCode(receiverAddress: String, activationCode: String): Single<Boolean> {
        val request = requestWithAddressAndContent(receiverAddress, activationCode)
        return asObservable<MailerReply> {
            streamObserver -> mailerServiceStub.sendUserActivationCode(request,     streamObserver)
        }.doAfterNext {
            logger.info("sendUserActivationCode result -> ${it.successful}")
        }.transformToBooleanWithCatchingErrors()
    }

    override fun sendUserResetPasswordCode(receiverAddress: String, resetPasswordCode: String): Single<Boolean> {
        val request = requestWithAddressAndContent(receiverAddress, resetPasswordCode)
        return asObservable<MailerReply> {
            streamObserver -> mailerServiceStub.sendUserResetPasswordCode(request, streamObserver)
        }.doAfterNext {
            logger.info("sendUserResetPasswordCode result -> ${it.successful}")
        }.transformToBooleanWithCatchingErrors()
    }

    private inline fun <T : Any> asObservable(crossinline body: (StreamObserver<T>) -> Unit): Observable<T> =
        Observable.create { subscription ->
            val observer = object : StreamObserver<T> {
                override fun onNext(value: T) = subscription.onNext(value)
                override fun onError(error: Throwable) = subscription.onError(error)
                override fun onCompleted() = subscription.onComplete()
            }
            body(observer)
        }

    private fun Observable<MailerReply>.transformToBooleanWithCatchingErrors() =
        firstElement()
            .map {
                it.successful
            }
            .defaultIfEmpty(false)
            .onErrorReturn {
                logger.error(it.message)
                return@onErrorReturn false
            }

    companion object {
        private val logger: Logger = LoggerFactory.getLogger(MailerServiceGrpc::class.java)
    }
}
