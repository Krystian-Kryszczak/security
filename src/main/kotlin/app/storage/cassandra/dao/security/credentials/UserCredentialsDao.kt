package app.storage.cassandra.dao.security.credentials

import app.model.security.credentials.being.user.UserCredentials
import app.storage.cassandra.dao.ItemDao
import com.datastax.dse.driver.api.core.cql.reactive.ReactiveResultSet
import com.datastax.dse.driver.api.mapper.reactive.MappedReactiveResultSet
import com.datastax.oss.driver.api.mapper.annotations.Dao
import com.datastax.oss.driver.api.mapper.annotations.Delete
import com.datastax.oss.driver.api.mapper.annotations.Query
import com.datastax.oss.driver.api.mapper.annotations.Select
import java.util.UUID

@Dao
interface UserCredentialsDao: ItemDao<UserCredentials> {
    @Select(customWhereClause = "email = :email", limit = "1", allowFiltering = true)
    fun findByEmailReactive(email: String): MappedReactiveResultSet<UserCredentials>
    @Select(customWhereClause = "email = :email AND password = :password", limit = "1", allowFiltering = true)
    fun findByEmailAndPasswordReactive(email: String, password: String): MappedReactiveResultSet<UserCredentials>
    @Query("UPDATE user SET password = ':password' WHERE id = :id")
    fun updatePasswordByIdReactive(id: UUID, password: String): ReactiveResultSet
    @Delete(entityClass = [UserCredentials::class])
    fun deleteByIdReactive(id: UUID?): ReactiveResultSet
}
