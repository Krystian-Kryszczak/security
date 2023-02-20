package app.model.security.credentials

import app.model.Item
import com.datastax.oss.driver.api.mapper.annotations.PartitionKey
import io.micronaut.security.authentication.AuthenticationRequest
import java.io.Serializable
import java.util.*

/***
 * @param identity for example: email, phoneNumber
 */
abstract class AbstractCredentials(
    @PartitionKey
    override val id: UUID? = null,
    private val identity: String? = null,
    private val hashedPassword: String? = null
): Serializable, AuthenticationRequest<String, String>, Item(id) {
    override fun getIdentity(): String = identity!!
    override fun getSecret(): String = hashedPassword!!
}
