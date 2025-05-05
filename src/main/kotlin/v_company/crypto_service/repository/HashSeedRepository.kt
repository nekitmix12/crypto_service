package v_company.crypto_service.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import v_company.crypto_service.entity.HashSeedEntity
import java.util.UUID

@Repository
interface HashSeedRepository : JpaRepository<HashSeedEntity, UUID> {
    fun findFirstById(id: UUID): HashSeedEntity?
}