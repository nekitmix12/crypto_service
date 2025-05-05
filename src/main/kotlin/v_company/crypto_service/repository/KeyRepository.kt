package v_company.crypto_service.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import v_company.crypto_service.entity.KeyEntity
import java.util.*

interface KeyRepository : JpaRepository<KeyEntity, UUID> {
    override fun findById(id: UUID): Optional<KeyEntity>

    @Query("SELECT k FROM KeyEntity k WHERE k.publicKeyS = :publicKeyS AND k.publicKeyR = :publicKeyR")
    fun findByPublicKeys(
        @Param("publicKeyR") publicKeyR: String,
        @Param("publicKeyS") publicKeyS: String
    ): List<KeyEntity>

    fun findByPrivateKey(privateKey: String): Optional<KeyEntity>
}