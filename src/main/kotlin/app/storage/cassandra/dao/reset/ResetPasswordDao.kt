package app.storage.cassandra.dao.reset

import app.model.reset.ResetPassword
import app.storage.cassandra.dao.BaseDao
import com.datastax.dse.driver.api.mapper.reactive.MappedReactiveResultSet
import com.datastax.oss.driver.api.mapper.annotations.Dao
import com.datastax.oss.driver.api.mapper.annotations.Delete
import com.datastax.oss.driver.api.mapper.annotations.Select
import java.util.UUID
import java.util.concurrent.CompletableFuture

@Dao
interface ResetPasswordDao: BaseDao<ResetPassword> {
    @Select(customWhereClause = "code = :code", limit = "1", allowFiltering = true)
    fun findByCodeReactive(code: String): MappedReactiveResultSet<ResetPassword>

    @Select(customWhereClause = "id = :id AND code = :code", limit = "1", allowFiltering = true)
    fun findByIdAndCodeReactive(id: UUID, code: String): MappedReactiveResultSet<ResetPassword>

    @Delete(entityClass = [ResetPassword::class], customWhereClause = "code = :code")
    fun deleteByCodeAsync(code: String): CompletableFuture<Void>
}
