package app.endpoints

import app.service.account.AccountService
import app.utils.SecurityUtils
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
class ChangePasswordController(private val accountService: AccountService) {
    @Post("/change-password/", consumes = [MediaType.APPLICATION_FORM_URLENCODED])
    fun changePassword(oldPassword: String, authentication: Authentication): Single<HttpStatus> {
        val id = SecurityUtils.getClientId(authentication) ?: return Single.just(HttpStatus.CONFLICT)
        return accountService.generateChangeUserPasswordCode(id, oldPassword)
            .map { if (it) HttpStatus.OK else HttpStatus.CONFLICT }
    }

    @Post("/reset-password/", consumes = [MediaType.APPLICATION_FORM_URLENCODED])
    fun resetUserPassword(code: String, password: String, authentication: Authentication): Single<HttpStatus> {
        val id = SecurityUtils.getClientId(authentication) ?: return Single.just(HttpStatus.CONFLICT)
        return accountService.changeUserPassword(id, code, password)
            .map { if (it) HttpStatus.ACCEPTED else HttpStatus.CONFLICT }
    }
}
