package app.model.security.credentials.being.user

import app.model.security.credentials.AbstractCredentials
import com.datastax.oss.driver.api.mapper.annotations.Entity
import com.datastax.oss.driver.api.mapper.annotations.PartitionKey
import com.datastax.oss.driver.api.mapper.annotations.SchemaHint
import io.micronaut.core.annotation.Introspected
import java.util.UUID

/***
 * @param identity for example: email, phoneNumber
 */
@Entity
@SchemaHint(targetElement = SchemaHint.TargetElement.TABLE)
@Introspected
class UserCredentials(
    @PartitionKey
    override val id: UUID? = null,
    val identity: String? = null,
    val hashedPassword: String? = null,
): AbstractCredentials(id, identity, hashedPassword)
