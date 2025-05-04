package v_company.crypto_service.services

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import v_company.crypto_service.entity.HashSeedEntity
import v_company.crypto_service.hashing.SeededParams
import v_company.crypto_service.hashing.SeededParamsFactory
import v_company.crypto_service.repository.HashSeedRepository
import kotlin.random.Random

@Service
class HashSeedService(private val hashSeedRepository: HashSeedRepository) {

    fun getParams(seedId: Long? = null, seedName: String? = null): SeededParams {
        val seedEntity = when {
            seedId != null -> hashSeedRepository.findById(seedId).orElse(null)
            seedName != null -> hashSeedRepository.findByName(seedName)
            else -> hashSeedRepository.findFirstByIsActiveTrueOrderByCreatedAtDesc()
        }

        return if (seedEntity != null) {
            SeededParamsFactory.generate(seedEntity.seed)
        } else {
            val newSeed = Random(System.nanoTime()).nextLong()
            saveActiveSeed(newSeed, "auto-generated")
            SeededParamsFactory.generate(newSeed)
        }
    }


    @Transactional
    fun saveActiveSeed(seed: Long, name: String? = null): Long {
        hashSeedRepository.findAll().filter { it.isActive }.forEach {
            hashSeedRepository.save(
                HashSeedEntity(
                    id = it.id,
                    seed = it.seed,
                    name = it.name,
                    createdAt = it.createdAt,
                    isActive = false
                )
            )
        }

        val entity = HashSeedEntity(
            seed = seed,
            name = name,
            isActive = true
        )

        return hashSeedRepository.save(entity).seed
    }

    fun getActiveSeed(): Long? {
        return hashSeedRepository.findFirstByIsActiveTrueOrderByCreatedAtDesc()?.seed
    }
}