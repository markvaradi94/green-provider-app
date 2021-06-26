package ro.asis.provider.service.model.entity

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer
import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import ro.asis.commons.model.Address
import ro.asis.commons.model.Dashboard
import ro.asis.commons.model.Inventory
import java.time.LocalDate

@Document(collection = "providers")
class ProviderEntity(
    @Id
    var id: String? = ObjectId.get().toHexString(),

    var accountId: String,
    var name: String,
    var description: String,

    @JsonSerialize(using = LocalDateSerializer::class)
    @JsonFormat(pattern = "yyyy-MM-dd")
    var since: LocalDate,

    var inventory: Inventory,
    var address: Address,
    var dashboard: Dashboard,
)
