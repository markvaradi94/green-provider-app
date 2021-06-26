package ro.asis.provider.events

import com.fasterxml.jackson.annotation.JsonProperty
import ro.asis.commons.enums.EventType
import ro.asis.commons.enums.EventType.MODIFIED
import ro.asis.provider.dto.Provider

data class ProviderEditEvent(
    @JsonProperty("providerId")
    val providerId: String,

    @JsonProperty("eventType")
    val eventType: EventType = MODIFIED,

    @JsonProperty("editedProvider")
    val editedProvider: Provider
)
