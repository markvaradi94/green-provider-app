package ro.asis.provider.service.service

import org.slf4j.LoggerFactory
import org.springframework.amqp.core.TopicExchange
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.stereotype.Service
import ro.asis.commons.model.Dashboard
import ro.asis.commons.model.Inventory
import ro.asis.provider.events.ProviderCreationEvent
import ro.asis.provider.events.ProviderDeletionEvent
import ro.asis.provider.events.ProviderEditEvent
import ro.asis.provider.service.model.entity.ProviderEntity
import ro.asis.provider.service.model.mappers.ProviderMapper

@Service
class ProviderNotificationsService(
    private val mapper: ProviderMapper,
    private val rabbitTemplate: RabbitTemplate,
    private val providerExchange: TopicExchange
) {
    companion object {
        private val LOG = LoggerFactory.getLogger(ProviderEntity::class.java)
    }

    fun notifyProviderCreated(provider: ProviderEntity) {
        val event = ProviderCreationEvent(accountId = provider.accountId, providerId = provider.id!!)

        LOG.info("Sending event $event")
        rabbitTemplate.convertAndSend(providerExchange.name, "green.providers.new", event)
    }

    fun notifyProviderDeleted(provider: ProviderEntity) {
        val event = ProviderDeletionEvent(providerId = provider.id!!)

        LOG.info("Sending event $event")
        rabbitTemplate.convertAndSend(providerExchange.name, "green.providers.delete", event)
    }

    fun notifyProviderEdited(provider: ProviderEntity) {
        val event = ProviderEditEvent(
            providerId = provider.id!!,
            editedProvider = mapper.toApi(provider)
        )

        LOG.info("Sending event $event")
        rabbitTemplate.convertAndSend(providerExchange.name, "green.providers.edit", event)
    }
}
