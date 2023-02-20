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
    @Delete(entityClass = [User::class])
    fun deleteByIdReactive(id: UUID?): ReactiveResultSet
}
