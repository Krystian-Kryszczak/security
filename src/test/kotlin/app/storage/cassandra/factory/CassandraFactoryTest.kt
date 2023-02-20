package app.storage.cassandra.factory

import app.storage.cassandra.dao.DaoMapper
import app.storage.cassandra.dao.security.activation.UserAccountActivationDao
import app.storage.cassandra.dao.being.user.UserDao
import app.storage.cassandra.dao.security.reset.ResetPasswordDao
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldNotBe
import io.micronaut.test.extensions.kotest.annotation.MicronautTest

@MicronautTest
class CassandraFactoryTest(
    private val daoMapper: DaoMapper,
    private val userDao: UserDao,
    private val activationCodeDao: UserAccountActivationDao,
    private val resetPasswordDao: ResetPasswordDao
): StringSpec({
    "cassandra factory beans inject test" {
        daoMapper shouldNotBe null
        userDao shouldNotBe null
        activationCodeDao shouldNotBe null
        resetPasswordDao shouldNotBe null
    }
})
