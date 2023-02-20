package app.storage.cassandra.dao

import app.storage.cassandra.dao.security.activation.UserAccountActivationDao
import app.storage.cassandra.dao.being.user.UserDao
import app.storage.cassandra.dao.security.credentials.UserCredentialsDao
import app.storage.cassandra.dao.security.reset.ResetPasswordDao
import com.datastax.oss.driver.api.core.CqlIdentifier
import com.datastax.oss.driver.api.core.CqlSession
import com.datastax.oss.driver.api.mapper.MapperBuilder
import com.datastax.oss.driver.api.mapper.annotations.DaoFactory
import com.datastax.oss.driver.api.mapper.annotations.DaoKeyspace
import com.datastax.oss.driver.api.mapper.annotations.Mapper

@Mapper
interface DaoMapper {
    @DaoFactory
    fun userDao(@DaoKeyspace keyspace: CqlIdentifier): UserDao
    @DaoFactory
    fun userAuthenticationCredentialsDao(@DaoKeyspace keyspace: CqlIdentifier): UserCredentialsDao
    @DaoFactory
    fun activationCodeDao(@DaoKeyspace keyspace: CqlIdentifier): UserAccountActivationDao
    @DaoFactory
    fun resetUserPasswordDao(@DaoKeyspace keyspace: CqlIdentifier): ResetPasswordDao
    companion object {
        @JvmStatic
        fun builder(session: CqlSession): MapperBuilder<DaoMapper> {
            return DaoMapperBuilder(session)
        }
    }
}
