package app.service.security.registration

import app.model.being.user.UserModel
import app.model.security.code.activation.being.user.UserAccountActivation
import app.security.encoder.PasswordEncoder
import app.security.validation.PasswordValidator
import app.service.analytics.MetricsService
import app.service.being.user.UserService
import app.service.mailer.smtp.SmtpMailerService
import app.storage.cassandra.dao.security.activation.UserAccountActivationDao
import app.storage.cassandra.dao.security.reset.ResetPasswordDao
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.core.Single
import jakarta.inject.Singleton
import org.slf4j.Logger
import org.slf4j.LoggerFactory

@Singleton
class UserRegistrationServiceImpl(
    private val metricsService: MetricsService,
    private val activationCodeDao: UserAccountActivationDao,
    private val resetPasswordDao: ResetPasswordDao,
    private val userService: UserService,
    private val passwordEncoder: PasswordEncoder,
    private val smtpMailerService: SmtpMailerService,
): UserRegistrationService {
    override fun registerUser(userModel: UserModel): Single<Boolean> {
        if (!PasswordValidator.validate(userModel.password)) {
            logger.info("Invalid user password!${if (userModel.email != null) " User email: ${userModel.email}" else ""}")
            return Single.just(false) // return false if password is invalid!
        }
        userModel.password = passwordEncoder.encode(userModel.password!!) // important
        return generateActivationCodeForUser(userModel)
            .flatMap { code -> run {
                val email: String = userModel.email ?: return@run Single.just(false)
                logger.info("The activation code `$code` will be sent to the email address $email")
                smtpMailerService.sendUserActivationCode(email, code)
            }}.onErrorReturn {
                it.printStackTrace()
                return@onErrorReturn false
            }
    }

    override fun completeActivationUserAccount(email: String, code: String): Single<Boolean> =
        activationCodeMatches(email, code)
            .flatMapSingle {
                    activation -> run {
                val user = activation.mapToUser() ?: return@run Single.just(false) // Activation failed //
                Completable.fromPublisher(activationCodeDao.deleteReactive(activation)) // Delete activation //
                    .andThen(userService.saveReactive(user)) // ... and save user //
                    .doOnComplete {
                        logger.info("Successful activated account with email $email")
                        metricsService.incrementActivatedAccounts() // increment successful activated account metric //
                    }.toSingleDefault(true) // Activation successful //
                    .onErrorReturn {
                        it.printStackTrace()
                        metricsService.incrementActivationAccountFails() // increment activation accounts fails metric //
                        return@onErrorReturn false // Activation failed //
                    }
            }
            }.switchIfEmpty(Single.create {
                metricsService.incrementActivationAccountFails() // increment activation accounts fails metric //
                it.onSuccess(false) // Activation failed //
            }).onErrorReturn {
                metricsService.incrementActivationAccountFails() // increment activation accounts fails metric //
                return@onErrorReturn false // Activation failed //
            }

    private fun generateActivationCodeForUser(userModel: UserModel): Single<String> {
        val activation = UserAccountActivation.createWithGeneratedCode(
            userEmail = userModel.email,
            userModel = userModel,
        )
        return Completable.fromPublisher(activationCodeDao.saveReactive(activation))
            .doOnComplete {
                metricsService.incrementGeneratedActivationCodes()
            }.toSingleDefault(activation.code)
    }

    private fun activationCodeMatches(userEmail: String, code: String): Maybe<UserAccountActivation> =
        Maybe.fromPublisher(activationCodeDao.findByEmailAndCodeReactive(userEmail, code))

    companion object {
        val logger: Logger = LoggerFactory.getLogger(UserRegistrationServiceImpl::class.java)
    }
}