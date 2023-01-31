package app.model.being.user

import app.model.being.Being
import com.datastax.oss.driver.api.mapper.annotations.*
import java.time.LocalDate
import java.util.UUID

/**
 * @param dateOfBirth - "day" + "month" + "year" converted to int
 * @param sex - unselected = 0; male = 1; female = 2; unknown = 3;
 */
@Entity
@SchemaHint(targetElement = SchemaHint.TargetElement.TABLE)
data class User(
    @PartitionKey
    override var id: UUID? = null,
    override var name: String? = null,
    var lastname: String? = null,
    var email: String? = null,
    var phoneNumber: String? = null,
    var password: String? = null,
    var dateOfBirth: Int = 0,
    var sex: Byte = 0
): Being(id, name) {
    companion object {
        fun getDateOfBirthAsInt(localDate: LocalDate): Int { // TODO
            val day = localDate.dayOfMonth
            val month = localDate.monthValue
            val year = localDate.year

            val yearsAsDays = (year * 12)

            return (localDate.dayOfMonth.toString() + // Day
                    (if (month < 10) "0$month" else month.toString()) + // Month
                        year.toString()) // Year
                            .toInt() // convert to Int
        }
    }
}
