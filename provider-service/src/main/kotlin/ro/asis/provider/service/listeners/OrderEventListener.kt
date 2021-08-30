package ro.asis.provider.service.listeners

import org.slf4j.LoggerFactory
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.stereotype.Component
import ro.asis.commons.enums.EventType.*
import ro.asis.order.events.OrderCreatedEvent
import ro.asis.order.events.OrderDeletedEvent
import ro.asis.order.events.OrderModifiedEvent
import ro.asis.provider.service.model.entity.ProviderEntity
import ro.asis.provider.service.service.ProviderService

@Component
class OrderEventListener(
    private val providerService: ProviderService
) {
    companion object {
        private val LOG = LoggerFactory.getLogger(ProviderEntity::class.java)
    }

    @RabbitListener(queues = ["#{newOrderQueue.name}"])
    fun processOrderCreated(event: OrderCreatedEvent) = callAddOrderToProvider(event)

    @RabbitListener(queues = ["#{deleteOrderQueue.name}"])
    fun processOrderDeleted(event: OrderDeletedEvent) = callRemoveOrderFromProvider(event)

    @RabbitListener(queues = ["#{editOrderQueue.name}"])
    fun processOrderEdited(event: OrderModifiedEvent) = callEditOrderForProvider(event)

    private fun callAddOrderToProvider(event: OrderCreatedEvent) {
        if (event.eventType == CREATE) {
            LOG.info("Adding order for provider with id ${event.providerId}")
            providerService.addOrderToProviderDashboard(
                providerId = event.providerId,
                orderId = event.orderId
            )
        }
    }

    private fun callRemoveOrderFromProvider(event: OrderDeletedEvent) {
        if (event.eventType == DELETE) {
            LOG.info("Deleting order for provider with id $event")
            providerService.removeOrderFromProviderDashboard(orderId = event.orderId)
        }
    }

    private fun callEditOrderForProvider(event: OrderModifiedEvent) {
        if (event.eventType == MODIFY) {
            LOG.info("Modifying order for provider with id $event")
            providerService.editOrderFromProviderDashboard(
                providerId = event.editedOrder.providerId,
                editedOrder = event.editedOrder
            )
        }
    }
}
