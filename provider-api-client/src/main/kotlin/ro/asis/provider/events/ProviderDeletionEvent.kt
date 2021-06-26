package ro.asis.provider.events

import com.fasterxml.jackson.annotation.JsonProperty
import ro.asis.commons.enums.EventType
import ro.asis.commons.enums.EventType.DELETED

data class ProviderDeletionEvent(
    @JsonProperty("providerId")
    val providerId: String,

    @JsonProperty("eventType")
    val eventType: EventType = DELETED
)
