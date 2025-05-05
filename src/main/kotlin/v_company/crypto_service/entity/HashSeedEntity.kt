package v_company.crypto_service.entity

import jakarta.persistence.*
import java.time.Instant
import java.util.UUID

@Entity
@Table(name = "hash_seeds")
data class HashSeedEntity(
    @Id
    @Column
    val id: UUID,

    @Column(nullable = false, unique = true)
    val seed: Long,

)