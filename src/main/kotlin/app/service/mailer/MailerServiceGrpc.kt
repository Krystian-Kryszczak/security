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
    override fun sendUserActivationCode(address: String, code: String): Single<Boolean> {
        val request: MailerRequest = MailerRequest.newBuilder().setAddress(address).setContent(code).build()
        // ---------------------------------------------------------------------------------------------------- //
        return asObservable<MailerReply> { mailerServiceStub.sendUserActivationCode(request, it) }.firstElement().map {
            logger.info("sendUserActivationCode -> ${it.successful}")
            it.successful
        }.defaultIfEmpty(false).onErrorReturn {
            logger.error(it.message)
            return@onErrorReturn false
        }
    }

//    private fun sendUserActivationCode2(address: String, code: String): Single<Boolean> {
//        val request: MailerRequest = MailerRequest.newBuilder().setAddress(address).setContent(code).build()
//        // ---------------------------------------------------------------------------------------------------- //
//        return asObservable<MailerReply> { mailerServiceStub.sendUserActivationCode(request, it) }
//            .firstElement() .map {
//                logger.info("sendUserActivationCode -> ${it.successful}")
//                it.successful
//            }.defaultIfEmpty(false).onErrorReturn {
//                logger.error(it.message)
//                return@onErrorReturn false
//            }
//    }

    override fun sendUserResetPasswordCode(address: String, code: String): Single<Boolean> {
        val request: MailerRequest = MailerRequest.newBuilder().setAddress(address).setContent(code).build()
        // ---------------------------------------------------------------------------------------------------- //
        return Single.create<MailerReply> {
            val observer = object: StreamObserver<MailerReply> {
                override fun onNext(value: MailerReply) {
                    it.onSuccess(value)
                }
                override fun onError(t: Throwable) {
                    it.onError(t)
                }
                override fun onCompleted() {}
            }
            mailerServiceStub.sendUserResetPasswordCode(request, observer)
        }.map {
            logger.info("sendUserResetPasswordCode -> ${it.successful}")
            it.successful
        }
        .onErrorReturn {
            logger.error(it.message)
            return@onErrorReturn false
        }
    }

    private inline fun <T : Any> asObservable(crossinline body: (StreamObserver<T>) -> Unit): Observable<T> {
        return Observable.create { subscription ->
            val observer = object : StreamObserver<T> {
                override fun onNext(value: T) = subscription.onNext(value)
                override fun onError(error: Throwable) = subscription.onError(error)
                override fun onCompleted() = subscription.onComplete()
            }
            body(observer)
        }
    }

    private inline fun <T : Any> asSingle(crossinline body: (StreamObserver<T>) -> Unit): Single<T> {
        return Single.create { subscription ->
            val observer = object : StreamObserver<T> {
                override fun onNext(value: T) = subscription.onSuccess(value)
                override fun onError(error: Throwable) = subscription.onError(error)
                override fun onCompleted() {}
            }
            body(observer)
        }
    }

    companion object {
        private val logger: Logger = LoggerFactory.getLogger(MailerServiceGrpc::class.java)
    }
}
