package app.service.mailer.smtp

import io.reactivex.rxjava3.core.Single

interface SmtpMailerService {
    fun sendUserActivationCode(receiverAddress: String, activationCode: String): Single<Boolean>
    fun sendUserResetPasswordCode(receiverAddress: String, resetPasswordCode: String): Single<Boolean>
}
