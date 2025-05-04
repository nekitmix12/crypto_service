package v_company.crypto_service.config

import CalculateService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import v_company.crypto_service.repository.HashSeedRepository
import v_company.crypto_service.services.HashSeedService

@Configuration
class ServiceConfig {

    @Bean
    fun hashSeedService(hashSeedRepository: HashSeedRepository): HashSeedService {
        return HashSeedService(hashSeedRepository)
    }

    @Bean
    fun calculateService(hashSeedService: HashSeedService): CalculateService {
        return CalculateService(hashSeedService)
    }
}