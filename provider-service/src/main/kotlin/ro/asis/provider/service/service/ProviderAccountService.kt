package ro.asis.provider.service.service

import org.springframework.stereotype.Service
import ro.asis.account.client.AccountApiClient
import ro.asis.account.dto.Account
import ro.asis.commons.exceptions.ResourceNotFoundException
import ro.asis.provider.client.ProviderApiClient
import ro.asis.provider.dto.Provider
import ro.asis.provider.service.model.entity.ProviderAccountEntity
import ro.asis.provider.service.model.entity.ProviderEntity
import ro.asis.provider.service.repository.ProviderAccountRepository

@Service
class ProviderAccountService(
    private val accountApiClient: AccountApiClient,
    private val providerApiClient: ProviderApiClient,
    private val repository: ProviderAccountRepository
) {

    fun createProviderAccount(provider: ProviderEntity): ProviderAccountEntity {
        val account = accountApiClient.getAccount(provider.accountId)
            .orElseThrow { ResourceNotFoundException("Could not find account with id ${provider.accountId}") }
        return repository.save(
            ProviderAccountEntity(
                providerId = provider.id,
                accountId = account.id,
                email = account.email,
                phoneNumber = account.phoneNumber
            )
        )
    }

    fun createProviderAccountForNewProvider(providerId: String, accountId: String): ProviderAccountEntity {
        val account = accountApiClient.getAccount(accountId)
            .orElseThrow { ResourceNotFoundException("Could not find account with id $accountId") }
        val provider = providerApiClient.getProvider(providerId)
            .orElseThrow { ResourceNotFoundException("Could not find provider with id $providerId") }

        val newProviderAccount = ProviderAccountEntity(
            providerId = provider.id,
            accountId = account.id,
            email = account.email,
            phoneNumber = account.phoneNumber
        )

        return repository.save(newProviderAccount)
    }

    fun editForAccountChange(accountId: String, editedAccount: Account): ProviderAccountEntity {
        val providerAccount = findProviderAccountByAccountId(accountId)
        providerAccount.email = editedAccount.email
        providerAccount.phoneNumber = editedAccount.phoneNumber
        return repository.save(providerAccount)
    }

    fun editForProviderChange(providerId: String, editedProvider: Provider): ProviderAccountEntity {
        val providerAccount = findProviderAccountByProviderId(providerId)
        editedProvider.address
        editedProvider.dashboard
        //TODO figure out if we need any provider specific details in provider_account
        return providerAccount
    }

    fun deleteForProvider(providerId: String): ProviderAccountEntity {
        val providerAccountToDelete = findProviderAccountByProviderId(providerId)
        repository.delete(providerAccountToDelete)
        return providerAccountToDelete
    }

    fun findProviderAccountByAccountId(accountId: String): ProviderAccountEntity =
        repository.findByAccountId(accountId)
            .orElseThrow { ResourceNotFoundException("Could not find provider account for provider with id $accountId") }

    fun findProviderAccountByProviderId(providerId: String): ProviderAccountEntity =
        repository.findByProviderId(providerId)
            .orElseThrow { ResourceNotFoundException("Could not find provider account for provider with id $providerId") }

    fun findProviderAccountByProvider(provider: ProviderEntity): ProviderAccountEntity =
        repository.findByProviderId(provider.id)
            .orElseThrow {
                ResourceNotFoundException("Could not find provider account for provider with id ${provider.id}")
            }

    fun findProviderForProviderAccount(providerAccount: ProviderAccountEntity): Provider =
        providerApiClient.getProvider(providerAccount.providerId)
            .orElseThrow { ResourceNotFoundException("Could not find provider with id ${providerAccount.providerId}") }

    fun findAccountForProviderAccount(providerAccount: ProviderAccountEntity): Account =
        accountApiClient.getAccount(providerAccount.accountId)
            .orElseThrow { ResourceNotFoundException("Could not find account with id ${providerAccount.accountId}") }
}
