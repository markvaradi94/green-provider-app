package ro.asis.provider.service.listeners

import org.slf4j.LoggerFactory
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.stereotype.Component
import ro.asis.commons.enums.EventType
import ro.asis.provider.events.ProviderCreatedEvent
import ro.asis.provider.events.ProviderDeletedEvent
import ro.asis.provider.events.ProviderModifiedEvent
import ro.asis.provider.service.model.entity.ProviderEntity
import ro.asis.provider.service.service.ProviderAccountService

@Component
class ProviderEventListener(
    private val providerAccountService: ProviderAccountService
) {
    companion object {
        private val LOG = LoggerFactory.getLogger(ProviderEntity::class.java)
    }

    @RabbitListener(queues = ["#{newProviderQueue.name}"])
    fun processNewProviderCreation(event: ProviderCreatedEvent) = callProviderAccountCreationForProvider(event)

    @RabbitListener(queues = ["#{deleteProviderQueue.name}"])
    fun processProviderDeletion(event: ProviderDeletedEvent) = callProviderAccountDeletionForProvider(event)

    @RabbitListener(queues = ["#{editProviderQueue.name}"])
    fun processProviderEdit(event: ProviderModifiedEvent) = callProviderAccountEditForProvider(event)

    private fun callProviderAccountCreationForProvider(event: ProviderCreatedEvent) {
        if (event.eventType == EventType.CREATE) {
            LOG.info("Creating provider_account for provider with id ${event.providerId}")
            providerAccountService.createProviderAccountForNewProvider(event.providerId, event.accountId)
        }
    }

    private fun callProviderAccountDeletionForProvider(event: ProviderDeletedEvent) {
        if (event.eventType == EventType.DELETE) {
            LOG.info("Deleting provider_account for provider with id ${event.providerId}")
            providerAccountService.deleteForProvider(event.providerId)
        }
    }

    private fun callProviderAccountEditForProvider(event: ProviderModifiedEvent) {
        if (event.eventType == EventType.MODIFY) {
            LOG.info("Provider_account was edited for provider with id ${event.providerId}")
            LOG.info("$event")
            providerAccountService.editForProviderChange(event.providerId, event.editedProvider)
        }
    }
}
