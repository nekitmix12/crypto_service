package v_company.crypto_service.repository

import org.springframework.data.jpa.repository.JpaRepository
import v_company.crypto_service.entity.UserEntity
import java.util.UUID

interface UserRepository : JpaRepository<UserEntity, UUID> {

    fun findByFirstNameAndLastName(firstName: String, lastName: String): UserEntity?
}