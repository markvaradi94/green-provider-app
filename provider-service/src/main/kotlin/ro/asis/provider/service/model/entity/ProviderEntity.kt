package ro.asis.provider.service.model.entity

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
    var since: LocalDate,
    var inventory: Inventory = Inventory(),
    var address: Address = Address(),
    var dashboard: Dashboard = Dashboard()
)
