package ro.asis.provider.events

import com.fasterxml.jackson.annotation.JsonProperty
import ro.asis.commons.enums.EventType
import ro.asis.commons.enums.EventType.MODIFY
import ro.asis.provider.dto.Provider

data class ProviderModifiedEvent(
    @JsonProperty("providerId")
    val providerId: String,

    @JsonProperty("eventType")
    val eventType: EventType = MODIFY,

    @JsonProperty("editedProvider")
    val editedProvider: Provider
)
