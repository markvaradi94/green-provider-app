package ro.asis.provider.service.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.github.fge.jsonpatch.JsonPatch
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import ro.asis.commons.exceptions.ResourceNotFoundException
import ro.asis.commons.filters.ProviderFilters
import ro.asis.commons.model.Address
import ro.asis.commons.model.Dashboard
import ro.asis.commons.model.Inventory
import ro.asis.provider.service.model.entity.ProviderAccountEntity
import ro.asis.provider.service.model.entity.ProviderEntity
import ro.asis.provider.service.repository.ProviderDao
import ro.asis.provider.service.repository.ProviderRepository
import ro.asis.provider.service.service.validator.ProviderValidator
import java.time.LocalDate
import java.util.*

@Service
class ProviderService(
    private val dao: ProviderDao,
    private val mapper: ObjectMapper,
    private val validator: ProviderValidator,
    private val repository: ProviderRepository,
    private val providerAccountService: ProviderAccountService,
    private val notificationsService: ProviderNotificationsService
) {
    companion object {
        private val LOG = LoggerFactory.getLogger(ProviderEntity::class.java)
    }

    fun findAllProviders(filters: ProviderFilters): List<ProviderEntity> = dao.findProviders(filters)

    fun findProvider(providerId: String): Optional<ProviderEntity> = repository.findById(providerId)

    fun findProviderAccount(providerId: String): ProviderAccountEntity {
        val provider = getOrThrow(providerId)
        return providerAccountService.findProviderAccountByProvider(provider)
    }

    fun addProvider(provider: ProviderEntity): ProviderEntity {
        validator.validateNewOrThrow(provider)
        val dbProvider = repository.save(provider)
        notificationsService.notifyProviderCreated(dbProvider)
        return dbProvider
    }

    fun deleteProvider(providerId: String): Optional<ProviderEntity> {
        validator.validateExistsOrThrow(providerId)
        val providerToDelete = repository.findById(providerId)
        providerToDelete.ifPresent { deleteExistingProvider(it) }
        return providerToDelete
    }

    fun patchProvider(providerId: String, patch: JsonPatch): ProviderEntity {
        validator.validateExistsOrThrow(providerId)

        val dbProvider = getOrThrow(providerId)
        val patchedProviderJson = patch.apply(mapper.valueToTree(dbProvider))
        val patchedProvider = mapper.treeToValue(patchedProviderJson, ProviderEntity::class.java)

        validator.validateReplaceOrThrow(providerId, patchedProvider)
        copyProvider(patchedProvider, dbProvider)
        notificationsService.notifyProviderEdited(dbProvider)

        return repository.save(dbProvider)
    }

    fun createProviderForNewAccount(accountId: String): ProviderEntity {
        val newProvider = ProviderEntity(
            accountId = accountId,
            name = "",
            description = "",
            since = LocalDate.now(),
            address = Address(),
            inventory = Inventory(),
            dashboard = Dashboard()
        )

        val dbProvider = repository.save(newProvider)
        notificationsService.notifyProviderCreated(dbProvider)

        return dbProvider
    }

    fun deleteProviderForAccount(accountId: String): ProviderEntity {
        val providerToDelete = repository.findByAccountId(accountId)
        providerToDelete.ifPresent { deleteExistingProvider(it) }
        return providerToDelete
            .orElseThrow { ResourceNotFoundException("Could not find provider for account $accountId") }
    }

    private fun copyProvider(newProvider: ProviderEntity, dbProvider: ProviderEntity) {
        LOG.info("Copying provider: $newProvider")
        dbProvider.address = newProvider.address
        dbProvider.dashboard = newProvider.dashboard
        dbProvider.description = newProvider.description
        dbProvider.name = newProvider.name
        dbProvider.inventory = newProvider.inventory
        dbProvider.since = newProvider.since
    }

    private fun deleteExistingProvider(provider: ProviderEntity) {
        LOG.info("Deleting provider: $provider")
        notificationsService.notifyProviderDeleted(provider)
        repository.delete(provider)
    }

    private fun getOrThrow(providerId: String): ProviderEntity = repository
        .findById(providerId)
        .orElseThrow { ResourceNotFoundException("Could not find provider with id $providerId") }
}

