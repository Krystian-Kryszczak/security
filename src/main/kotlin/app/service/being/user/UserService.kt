package app.service.being.user

import app.model.being.user.User
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Maybe
import java.util.UUID

interface UserService {
    fun saveReactive(user: User): Completable
    fun findByIdReactive(id: UUID): Maybe<User>
    fun findByEmailReactive(email: String): Maybe<User>
    fun findByEmailAndPasswordReactive(email: String, password: String): Maybe<User>
    fun updatePasswordByIdReactive(id: UUID, password: String): Completable
    fun deleteByIdReactive(id: UUID): Completable
}
