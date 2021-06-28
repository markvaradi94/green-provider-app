package ro.asis.provider.dto

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer
import org.bson.types.ObjectId
import ro.asis.commons.enums.AccountType
import ro.asis.commons.model.Address
import java.time.LocalDate

data class ProviderAccount(
    @JsonProperty("id")
    var id: String = ObjectId.get().toHexString(),

    @JsonProperty("accountId")
    var accountId: String,

    @JsonProperty("providerId")
    var providerId: String,

    @JsonProperty("name")
    var name: String,

    @JsonSerialize(using = LocalDateSerializer::class)
    @JsonFormat(pattern = "yyyy-MM-dd")
    @JsonProperty("since")
    var since: LocalDate,

    @JsonProperty("address")
    var address: Address,

    @JsonProperty("username")
    var username: String,

    @JsonProperty("email")
    var email: String,

    @JsonProperty("phoneNumber")
    var phoneNumber: String,

    @JsonProperty("type")
    var type: AccountType
)
