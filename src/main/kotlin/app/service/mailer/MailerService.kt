package app.service.mailer

import io.reactivex.rxjava3.core.Single

interface MailerService {
    fun sendUserActivationCode(address: String, code: String): Single<Boolean>
    fun sendUserResetPasswordCode(address: String, code: String): Single<Boolean>
}
