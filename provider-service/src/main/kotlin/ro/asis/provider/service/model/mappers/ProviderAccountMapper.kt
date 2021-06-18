package ro.asis.provider.service.model.mappers

import org.springframework.stereotype.Component
import ro.asis.commons.utils.ModelMapper
import ro.asis.provider.dto.ProviderAccount
import ro.asis.provider.service.model.entity.ProviderAccountEntity
import ro.asis.provider.service.service.ProviderAccountService

@Component
class ProviderAccountMapper(
    private val service: ProviderAccountService
) : ModelMapper<ProviderAccount, ProviderAccountEntity> {
    override fun toApi(source: ProviderAccountEntity): ProviderAccount {
        val provider = service.findProviderForProviderAccount(source)
        val account = service.findAccountForProviderAccount(source)
        return ProviderAccount(
            id = source.id,
            accountId = source.accountId,
            providerId = source.providerId,
            name = provider.name,
            since = provider.since,
            address = provider.address,
            username = account.username,
            email = account.email,
            phoneNumber = account.phoneNumber,
            type = account.type
        )
    }

    override fun toEntity(source: ProviderAccount): ProviderAccountEntity {
        return ProviderAccountEntity(
            id = source.id,
            providerId = source.providerId,
            accountId = source.accountId,
            username = source.username,
            email = source.email,
            phoneNumber = source.phoneNumber
        )
    }
}
