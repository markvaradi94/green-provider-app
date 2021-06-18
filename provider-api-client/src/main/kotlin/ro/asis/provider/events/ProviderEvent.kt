package ro.asis.provider.events

import ro.asis.commons.enums.EventType
import ro.asis.provider.dto.Provider

data class ProviderEvent(
    private val provider: Provider,
    private val type: EventType
)
