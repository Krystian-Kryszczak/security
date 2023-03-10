package app.storage.cassandra.factory

import app.storage.cassandra.dao.DaoMapper
import app.storage.cassandra.dao.security.activation.UserAccountActivationDao
import app.storage.cassandra.dao.being.user.UserDao
import app.storage.cassandra.dao.security.credentials.UserCredentialsDao
import app.storage.cassandra.dao.security.reset.ResetPasswordDao
import com.datastax.oss.driver.api.core.CqlIdentifier
import com.datastax.oss.driver.api.core.CqlSession
import io.micronaut.context.annotation.Factory
import jakarta.inject.Singleton

@Factory
class CassandraFactory(cqlSession: CqlSession) {
    private val keyspace: CqlIdentifier = cqlSession.keyspace.get()
    private val daoMapper: DaoMapper = DaoMapper.builder(cqlSession).withDefaultKeyspace(keyspace).build()

    @Singleton
    fun daoMapper(): DaoMapper = daoMapper
    @Singleton
    fun userDao(): UserDao {
        return daoMapper.userDao(keyspace)
    }
    @Singleton
    fun userAuthenticationCredentialsDao(): UserCredentialsDao {
        return daoMapper.userAuthenticationCredentialsDao(keyspace)
    }
    @Singleton
    fun userActivationDao(): UserAccountActivationDao {
        return daoMapper.activationCodeDao(keyspace)
    }
    @Singleton
    fun resetPasswordDao(): ResetPasswordDao {
        return daoMapper.resetUserPasswordDao(keyspace)
    }
}
