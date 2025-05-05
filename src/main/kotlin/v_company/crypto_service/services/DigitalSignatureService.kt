package v_company.crypto_service.services

import CalculateService
import org.springframework.stereotype.Service
import v_company.crypto_service.repository.KeyRepository
import v_company.crypto_service.repository.UserRepository
import java.math.BigInteger
import java.util.*

@Service
class DigitalSignatureService(
    private var calculateService: CalculateService,
    private val keyRepository: KeyRepository,
    private val userRepository: UserRepository,
) {


    fun signMessage(
        privateKey: BigInteger,
        message: ByteArray,
    ): Pair<ByteArray, ByteArray> {
        val seedId = userRepository.findByKeyId(
            keyRepository.findByPrivateKey(privateKey.toString()).get().id
        ).seedId!!

        val z = calculateService.hashMessage(message, seedId)
        println("hash2: $z")
        println("hash2: $seedId")
        var r: BigInteger
        var s: BigInteger

        do {
            val k = BigInteger(CalculateService.n.bitLength(), Random()).mod(CalculateService.n)
            val (x, _) = calculateService.scalarMultiply(k, CalculateService.G)!!
            r = x.mod(CalculateService.n)
            s = (z + r * privateKey).multiply(calculateService.inverseMod(k, CalculateService.n))
                .mod(CalculateService.n)
        } while (r == BigInteger.ZERO || s == BigInteger.ZERO)

        return toFixedSize(r, 32) to toFixedSize(s, 32)
    }

    fun toFixedSize(bigInt: BigInteger, size: Int): ByteArray {
        val bytes = bigInt.toByteArray()
        return if (bytes.size > size) bytes.copyOfRange(bytes.size - size, bytes.size)
        else ByteArray(size - bytes.size) { 0 } + bytes
    }

    fun verifySignature(
        publicKey: Pair<BigInteger, BigInteger>,
        message: ByteArray,
        signature: Pair<BigInteger, BigInteger>,
    ): Boolean {
        println(publicKey)
        val seedId = userRepository.findByKeyId(
            keyRepository.findByPublicKeys(publicKey.first.toString(), publicKey.second.toString())[0].id
        ).seedId!!

        val (r, s) = signature
        val z = calculateService.hashMessage(message, seedId)
        println("hash: $z")
        println("hash: $seedId")

        val w = calculateService.inverseMod(s, CalculateService.n)
        val u1 = z.multiply(w).mod(CalculateService.n)
        val u2 = r.multiply(w).mod(CalculateService.n)
        val point = calculateService.pointAdd(
            calculateService.scalarMultiply(u1, CalculateService.G), calculateService.scalarMultiply(u2, publicKey)
        )

        return point?.first?.mod(CalculateService.n) == r
    }

    fun makeKeypair(): Pair<BigInteger, Pair<BigInteger, BigInteger>> {
        val privateKey = BigInteger(CalculateService.n.bitLength(), Random()).mod(CalculateService.n)
        val publicKey = calculateService.scalarMultiply(privateKey, CalculateService.G)!!
        return privateKey to publicKey
    }
}