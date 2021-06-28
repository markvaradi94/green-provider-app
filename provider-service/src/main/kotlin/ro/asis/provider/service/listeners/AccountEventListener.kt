package ro.asis.provider.service.listeners

import org.slf4j.LoggerFactory
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.stereotype.Component
import ro.asis.account.events.AccountCreatedEvent
import ro.asis.account.events.AccountDeletedEvent
import ro.asis.account.events.AccountModifiedEvent
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
    fun processNewAccountCreation(event: AccountCreatedEvent) = callProviderCreationForAccount(event)

    @RabbitListener(queues = ["#{deleteAccountQueue.name}"])
    fun processAccountDeletion(event: AccountDeletedEvent) = callProviderDeletionForAccount(event)

    @RabbitListener(queues = ["#{editAccountQueue.name}"])
    fun processAccountEdit(event: AccountModifiedEvent) = callProviderEditForAccount(event)

    private fun callProviderCreationForAccount(event: AccountCreatedEvent) {
        if (event.accountType == PROVIDER && event.eventType == CREATE) {
            LOG.info("Creating provider for account with id ${event.accountId}")
            providerService.createProviderForNewAccount(event.accountId)
        }
    }

    private fun callProviderDeletionForAccount(event: AccountDeletedEvent) {
        if (event.accountType == PROVIDER && event.eventType == DELETE) {
            LOG.info("Deleting provider for account with id ${event.accountId}")
            providerService.deleteProviderForAccount(event.accountId)
        }
    }

    private fun callProviderEditForAccount(event: AccountModifiedEvent) {
        if (event.accountType == PROVIDER && event.eventType == MODIFY) {
            LOG.info("Provider account was edited for account with id ${event.accountId}")
            LOG.info("$event")
            providerAccountService.editForAccountChange(event.accountId, event.editedAccount)
        }
    }
}
