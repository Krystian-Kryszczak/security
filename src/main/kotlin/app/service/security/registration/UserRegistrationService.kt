package app.service.security.registration

import app.model.being.user.UserModel
import io.reactivex.rxjava3.core.Single

interface UserRegistrationService {
    fun registerUser(userModel: UserModel): Single<Boolean>
    fun completeActivationUserAccount(email: String, code: String): Single<Boolean>
}
