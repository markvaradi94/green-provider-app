package ro.asis.provider.service.bootstrap

import org.springframework.boot.CommandLineRunner
import org.springframework.stereotype.Component
import ro.asis.provider.service.model.entity.ProviderEntity
import ro.asis.provider.service.service.ProviderService
import java.time.LocalDate

@Component
class DataLoader(private val service: ProviderService) : CommandLineRunner {
    override fun run(vararg args: String?) {
//        service.addProvider(
//            ProviderEntity(
//                accountId = "42069",
//                name = "THC",
//                description = "3good5u",
//                since = LocalDate.now().minusYears(5),
//            )
//        )
//        service.addProvider(
//            ProviderEntity(
//                accountId = "123",
//                name = "Danessa",
//                description = "right next to you",
//                since = LocalDate.now().minusYears(15),
//            )
//        )
//        service.addProvider(
//            ProviderEntity(
//                accountId = "456",
//                name = "Bridge",
//                description = "meh but ok",
//                since = LocalDate.now().minusYears(12),
//            )
//        )
    }
}
