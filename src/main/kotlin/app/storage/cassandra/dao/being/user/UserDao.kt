package app.storage.cassandra.dao.being.user

import app.model.being.user.User
import app.storage.cassandra.dao.ItemDao
import com.datastax.dse.driver.api.core.cql.reactive.ReactiveResultSet
import com.datastax.dse.driver.api.mapper.reactive.MappedReactiveResultSet
import com.datastax.oss.driver.api.mapper.annotations.*
import java.util.UUID

@Dao
interface UserDao: ItemDao<User> {
    @Select(customWhereClause = "email = :email", limit = "1", allowFiltering = true)
    fun findByEmailReactive(email: String): MappedReactiveResultSet<User>
    @Select(customWhereClause = "email = :email AND password = :password", limit = "1", allowFiltering = true)
    fun findByEmailAndPasswordReactive(email: String, password: String): MappedReactiveResultSet<User>
    @Query("UPDATE user SET password = ':password' WHERE id = :id")
    fun updatePasswordByIdReactive(id: UUID, password: String): ReactiveResultSet
    @Delete(entityClass = [User::class])
    fun deleteByIdReactive(id: UUID?): ReactiveResultSet
}
