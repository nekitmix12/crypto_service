package v_company.crypto_service.config

import CalculateService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import v_company.crypto_service.services.CertificateService

@Configuration
class ServiceConfig {
    @Bean
    fun calculateService(): CalculateService = CalculateService()



}