package ro.asis.provider.events

import com.fasterxml.jackson.annotation.JsonProperty
import ro.asis.commons.enums.EventType
import ro.asis.commons.enums.EventType.CREATED

data class ProviderCreationEvent(
    @JsonProperty("providerId")
    val providerId: String,

    @JsonProperty("accountId")
    val accountId: String,

    @JsonProperty("eventType")
    val eventType: EventType = CREATED
)
