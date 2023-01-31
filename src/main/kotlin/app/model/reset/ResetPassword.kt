package app.model.reset

import com.datastax.oss.driver.api.mapper.annotations.Entity
import com.datastax.oss.driver.api.mapper.annotations.PartitionKey
import com.datastax.oss.driver.api.mapper.annotations.SchemaHint
import java.util.UUID
import kotlin.random.Random

@Entity
@SchemaHint(targetElement = SchemaHint.TargetElement.TABLE)
data class ResetPassword(
    @PartitionKey
    var code: String = UUID.randomUUID().toString().split(Regex("-")).map { it[Random.nextInt(it.length-1)] }.joinToString(""),
    var id: UUID? = null
)
