package v_company.crypto_service.entity

import jakarta.persistence.*
import java.time.Instant

@Entity
@Table(name = "hash_seeds")
data class HashSeedEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(nullable = false, unique = true)
    val seed: Long,

    @Column(nullable = true, unique = true)
    val name: String? = null,

    @Column(nullable = false)
    val createdAt: Instant = Instant.now(),

    @Column(nullable = true)
    val description: String? = null,

    @Column(nullable = false)
    var isActive: Boolean = true
)