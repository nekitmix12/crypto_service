package v_company.crypto_service.services

import CalculateService
import org.springframework.stereotype.Service
import java.math.BigInteger
import java.util.*

@Service
class DigitalSignatureService(private var calculateService: CalculateService) {


    fun signMessage(privateKey: BigInteger, message: ByteArray): Pair<BigInteger, BigInteger> {
        val z = calculateService.hashMessage(message)

        var r: BigInteger
        var s: BigInteger

        do {
            val k = BigInteger(CalculateService.n.bitLength(), Random()).mod(CalculateService.n)
            val (x, _) = calculateService.scalarMultiply(k, CalculateService.G)!!
            r = x.mod(CalculateService.n)
            s = (z + r * privateKey).multiply(calculateService.inverseMod(k, CalculateService.n))
                .mod(CalculateService.n)
        } while (r == BigInteger.ZERO || s == BigInteger.ZERO)

        return r to s
    }

    fun verifySignature(
        publicKey: Pair<BigInteger, BigInteger>, message: ByteArray, signature: Pair<BigInteger, BigInteger>
    ): Boolean {
        val (r, s) = signature
        val z = calculateService.hashMessage(message)
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