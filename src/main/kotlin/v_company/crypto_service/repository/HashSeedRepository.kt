package v_company.crypto_service.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import v_company.crypto_service.entity.HashSeedEntity

@Repository
interface HashSeedRepository : JpaRepository<HashSeedEntity, Long> {
    fun findByName(name: String): HashSeedEntity?
    fun findFirstByIsActiveTrueOrderByCreatedAtDesc(): HashSeedEntity?
}