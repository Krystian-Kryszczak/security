package app.model.being.user

import app.model.Item
import com.datastax.oss.driver.api.mapper.annotations.Entity
import com.datastax.oss.driver.api.mapper.annotations.PartitionKey
import com.datastax.oss.driver.api.mapper.annotations.SchemaHint
import java.util.UUID

@Entity
@SchemaHint(targetElement = SchemaHint.TargetElement.TABLE)
class UserAuthCredentials(
    @PartitionKey
    override var id: UUID? = null,
    var email: String? = null,
    var hashedPassword: String? = null,
): Item(id)
