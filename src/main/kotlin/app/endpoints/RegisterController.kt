package app.endpoints

import app.model.being.user.UserModel
import app.service.account.AccountService
import io.micronaut.http.HttpStatus
import io.micronaut.http.MediaType
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Post
import io.micronaut.security.annotation.Secured
import io.micronaut.security.rules.SecurityRule
import io.reactivex.rxjava3.core.Single
import org.slf4j.Logger
import org.slf4j.LoggerFactory

@Secured(SecurityRule.IS_ANONYMOUS)
@Controller
class RegisterController(private val accountService: AccountService) {

    @Post(value = "/register", consumes = [MediaType.APPLICATION_FORM_URLENCODED])
    fun register(userModel: UserModel): Single<HttpStatus> =
        accountService.registerUser(userModel)
        .mapToStatusOnSuccessfulOtherwiseToConflictStatus(HttpStatus.ACCEPTED)

    @Post(value = "/activate-account", consumes = [MediaType.APPLICATION_FORM_URLENCODED])
    fun activateAccount(email: String, code: String): Single<HttpStatus> =
        accountService.completeActivationUserAccount(email, code)
        .mapToStatusOnSuccessfulOtherwiseToConflictStatus(HttpStatus.CREATED)

    private fun Single<Boolean>.mapToStatusOnSuccessfulOtherwiseToConflictStatus(onSuccess: HttpStatus) =
        map { if (it) onSuccess else HttpStatus.CONFLICT }

    companion object {
        val logger: Logger = LoggerFactory.getLogger(RegisterController::class.java)
    }
}
