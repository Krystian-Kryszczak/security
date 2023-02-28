package app.endpoints

import io.micronaut.http.HttpStatus
import io.micronaut.security.authentication.Authentication
import io.reactivex.rxjava3.core.Single
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.util.UUID

abstract class BaseController {
    private val logger: Logger = LoggerFactory.getLogger(BaseController::class.java)

    protected fun Authentication.extractClientId(): UUID? {
        val stringId = attributes["id"] as String? ?: return null
        return try {
            UUID.fromString(stringId)
        } catch (e: IllegalArgumentException) {
            logger.error(e.message)
            null
        }
    }
    protected inline fun runProvidesClientId(authentication: Authentication, crossinline body: (id: UUID) -> Single<HttpStatus>): Single<HttpStatus> {
        val id = authentication.extractClientId() ?: return Single.just(HttpStatus.CONFLICT)
        return body(id)
    }
    protected fun Single<Boolean>.mapBooleanToStatus(ifTrue: HttpStatus, ifFalse: HttpStatus): Single<HttpStatus> =
        map { if (it) ifTrue else ifFalse }
}
