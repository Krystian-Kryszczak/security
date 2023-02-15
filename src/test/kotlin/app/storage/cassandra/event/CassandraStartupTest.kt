package app.storage.cassandra.event

import com.datastax.oss.driver.api.core.CqlSession
import io.kotest.core.spec.style.StringSpec
import io.micronaut.test.extensions.kotest.annotation.MicronautTest

@MicronautTest
class CassandraStartupTest(
    private val cqlSession: CqlSession,

    val testingKeySpaceName: String = cqlSession.keyspace.get().toString() + "-testing",
    private val testingCqlSession: CqlSession,

    val cassandraStartup: CassandraStartup = CassandraStartup(testingCqlSession)
): StringSpec({
    "" {
        // TODO
    }
})
