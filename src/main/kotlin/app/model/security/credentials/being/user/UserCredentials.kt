package app.model.security.credentials.being.user

import app.model.Item
import app.model.security.credentials.Credentials
import com.datastax.oss.driver.api.mapper.annotations.Entity
import com.datastax.oss.driver.api.mapper.annotations.PartitionKey
import com.datastax.oss.driver.api.mapper.annotations.SchemaHint
import com.datastax.oss.driver.api.mapper.annotations.Transient
import java.util.UUID

@Entity
@SchemaHint(targetElement = SchemaHint.TargetElement.TABLE)
data class UserCredentials(
    @PartitionKey
    override val id: UUID? = null,
    val username: String? = null,
    val hashedPassword: String? = null
): Credentials, Item(id) {
    @Transient
    override fun getIdentity(): String = username!!
    @Transient
    override fun getSecret(): String = hashedPassword!!
}
