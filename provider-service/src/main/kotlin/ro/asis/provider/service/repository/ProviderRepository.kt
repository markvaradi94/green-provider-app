package ro.asis.provider.service.repository

import org.springframework.data.mongodb.repository.MongoRepository
import ro.asis.provider.service.model.entity.ProviderEntity
import java.util.*

interface ProviderRepository : MongoRepository<ProviderEntity, String> {
    fun existsByAccountId(accountId: String): Boolean
    fun findByAccountId(accountId: String): Optional<ProviderEntity>
}
