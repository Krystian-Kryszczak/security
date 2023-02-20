package app.storage.cassandra.dao.security.activation

import app.model.security.code.activation.being.user.UserAccountActivation
import app.storage.cassandra.dao.BaseDao
import com.datastax.dse.driver.api.core.cql.reactive.ReactiveResultSet
import com.datastax.dse.driver.api.mapper.reactive.MappedReactiveResultSet
import com.datastax.oss.driver.api.mapper.annotations.Dao
import com.datastax.oss.driver.api.mapper.annotations.Delete
import com.datastax.oss.driver.api.mapper.annotations.Select

@Dao
interface UserAccountActivationDao: BaseDao<UserAccountActivation> {
    @Select(customWhereClause = "code = :code", limit = "1", allowFiltering = true)
    fun findByCodeReactive(code: String): MappedReactiveResultSet<UserAccountActivation>

    @Select(customWhereClause = "user_email = :email AND code = :code", limit = "1", allowFiltering = true)
    fun findByEmailAndCodeReactive(email: String, code: String): MappedReactiveResultSet<UserAccountActivation>

    @Delete(entityClass = [UserAccountActivation::class], customWhereClause = "code = :code")
    fun deleteByCodeReactive(code: String): ReactiveResultSet
}
