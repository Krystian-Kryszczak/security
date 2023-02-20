package app.storage.cassandra.event

import com.datastax.oss.driver.api.core.CqlSession
import com.datastax.oss.driver.api.core.cql.SimpleStatement
import com.datastax.oss.driver.api.core.type.DataTypes
import com.datastax.oss.driver.api.querybuilder.SchemaBuilder
import io.micronaut.context.event.StartupEvent
import io.micronaut.runtime.event.annotation.EventListener
import jakarta.inject.Singleton
import org.slf4j.Logger
import org.slf4j.LoggerFactory

@Singleton
class CassandraStartup(private val cqlSession: CqlSession) {
    @EventListener
    internal fun onStartupEvent(event: StartupEvent) {
        createKeyspace()
        createUserTable()
        createUserCredentialsTable()
        createUserModel()
        createActivationCodeTable()
        createResetPasswordTable()
    }
    private fun createKeyspace() = execute(
        SchemaBuilder
            .createKeyspace(cqlSession.keyspace.get()).ifNotExists()
            .withSimpleStrategy(2)
            .build()
    )
    private fun createUserTable() = execute(
        SchemaBuilder
            .createTable("user").ifNotExists()
            .withPartitionKey("id", DataTypes.TIMEUUID)
            .withColumn("name", DataTypes.TEXT)
            .withColumn("lastname", DataTypes.TEXT)
            .withColumn("email", DataTypes.TEXT)
            .withColumn("phone_number", DataTypes.TEXT)
            .withColumn("date_of_birth_in_days", DataTypes.INT)
            .withColumn("sex", DataTypes.TINYINT)
            .build()
    )
    private fun createUserCredentialsTable() = execute(
        SchemaBuilder
            .createTable("user_credentials")
            .withPartitionKey("id", DataTypes.TIMEUUID)
            .withColumn("identity", DataTypes.TEXT)
            .withColumn("hashed_password", DataTypes.TEXT)
            .build()
    )
    private fun createUserModel() = execute(
        SchemaBuilder
            .createType("user_model").ifNotExists()
            .withField("firstname", DataTypes.TEXT)
            .withField("lastname", DataTypes.TEXT)
            .withField("email", DataTypes.TEXT)
            .withField("password", DataTypes.TEXT)
            .withField("phone_number", DataTypes.TEXT)
            .withField("date_of_birth_in_days", DataTypes.INT)
            .withField("sex", DataTypes.TINYINT)
            .build()
    )
    private fun createActivationCodeTable() =
        execute("CREATE TABLE IF NOT EXISTS user_activation (code text PRIMARY KEY,user_email text,user_model user_model)")
    private fun createResetPasswordTable() =
        execute("CREATE TABLE IF NOT EXISTS reset_password (code text PRIMARY KEY,id timeuuid)")
    private fun execute(statement: SimpleStatement) {
        cqlSession.execute(statement)
        logger.info("Query \"${statement.query}\" was executed.")
    }
    private fun execute(query: String) {
        cqlSession.execute(query)
        logger.info("Query \"$query\" was executed.")
    }
    companion object {
        private val logger: Logger = LoggerFactory.getLogger(CassandraStartup::class.java)
    }
}
