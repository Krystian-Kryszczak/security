package app.model.activation.being.user

import app.model.being.user.User
import app.model.being.user.UserModel
import app.security.generator.activation.ActivationAccountCodeGenerator
import com.datastax.oss.driver.api.core.uuid.Uuids
import com.datastax.oss.driver.api.mapper.annotations.Entity
import com.datastax.oss.driver.api.mapper.annotations.PartitionKey
import com.datastax.oss.driver.api.mapper.annotations.SchemaHint
import com.datastax.oss.driver.api.mapper.annotations.Transient

@Entity
@SchemaHint(targetElement = SchemaHint.TargetElement.TABLE)
data class UserAccountActivation(
    @PartitionKey
    var code: String = ActivationAccountCodeGenerator.generateCode(),
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
