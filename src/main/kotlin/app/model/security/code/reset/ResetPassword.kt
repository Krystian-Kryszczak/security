package app.model.security.code.reset

import app.model.security.code.HaveCode
import app.security.generator.reset.ResetPasswordCodeGenerator
import com.datastax.oss.driver.api.mapper.annotations.Entity
import com.datastax.oss.driver.api.mapper.annotations.PartitionKey
import com.datastax.oss.driver.api.mapper.annotations.SchemaHint
import java.util.UUID

@Entity
@SchemaHint(targetElement = SchemaHint.TargetElement.TABLE)
data class ResetPassword(
    @PartitionKey
    override val code: String? = null,
    val id: UUID? = null
): HaveCode {
    companion object {
        fun createWithGeneratedCode(id: UUID?): ResetPassword =
            ResetPassword(
                ResetPasswordCodeGenerator.generateCode(),
                id
            )
    }
}
