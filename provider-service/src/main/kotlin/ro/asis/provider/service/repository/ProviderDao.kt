package ro.asis.provider.service.repository

import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.stereotype.Repository
import ro.asis.commons.filters.ProviderFilters
import ro.asis.provider.service.model.entity.ProviderEntity
import java.util.*
import java.util.Optional.ofNullable

@Repository
class ProviderDao(private val mongo: MongoTemplate) {
    fun findProviders(filters: ProviderFilters): List<ProviderEntity> {
        val query = Query()
        val criteria = buildCriteria(filters)

        if (criteria.isNotEmpty()) query.addCriteria(Criteria().andOperator(*criteria.toTypedArray()))

        return mongo.find(query, ProviderEntity::class.java).toList()
    }

    private fun buildCriteria(filters: ProviderFilters): List<Criteria> {
        val criteria = mutableListOf<Criteria>()

        ofNullable(filters.id)
            .ifPresent { criteria.add(Criteria.where("id").`is`(it)) }
        ofNullable(filters.name)
            .ifPresent { criteria.add(Criteria.where("name").regex(".*$it.*", "i")) }
        ofNullable(filters.city)
            .ifPresent { criteria.add(Criteria.where("address.city").regex(".*$it.*", "i")) }
        ofNullable(filters.streetName)
            .ifPresent { criteria.add(Criteria.where("address.streetName").regex(".*$it.*", "i")) }
        ofNullable(filters.accountId)
            .ifPresent { criteria.add(Criteria.where("accountId").`is`(it)) }

        return criteria
    }
}
