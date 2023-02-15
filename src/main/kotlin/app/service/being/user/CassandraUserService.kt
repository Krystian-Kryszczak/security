package app.service.being.user

import app.model.being.user.User
import app.storage.cassandra.dao.being.user.UserDao
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Maybe
import jakarta.inject.Singleton
import java.util.UUID

@Singleton
class CassandraUserService(private val userDao: UserDao): UserService {
    override fun saveReactive(user: User): Completable = Completable.fromPublisher(userDao.saveReactive(user))
    override fun findByIdReactive(id: UUID): Maybe<User> = Maybe.fromPublisher(userDao.findByIdAsync(id))
    override fun findByEmailReactive(email: String): Maybe<User> = Maybe.fromPublisher(userDao.findByEmailReactive(email))
    override fun findByEmailAndPasswordReactive(email: String, password: String): Maybe<User> = Maybe.fromPublisher(userDao.findByEmailAndPasswordReactive(email, password))
    override fun updatePasswordByIdReactive(id: UUID, password: String): Completable = Completable.fromPublisher(userDao.updatePasswordByIdReactive(id, password))
    override fun deleteByIdReactive(id: UUID): Completable = Completable.fromPublisher(userDao.deleteByIdReactive(id))
}
