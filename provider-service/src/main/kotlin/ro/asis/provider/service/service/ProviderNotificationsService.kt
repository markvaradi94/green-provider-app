package ro.asis.provider.service.service

import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.stereotype.Service
import ro.asis.provider.service.model.mappers.ProviderMapper

@Service
class ProviderNotificationsService(
    private val mapper: ProviderMapper,
    private val rabbitTemplate: RabbitTemplate
) {
}
