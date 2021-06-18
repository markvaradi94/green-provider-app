package ro.asis.provider.service.repository

import org.springframework.data.mongodb.repository.MongoRepository
import ro.asis.provider.service.model.entity.ProviderAccountEntity
import java.util.*

interface ProviderAccountRepository : MongoRepository<ProviderAccountEntity, String> {
    fun findByProviderId(providerId: String): Optional<ProviderAccountEntity>
    fun findByAccountId(accountId: String): Optional<ProviderAccountEntity>
    fun findByAccountIdAndProviderId(accountId: String, providerId: String): Optional<ProviderAccountEntity>
}
