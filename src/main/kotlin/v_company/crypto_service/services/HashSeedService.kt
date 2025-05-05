package v_company.crypto_service.services

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import v_company.crypto_service.dto.UserDto
import v_company.crypto_service.entity.HashSeedEntity
import v_company.crypto_service.entity.UserEntity
import v_company.crypto_service.hashing.SeededParams
import v_company.crypto_service.hashing.SeededParamsFactory
import v_company.crypto_service.repository.HashSeedRepository
import java.util.*
import kotlin.random.Random

@Service
class HashSeedService(private val hashSeedRepository: HashSeedRepository) {

    fun getParams(seedId: UUID): SeededParams {
        println("seedId: $seedId")
        val seedEntity = hashSeedRepository.findById(seedId).get()
        return SeededParamsFactory.generate(seedEntity.seed)
    }

    fun generateSeed(user: UserEntity): HashSeedEntity {
        val entity = HashSeedEntity(
            id = UUID.randomUUID(),
            seed = Random(System.nanoTime()).nextLong(),
        )
        return hashSeedRepository.save(entity)
    }

    fun getSeed(id: UUID): Long? {
        return hashSeedRepository.findFirstById(id)?.seed
    }
}