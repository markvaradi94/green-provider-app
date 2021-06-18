package ro.asis.provider.dto

import ro.asis.commons.enums.AccountType
import ro.asis.commons.model.Address
import java.time.LocalDate

data class ProviderAccount(
    var id: String?,
    var accountId: String,
    var providerId: String,
    var name: String,
    var since: LocalDate,
    var address: Address,
    var username: String,
    var email: String,
    var phoneNumber: String,
    var type: AccountType
)
