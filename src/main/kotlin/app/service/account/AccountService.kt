package app.service.account

import app.model.activation.UserActivation
import app.model.being.user.UserModel
import app.model.reset.ResetPassword
import app.security.encoder.PasswordEncoder
import app.security.validation.PasswordValidator
import app.service.analytics.MetricsService
import app.service.being.user.UserService
import app.service.mailer.MailerService
import app.storage.cassandra.dao.activation.UserActivationDao
import app.storage.cassandra.dao.reset.ResetPasswordDao
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.core.Single
import jakarta.inject.Singleton
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.util.UUID
import kotlin.random.Random

@Singleton
class AccountService(
    private val metricsService: MetricsService,
    private val activationCodeDao: UserActivationDao,
    private val resetPasswordDao: ResetPasswordDao,
    private val userService: UserService,
    private val passwordEncoder: PasswordEncoder,
    private val mailerService: MailerService,
) {
    fun registerUser(userModel: UserModel): Single<Boolean> {
        if (!PasswordValidator.validate(userModel.password)) {
            logger.info("Invalid user password!${if (userModel.email != null) " User email: ${userModel.email}" else ""}")
            return Single.just(false) // return false if password is invalid!
        }
        userModel.password = passwordEncoder.encode(userModel.password!!) // important
        return generateActivationCodeForUser(userModel)
            .flatMap { code -> run {
                val email: String = userModel.email ?: return@run Single.just(false)
                logger.info("The activation code `$code` will be sent to the email address $email")
                mailerService.sendUserActivationCode(email, code)
            }}.onErrorReturn {
                it.printStackTrace()
                return@onErrorReturn false
            }
    }

    private fun generateActivationCodeForUser(userModel: UserModel): Single<String> {
        val activation = UserActivation(
            userEmail = userModel.email,
            userModel = userModel,
        )
        return Completable.fromPublisher(activationCodeDao.saveReactive(activation))
            .doOnComplete {
                metricsService.incrementGeneratedActivationCodes()
            }.toSingleDefault(activation.code)
    }

    private fun activationCodeMatches(userEmail: String, code: String): Maybe<UserActivation> =
        Maybe.fromPublisher(activationCodeDao.findByEmailAndCodeReactive(userEmail, code))

    fun completeActivationUserAccount(email: String, code: String): Single<Boolean> =
        activationCodeMatches(email, code)
        .flatMapSingle {
            activation -> run {
                val user = activation.toUser() ?: return@run Single.just(false) // Activation failed //
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

    /**
     * Generates reset user password code.
     */
    fun generateChangeUserPasswordCode(id: UUID, oldPassword: String): Single<Boolean> {
        if (!PasswordValidator.validate(oldPassword)) return Single.just(false)
        val resetPassword = ResetPassword(
            code = UUID.randomUUID().toString().split(Regex("-")).map { it[Random.nextInt(it.length-1)] }.joinToString(),
            id = id
        )
        return userService.findByIdReactive(id)
            .flatMap {
                if (passwordEncoder.matches(oldPassword, it.password ?: return@flatMap Maybe.empty())) {
                    return@flatMap Maybe.just(it)
                } else {
                    return@flatMap Maybe.empty()
                }
            }.flatMapSingle { user ->
                Completable.fromPublisher(resetPasswordDao.saveReactive(resetPassword))
                    .andThen(if (user.email != null) mailerService.sendUserResetPasswordCode(user.email!!, resetPassword.code) else Single.just(false))
            }.defaultIfEmpty(false).onErrorReturnItem(false)
    }

    /**
     * Changes user password if user id, code matches and if new password is valid.
     */
    fun changeUserPassword(id: UUID, resetPasswordCode: String, newPassword: String): Single<Boolean> {
        // return false if resetPasswordCode is blank or if new password is invalid.
        if (resetPasswordCode.isBlank() || !PasswordValidator.validate(newPassword)) return Single.just(false)

        return Maybe.fromPublisher(resetPasswordDao.findByIdAndCodeReactive(id, resetPasswordCode)) // find reset-password record in table
            .flatMapSingle { // if it has been found
                userService.updatePasswordByIdReactive(id, newPassword) // update password
                    .andThen(Completable.fromFuture(resetPasswordDao.deleteByCodeAsync(resetPasswordCode))) // remove reset-password record from table
                        .andThen(Single.just(true)) // if successful
                        .onErrorReturnItem(false) // else
            }.defaultIfEmpty(false) // if not found also return false
    }

    companion object {
        val logger: Logger = LoggerFactory.getLogger(AccountService::class.java)
    }
}
