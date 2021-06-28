package ro.asis.provider.service.model.entity

import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "provider_accounts")
class ProviderAccountEntity(
    @Id
    var id: String = ObjectId.get().toHexString(),

    var providerId: String,
    var accountId: String,
    var username: String,
    var email: String,
    var phoneNumber: String
)
