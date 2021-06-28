package ro.asis.provider.events

import com.fasterxml.jackson.annotation.JsonProperty
import ro.asis.commons.enums.EventType
import ro.asis.commons.enums.EventType.CREATE

data class ProviderCreatedEvent(
    @JsonProperty("providerId")
    val providerId: String,

    @JsonProperty("accountId")
    val accountId: String,

    @JsonProperty("eventType")
    val eventType: EventType = CREATE
)
