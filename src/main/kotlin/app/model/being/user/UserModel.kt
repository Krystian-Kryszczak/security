package app.model.being.user

import com.datastax.oss.driver.api.mapper.annotations.Entity
import com.datastax.oss.driver.api.mapper.annotations.PropertyStrategy
import com.datastax.oss.driver.api.mapper.annotations.SchemaHint
import io.micronaut.core.annotation.Introspected

@Entity
@PropertyStrategy(mutable = false)
@SchemaHint(targetElement = SchemaHint.TargetElement.UDT)
@Introspected
data class UserModel(
    val firstname: String? = null,
    val lastname: String? = null,
    val email: String? = null,
    val phoneNumber: String? = null,
    val password: String? = null,
    val dateOfBirthInDays: Int = 0,
    val sex: Byte = 0,
)
