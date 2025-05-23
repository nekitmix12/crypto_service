package v_company.crypto_service.services

import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import v_company.crypto_service.dto.CertificateDto
import v_company.crypto_service.dto.UserDto
import v_company.crypto_service.entity.KeyEntity
import v_company.crypto_service.entity.UserEntity
import v_company.crypto_service.repository.KeyRepository
import v_company.crypto_service.repository.UserRepository
import java.math.BigInteger
import java.time.Instant
import java.util.UUID

@Service
class CertificateService(
    private val keyRepository: KeyRepository,
    private val userRepository: UserRepository,
    private val hashSeedService: HashSeedService
) {
    @Transactional
    fun getPrivateKey(userDto: UserDto): BigInteger? {
        val keyId =
            userRepository.findByFirstNameAndLastName(userDto.firstName, userDto.secondName)?.keyId ?: return null
        return keyRepository.findById(keyId).get().privateKey.toBigInteger()
    }

    @Transactional
    fun getKeys(userDto: UserDto) =
        keyRepository.findById(
            userRepository.findByFirstNameAndLastName(userDto.firstName, userDto.secondName)?.keyId!!
        )


    fun createCertificate(userDto: UserDto, keys: Pair<BigInteger, Pair<BigInteger, BigInteger>>): CertificateDto {
        val key = keyRepository.save(
            KeyEntity(
                privateKey = keys.first.toString(),
                publicKeyR = keys.second.first.toString(),
                publicKeyS = keys.second.second.toString(),
                createdAt = Instant.now(),
            )
        )
        val user = userRepository.save(
            UserEntity(
                firstName = userDto.firstName,
                lastName = userDto.secondName,
                createdAt = Instant.now(),
                keyId = key.id,
                seedId = null
            )
        )

        val hashSeed = hashSeedService.generateSeed(user)
        println("hashSeed: $hashSeed")
        println("user: $user")
        userRepository.save(user.copy(seedId = hashSeed.id))
        return CertificateDto(key.publicKeyR.toBigInteger() to key.publicKeyS.toBigInteger(), userDto)
    }
}