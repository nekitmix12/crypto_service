package v_company.crypto_service.config

import CalculateService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import v_company.crypto_service.repository.HashSeedRepository
import v_company.crypto_service.repository.KeyRepository
import v_company.crypto_service.repository.UserRepository
import v_company.crypto_service.services.CertificateService
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

    @Bean
    fun certificateService(
        keyRepository: KeyRepository,
        userRepository: UserRepository,
        hashSeedService: HashSeedService
    ): CertificateService {
        return CertificateService(keyRepository, userRepository, hashSeedService)
    }
}