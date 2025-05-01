package v_company.crypto_service.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.math.BigInteger
import java.security.PublicKey
import java.time.Instant
import java.util.*

@Entity
@Table(name = "keys")
data class KeyEntity(
    @Id
    @Column val id: UUID  = UUID.randomUUID(),
    @Column val privateKey: String,
    @Column val publicKeyS: String,
    @Column val publicKeyR: String,
    @Column(name = "created_at")
    val createdAt: Instant
)
