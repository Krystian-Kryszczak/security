package app.service.account

import app.model.being.user.UserModel
import io.reactivex.rxjava3.core.Single
import java.util.UUID

interface AccountService {
    fun registerUser(userModel: UserModel): Single<Boolean>
    fun completeActivationUserAccount(email: String, code: String): Single<Boolean>
    fun generateChangeUserPasswordCode(id: UUID, oldPassword: String): Single<Boolean>
    fun changeUserPasswordIfArgsMatchesAndAreValid(id: UUID, resetPasswordCode: String, newPassword: String): Single<Boolean>
}
