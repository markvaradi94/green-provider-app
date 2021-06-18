package ro.asis.provider.service.repository

import org.springframework.data.mongodb.repository.MongoRepository
import ro.asis.provider.service.model.entity.ProviderEntity

interface ProviderRepository : MongoRepository<ProviderEntity, String>
