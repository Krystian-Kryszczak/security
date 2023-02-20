package app.model.security.code.activation.being.user

import app.model.being.user.User
import app.model.being.user.UserModel
import app.model.security.code.activation.AccountActivation
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
    override val code: String? = null,
    val identity: String? = null,
    val userModel: UserModel? = null,
): AccountActivation(code) {
    companion object {
        fun createWithGeneratedCode(userEmail: String? = null, userModel: UserModel? = null): UserAccountActivation =
            UserAccountActivation(
                ActivationAccountCodeGenerator.generateCode(),
                userEmail,
                userModel
            )
    }

    @Transient
    fun mapToUser(): User? {
        val model = userModel ?: return null
        return User(
            id = Uuids.timeBased(),
            name =  model.firstname,
            lastname = model.lastname,
            email = model.email,
            phoneNumber = model.phoneNumber,
            dateOfBirthInDays = model.dateOfBirthInDays,
            sex = model.sex
        )
    }
}
