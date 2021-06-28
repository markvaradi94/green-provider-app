package ro.asis.provider.dto

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer
import org.bson.types.ObjectId
import ro.asis.commons.model.Address
import ro.asis.commons.model.Dashboard
import ro.asis.commons.model.Inventory
import java.time.LocalDate

data class Provider(
    @JsonProperty("id")
    val id: String = ObjectId.get().toHexString(),

    @JsonProperty("accountId")
    val accountId: String,

    @JsonProperty("name")
    val name: String,

    @JsonProperty("description")
    val description: String,

    @JsonSerialize(using = LocalDateSerializer::class)
    @JsonFormat(pattern = "yyyy-MM-dd")
    @JsonProperty("since")
    val since: LocalDate,

    @JsonProperty("inventory")
    var inventory: Inventory,

    @JsonProperty("address")
    val address: Address,

    @JsonProperty("dashboard")
    var dashboard: Dashboard,
)
