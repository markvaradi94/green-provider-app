package ro.asis.provider.service.listeners

import org.slf4j.LoggerFactory
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.stereotype.Component
import ro.asis.account.events.AccountCreationEvent
import ro.asis.account.events.AccountDeletionEvent
import ro.asis.account.events.AccountEditEvent
import ro.asis.commons.enums.AccountType.PROVIDER
import ro.asis.commons.enums.EventType.*
import ro.asis.provider.service.model.entity.ProviderEntity
import ro.asis.provider.service.service.ProviderAccountService
import ro.asis.provider.service.service.ProviderService

@Component
class AccountEventListener(
    private val providerService: ProviderService,
    private val providerAccountService: ProviderAccountService
) {
    companion object {
        private val LOG = LoggerFactory.getLogger(ProviderEntity::class.java)
    }

    @RabbitListener(queues = ["#{newAccountQueue.name}"])
    fun processNewAccountCreation(event: AccountCreationEvent) = callProviderCreationForAccount(event)

    @RabbitListener(queues = ["#{deleteAccountQueue.name}"])
    fun processAccountDeletion(event: AccountDeletionEvent) = callProviderDeletionForAccount(event)

    @RabbitListener(queues = ["#{editAccountQueue.name}"])
    fun processAccountEdit(event: AccountEditEvent) = callProviderEditForAccount(event)

    private fun callProviderCreationForAccount(event: AccountCreationEvent) {
        if (event.accountType == PROVIDER && event.eventType == CREATED) {
            LOG.info("Creating provider for account with id ${event.accountId}")
            providerService.createProviderForNewAccount(event.accountId)
        }
    }

    private fun callProviderDeletionForAccount(event: AccountDeletionEvent) {
        if (event.accountType == PROVIDER && event.eventType == DELETED) {
            LOG.info("Deleting provider for account with id ${event.accountId}")
            providerService.deleteProviderForAccount(event.accountId)
        }
    }

    private fun callProviderEditForAccount(event: AccountEditEvent) {
        if (event.accountType == PROVIDER && event.eventType == MODIFIED) {
            LOG.info("Provider account was edited for account with id ${event.accountId}")
            LOG.info("$event")
            providerAccountService.editForAccountChange(event.accountId, event.editedAccount)
        }
    }
}
