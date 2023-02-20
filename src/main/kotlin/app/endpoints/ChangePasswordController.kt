package app.endpoints

import app.service.security.change.password.UserChangePasswordService
import io.micronaut.http.HttpStatus
import io.micronaut.http.MediaType
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Post
import io.micronaut.security.annotation.Secured
import io.micronaut.security.authentication.Authentication
import io.micronaut.security.rules.SecurityRule
import io.reactivex.rxjava3.core.Single

@Secured(SecurityRule.IS_AUTHENTICATED)
@Controller
class ChangePasswordController(private val changePasswordService: UserChangePasswordService): BaseController() {
    @Post("/change-password", consumes = [MediaType.APPLICATION_FORM_URLENCODED])
    fun changePassword(oldPassword: String, authentication: Authentication): Single<HttpStatus> =
        runProvidesClientId(authentication) {
            id -> changePasswordService.generateChangeUserPasswordCode(id, oldPassword)
                .flatMapSingle {
                    code -> changePasswordService.saveResetPassword(code)
                        .flatMap {
                            val email = authentication.attributes["email"] as String? ?: return@flatMap Single.just(false)
                            changePasswordService.sendChangeUserPasswordCodeToEmail(code, email)
                        }
                }.defaultIfEmpty(false)
                .mapBooleanToStatus(HttpStatus.ACCEPTED, HttpStatus.CONFLICT)
        }

    @Post("/reset-password", consumes = [MediaType.APPLICATION_FORM_URLENCODED])
    fun resetUserPassword(code: String, password: String, authentication: Authentication): Single<HttpStatus> =
        runProvidesClientId(authentication) {
            id -> changePasswordService.changeUserPassword(id, code, password)
                .mapBooleanToStatus(HttpStatus.ACCEPTED, HttpStatus.CONFLICT)
        }
}
