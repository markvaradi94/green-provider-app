package ro.asis.provider.service.service.validator

import org.springframework.stereotype.Component
import ro.asis.account.client.AccountApiClient
import ro.asis.commons.exceptions.ValidationException
import ro.asis.commons.model.Address
import ro.asis.provider.service.model.entity.ProviderEntity
import ro.asis.provider.service.repository.ProviderRepository
import java.time.LocalDate
import java.util.*
import java.util.Optional.empty
import java.util.Optional.of

@Component
class ProviderValidator(
    private val repository: ProviderRepository,
    private val accountApiClient: AccountApiClient
) {

    fun validateReplaceOrThrow(providerId: String, newProvider: ProviderEntity) =
        exists(providerId)
            .or { validate(provider = newProvider, newEntity = false) }
            .ifPresent { throw it }

    fun validateNewOrThrow(provider: ProviderEntity) =
        validate(provider = provider, newEntity = true)
            .ifPresent { throw it }

    fun validateExistsOrThrow(providerId: String) = exists(providerId).ifPresent { throw it }

    private fun validate(provider: ProviderEntity, newEntity: Boolean): Optional<ValidationException> {
        if (newEntity) {
            accountAlreadyIsInUseOrDoesNotExist(provider).ifPresent { throw it }
        }
        accountDoesNotExist(provider).ifPresent { throw it }
        addressIsInvalid(provider.address).ifPresent { throw it }
        sinceDateIsInvalid(provider).ifPresent { throw it }
        descriptionMissing(provider).ifPresent { throw it }
        nameIsInvalid(provider).ifPresent { throw it }
        return empty()
    }

    private fun nameIsInvalid(provider: ProviderEntity): Optional<ValidationException> {
        return if (provider.name.isBlank()) of(ValidationException("Please provide a valid name"))
        else empty()
    }

    private fun descriptionMissing(provider: ProviderEntity): Optional<ValidationException> {
        return if (provider.description.isBlank()) of(ValidationException("Please provide a short description"))
        else empty()
    }

    private fun sinceDateIsInvalid(provider: ProviderEntity): Optional<ValidationException> {
        return if (provider.since > LocalDate.now()) of(ValidationException("Since date must be before the current date"))
        else empty()
    }

    private fun addressIsInvalid(address: Address): Optional<ValidationException> {
        return when (true) {
            address.city?.isBlank() -> of(ValidationException("City must be valid"))
            address.streetName?.isBlank() -> of(ValidationException("Street name must be valid"))
            address.streetNumber?.isBlank() -> of(ValidationException("Street number must be valid"))
            address.zipCode?.isBlank() -> of(ValidationException("Zipcode must be valid"))
            else -> empty()
        }
    }

    private fun accountDoesNotExist(provider: ProviderEntity): Optional<ValidationException> {
        return if (!accountExists(provider.accountId)) of(ValidationException("Client cannot be registered with a non-existent account"))
        else empty()
    }

    private fun accountAlreadyIsInUseOrDoesNotExist(provider: ProviderEntity): Optional<ValidationException> {
        return if (!accountExists(provider.accountId)) of(ValidationException("Provider cannot be registered with a non-existent account"))
        else if (providerWithAccountExists(provider.accountId)) of(ValidationException("There is already a provider registered for this account"))
        else empty()
    }

    private fun providerWithAccountExists(accountId: String): Boolean = repository.existsByAccountId(accountId)

    private fun accountExists(accountId: String) = accountApiClient.getAccount(accountId).isPresent

    private fun exists(providerId: String): Optional<ValidationException> {
        return if (repository.existsById(providerId)) empty()
        else of(ValidationException("Provider with id $providerId doesn't exist"))
    }
}
