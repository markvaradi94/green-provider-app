package ro.asis.provider.service

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.netflix.eureka.EnableEurekaClient

@EnableEurekaClient
@SpringBootApplication(
    scanBasePackages = [
        "ro.asis.commons",
        "ro.asis.account.client",
        "ro.asis.order.client",
        "ro.asis.provider"
    ]
)
class ProviderServiceApplication

fun main(args: Array<String>) {
    runApplication<ProviderServiceApplication>(*args)
}
