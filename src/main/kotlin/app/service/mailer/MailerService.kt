package app.service.mailer

import io.reactivex.rxjava3.core.Single

interface MailerService {
    fun sendUserActivationCode(receiverAddress: String, activationCode: String): Single<Boolean>
    fun sendUserResetPasswordCode(receiverAddress: String, resetPasswordCode: String): Single<Boolean>
}
