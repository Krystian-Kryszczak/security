package app.endpoints

import app.model.being.user.UserModel
import app.service.security.registration.UserRegistrationService
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
class RegisterController(private val registrationService: UserRegistrationService): BaseController() {

    @Post(value = "/register", consumes = [MediaType.APPLICATION_FORM_URLENCODED])
    fun register(userModel: UserModel): Single<HttpStatus> =
        registrationService.registerUser(userModel)
        .mapBooleanToStatus(HttpStatus.ACCEPTED, HttpStatus.CONFLICT)

    @Post(value = "/activate-account", consumes = [MediaType.APPLICATION_FORM_URLENCODED])
    fun activateAccount(email: String, code: String): Single<HttpStatus> =
        registrationService.completeActivationUserAccount(email, code)
        .mapBooleanToStatus(HttpStatus.ACCEPTED, HttpStatus.CONFLICT)

    companion object {
        val logger: Logger = LoggerFactory.getLogger(RegisterController::class.java)
    }
}
