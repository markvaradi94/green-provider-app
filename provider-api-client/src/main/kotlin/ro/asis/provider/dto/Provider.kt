package ro.asis.provider.dto

import ro.asis.commons.model.Address
import ro.asis.commons.model.Dashboard
import ro.asis.commons.model.Inventory
import java.time.LocalDate

data class Provider(
    val id: String?,
    val accountId: String,
    val name: String,
    val description: String,
    val since: LocalDate,
    val inventory: Inventory,
    val address: Address,
    val dashboard: Dashboard,
)
