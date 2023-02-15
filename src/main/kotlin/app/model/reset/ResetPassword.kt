package app.model.reset

import app.security.generator.reset.ResetPasswordCodeGenerator
import com.datastax.oss.driver.api.mapper.annotations.Entity
import com.datastax.oss.driver.api.mapper.annotations.PartitionKey
import com.datastax.oss.driver.api.mapper.annotations.SchemaHint
import java.util.UUID

@Entity
@SchemaHint(targetElement = SchemaHint.TargetElement.TABLE)
data class ResetPassword(
    @PartitionKey
    var code: String = ResetPasswordCodeGenerator.generateCode(),
    var id: UUID? = null
)
