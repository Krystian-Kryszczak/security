package app.model.activation

import app.model.being.user.User
import app.model.being.user.UserModel
import com.datastax.oss.driver.api.core.uuid.Uuids
import com.datastax.oss.driver.api.mapper.annotations.Entity
import com.datastax.oss.driver.api.mapper.annotations.PartitionKey
import com.datastax.oss.driver.api.mapper.annotations.SchemaHint
import com.datastax.oss.driver.api.mapper.annotations.Transient
import java.util.UUID
import kotlin.random.Random

@Entity
@SchemaHint(targetElement = SchemaHint.TargetElement.TABLE)
data class UserActivation(
    @PartitionKey
    var code: String = UUID.randomUUID().toString().split(Regex("-")).map { it[Random.nextInt(it.length-1)] }.joinToString(""),
    var userEmail: String? = null,
    var userModel: UserModel? = null,
) {
    @Transient
    fun toUser(): User? {
        val model = userModel ?: return null
        return User(
            id = Uuids.timeBased(),
            name =  model.firstname,
            lastname = model.lastname,
            email = model.email,
            phoneNumber = model.phoneNumber,
            password = model.password!!,
            dateOfBirth = model.dateOfBirth,
            sex = model.sex
        )
    }
}
