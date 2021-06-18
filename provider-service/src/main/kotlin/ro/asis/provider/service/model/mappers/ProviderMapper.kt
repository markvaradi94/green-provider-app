package ro.asis.provider.service.model.mappers

import org.springframework.stereotype.Component
import ro.asis.commons.utils.ModelMapper
import ro.asis.provider.dto.Provider
import ro.asis.provider.service.model.entity.ProviderEntity

@Component
class ProviderMapper : ModelMapper<Provider, ProviderEntity> {
    override fun toApi(source: ProviderEntity): Provider {
        return Provider(
            id = source.id,
            accountId = source.accountId,
            name = source.name,
            description = source.description,
            since = source.since,
            inventory = source.inventory,
            address = source.address,
            dashboard = source.dashboard
        )
    }

    override fun toEntity(source: Provider): ProviderEntity {
        return ProviderEntity(
            id = source.id,
            accountId = source.accountId,
            name = source.name,
            description = source.description,
            since = source.since,
            inventory = source.inventory,
            address = source.address,
            dashboard = source.dashboard
        )
    }
}
