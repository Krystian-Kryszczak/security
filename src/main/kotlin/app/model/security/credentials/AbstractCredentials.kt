package app.model.security.credentials

import app.model.Item
import com.datastax.oss.driver.api.mapper.annotations.Transient
import io.micronaut.security.authentication.AuthenticationRequest
import java.io.Serializable
import java.util.UUID

/***
 * @param identity for example: email, phoneNumber
 */
abstract class AbstractCredentials(
    id: UUID? = null,
    private val identity: String? = null,
    private val hashedPassword: String? = null
): Serializable, AuthenticationRequest<String, String>, Item(id) {
    @Transient
    override fun getIdentity(): String = identity!!
    @Transient
    override fun getSecret(): String = hashedPassword!!
}
