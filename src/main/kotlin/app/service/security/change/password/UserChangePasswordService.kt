package app.service.security.change.password

import app.model.security.code.reset.ResetPassword
import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.core.Single
import java.util.UUID

interface UserChangePasswordService {
    fun generateChangeUserPasswordCode(id: UUID, oldPassword: String): Maybe<String>
    fun saveResetPassword(resetPassword: ResetPassword): Single<Boolean>
    fun sendChangeUserPasswordCodeToEmail(resetPassword: ResetPassword, email: String): Single<Boolean>
    fun changeUserPassword(id: UUID, resetPasswordCode: String, newPassword: String): Single<Boolean>
}
