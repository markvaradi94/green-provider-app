package ro.asis.provider.service.listeners

import org.slf4j.LoggerFactory
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.stereotype.Component
import ro.asis.commons.enums.EventType
import ro.asis.provider.events.ProviderCreationEvent
import ro.asis.provider.events.ProviderDeletionEvent
import ro.asis.provider.events.ProviderEditEvent
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
    fun processNewProviderCreation(event: ProviderCreationEvent) = callProviderAccountCreationForProvider(event)

    @RabbitListener(queues = ["#{deleteProviderQueue.name}"])
    fun processProviderDeletion(event: ProviderDeletionEvent) = callProviderAccountDeletionForProvider(event)

    @RabbitListener(queues = ["#{editProviderQueue.name}"])
    fun processProviderEdit(event: ProviderEditEvent) = callProviderAccountEditForProvider(event)

    private fun callProviderAccountCreationForProvider(event: ProviderCreationEvent) {
        if (event.eventType == EventType.CREATED) {
            LOG.info("Creating provider_account for provider with id ${event.providerId}")
            providerAccountService.createProviderAccountForNewProvider(event.providerId, event.accountId)
        }
    }

    private fun callProviderAccountDeletionForProvider(event: ProviderDeletionEvent) {
        if (event.eventType == EventType.DELETED) {
            LOG.info("Deleting provider_account for provider with id ${event.providerId}")
            providerAccountService.deleteForProvider(event.providerId)
        }
    }

    private fun callProviderAccountEditForProvider(event: ProviderEditEvent) {
        if (event.eventType == EventType.MODIFIED) {
            LOG.info("Provider_account was edited for provider with id ${event.providerId}")
            LOG.info("$event")
            providerAccountService.editForProviderChange(event.providerId, event.editedProvider)
        }
    }
}
