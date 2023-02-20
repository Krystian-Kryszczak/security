package app.endpoints

import app.utils.SecurityUtils
import io.micronaut.http.HttpStatus
import io.micronaut.security.authentication.Authentication
import io.reactivex.rxjava3.core.Single
import java.util.UUID

abstract class BaseController {
    protected inline fun runProvidesClientId(authentication: Authentication, crossinline body: (id: UUID) -> Single<HttpStatus>): Single<HttpStatus> {
        val id = SecurityUtils.extractClientId(authentication) ?: return Single.just(HttpStatus.CONFLICT)
        return body(id)
    }
    protected fun Single<Boolean>.mapBooleanToStatus(ifTrue: HttpStatus, ifFalse: HttpStatus): Single<HttpStatus> =
        map { if (it) ifTrue else ifFalse }
}
