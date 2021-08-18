package ro.asis.provider.service.controller

import com.github.fge.jsonpatch.JsonPatch
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.*
import ro.asis.commons.exceptions.ResourceNotFoundException
import ro.asis.commons.filters.ProviderFilters
import ro.asis.provider.dto.Provider
import ro.asis.provider.dto.ProviderAccount
import ro.asis.provider.service.model.entity.ProviderEntity
import ro.asis.provider.service.model.mappers.ProviderAccountMapper
import ro.asis.provider.service.model.mappers.ProviderMapper
import ro.asis.provider.service.service.ProviderService
import javax.validation.Valid

@RestController
@RequestMapping("providers")
class ProviderController(
    private val service: ProviderService,
    private val providerMapper: ProviderMapper,
    private val providerAccountMapper: ProviderAccountMapper
) {
    companion object {
        private val LOG = LoggerFactory.getLogger(ProviderEntity::class.java)
    }

    @GetMapping
    fun getAllProviders(filters: ProviderFilters): List<Provider> =
        providerMapper.toApi(service.findAllProviders(filters))

    @GetMapping("{providerId}")
    fun getProvider(@PathVariable providerId: String): Provider = service.findProvider(providerId)
        .map { providerMapper.toApi(it) }
        .orElseThrow { ResourceNotFoundException("Could not find provider with id $providerId") }

    @GetMapping("{providerId}/account")
    fun getProviderAccount(@PathVariable providerId: String): ProviderAccount =
        providerAccountMapper.toApi(service.findProviderAccount(providerId))

    @PostMapping
    fun addProvider(@Valid @RequestBody provider: Provider): Provider =
        providerMapper.toApi(service.addProvider(providerMapper.toEntity(provider)))

    @PatchMapping("{providerId}")
    fun patchProvider(@PathVariable providerId: String, @RequestBody patch: JsonPatch): Provider =
        providerMapper.toApi(service.patchProvider(providerId, patch))

    @DeleteMapping("{providerId}")
    fun deleteProvider(@PathVariable providerId: String): Provider = service.deleteProvider(providerId)
        .map { providerMapper.toApi(it) }
        .orElseGet { null }
}
