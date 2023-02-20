package app.model.being.user

import com.datastax.oss.driver.api.mapper.annotations.Entity
import com.datastax.oss.driver.api.mapper.annotations.SchemaHint

@Entity
@SchemaHint(targetElement = SchemaHint.TargetElement.UDT)
class UserModel(
    var firstname: String? = null,
    var lastname: String? = null,
    var email: String? = null,
    var phoneNumber: String? = null,
    var password: String? = null,
    var dateOfBirthInDays: Int = 0,
    var sex: Byte = 0
)
