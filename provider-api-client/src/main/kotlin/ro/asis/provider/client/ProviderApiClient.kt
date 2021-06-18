package ro.asis.provider.client

import com.github.fge.jsonpatch.JsonPatch
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.HttpEntity
import org.springframework.http.HttpEntity.EMPTY
import org.springframework.http.HttpMethod.*
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.postForObject
import org.springframework.web.util.UriComponentsBuilder
import ro.asis.commons.filters.ProviderFilters
import ro.asis.provider.dto.Provider
import java.util.*
import java.util.Optional.ofNullable

@Component
class ProviderApiClient(
    @Value("\${provider-service-location:NOT_DEFINED}")
    private val baseUrl: String,
    private val restTemplate: RestTemplate
) {
    companion object {
        private val LOG = LoggerFactory.getLogger(Provider::class.java)
    }

    fun getAllProviders(filters: ProviderFilters): List<Provider> {
        val url = buildQueriedUrl(filters)
        return restTemplate.exchange(
            url,
            GET,
            EMPTY,
            object : ParameterizedTypeReference<List<Provider>>() {}
        ).body ?: listOf()
    }

    fun getProvider(providerId: String): Optional<Provider> {
        val url = UriComponentsBuilder.fromHttpUrl(baseUrl)
            .path("/providers/$providerId")
            .toUriString()

        return ofNullable(restTemplate.getForObject(url, Provider::class.java))
    }

    fun addProvider(provider: Provider): Provider {
        val url = UriComponentsBuilder.fromHttpUrl(baseUrl)
            .path("/providers")
            .toUriString()
        return restTemplate.postForObject(url, provider, Provider::class)
    }

    fun patchProvider(providerId: String, patch: JsonPatch): Optional<Provider> {
        val url = UriComponentsBuilder.fromHttpUrl(baseUrl)
            .path("/providers/$providerId")
            .toUriString()
        val patchedProvider = HttpEntity(patch)
        return ofNullable(
            restTemplate.exchange(
                url,
                PATCH,
                patchedProvider,
                Provider::class.java
            ).body
        )
    }

    fun deleteProvider(providerId: String): Optional<Provider> {
        val url = UriComponentsBuilder.fromHttpUrl(baseUrl)
            .path("/providers/$providerId")
            .toUriString()
        return ofNullable(
            restTemplate.exchange(
                url,
                DELETE,
                EMPTY,
                Provider::class.java
            ).body
        )
    }

    private fun buildQueriedUrl(filters: ProviderFilters): String {
        val builder = UriComponentsBuilder.fromHttpUrl(baseUrl)
            .path("/providers/")

        ofNullable(filters.id)
            .ifPresent { builder.queryParam("id", it) }
        ofNullable(filters.city)
            .ifPresent { builder.queryParam("city", it) }
        ofNullable(filters.streetName)
            .ifPresent { builder.queryParam("streetName", it) }
        ofNullable(filters.name)
            .ifPresent { builder.queryParam("name", it) }

        return builder.toUriString()
    }
}
