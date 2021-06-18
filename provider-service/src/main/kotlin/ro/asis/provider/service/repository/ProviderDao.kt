package ro.asis.provider.service.repository

import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.stereotype.Repository
import ro.asis.commons.filters.ProviderFilters
import ro.asis.provider.service.model.entity.ProviderEntity
import java.util.*

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

        Optional.ofNullable(filters.id)
            .ifPresent { criteria.add(Criteria.where("id").`is`(it)) }
        Optional.ofNullable(filters.name)
            .ifPresent { criteria.add(Criteria.where("name").regex(".*$it.*", "i")) }
        Optional.ofNullable(filters.city)
            .ifPresent { criteria.add(Criteria.where("address.city").regex(".*$it.*", "i")) }
        Optional.ofNullable(filters.streetName)
            .ifPresent { criteria.add(Criteria.where("address.streetName").regex(".*$it.*", "i")) }

        return criteria
    }
}
