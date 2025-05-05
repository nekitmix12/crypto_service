package v_company.crypto_service.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.OneToOne
import jakarta.persistence.Table
import java.time.Instant
import java.util.*

@Entity
@Table(name = "users")
data class UserEntity(
    @Id
    @Column(columnDefinition = "UUID")
    val id: UUID = UUID.randomUUID(),

    @Column(name = "first_name")
    val firstName: String,

    @Column(name = "last_name")
    val lastName: String,

    @Column(name = "created_at")
    val createdAt: Instant,

    @Column(name = "key_id")
    val keyId: UUID? = null,

    @Column(name = "seed_id")
    val seedId: UUID? = null
)