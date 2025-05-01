package v_company.crypto_service.repository

import org.springframework.data.jpa.repository.JpaRepository
import v_company.crypto_service.entity.KeyEntity
import java.util.*

interface KeyRepository : JpaRepository<KeyEntity, UUID> {
    override fun findById(id:UUID): Optional<KeyEntity>
}