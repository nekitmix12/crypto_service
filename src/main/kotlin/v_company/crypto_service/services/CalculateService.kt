import org.springframework.stereotype.Service
import v_company.crypto_service.hashing.HashUtil
import v_company.crypto_service.services.HashSeedService
import java.math.BigInteger
import java.nio.ByteBuffer
import java.util.UUID

@Service
class CalculateService(private val hashSeedService: HashSeedService) {


    fun inverseMod(k: BigInteger, p: BigInteger): BigInteger = k.modInverse(p)


    fun isOnCurve(point: Pair<BigInteger, BigInteger>): Boolean {
        val (x, y) = point
        val left = y.modPow(BigInteger.TWO, P)
        val right = (x.modPow(BigInteger.valueOf(3), P).add(a) * x).add(b).mod(P)

        return left == right
    }

    fun scalarMultiply(k: BigInteger, point: Pair<BigInteger, BigInteger>?): Pair<BigInteger, BigInteger>? {
        var result: Pair<BigInteger, BigInteger>? = null
        var addend = point
        var scalar = k

        while (scalar > BigInteger.ZERO) {
            if (scalar.and(BigInteger.ONE) == BigInteger.ONE) {
                result = pointAdd(result, addend)
            }

            addend = pointAdd(addend, addend)
            scalar = scalar.shiftRight(1)
        }

        return result
    }

    fun pointAdd(p1: Pair<BigInteger, BigInteger>?, p2: Pair<BigInteger, BigInteger>?): Pair<BigInteger, BigInteger>? {
        if (p1 == null) return p2
        if (p2 == null) return p1

        val (x1, y1) = p1
        val (x2, y2) = p2

        val m = when {
            x1 == x2 && y1 != y2 -> return null
            x1 == x2 -> {
                val numerator = BigInteger.valueOf(3).multiply(x1.pow(2)).add(a)
                val denominator = BigInteger.TWO.multiply(y1)
                numerator.multiply(inverseMod(denominator, P)).mod(P)
            }

            else -> {
                val numerator = y2.subtract(y1)
                val denominator = x2.subtract(x1)
                numerator.multiply(inverseMod(denominator, P)).mod(P)
            }
        }

        val x3 = (m.pow(2) - x1 - x2).mod(P)
        val y3 = (m.multiply(x1 - x3) - y1).mod(P)

        return Pair(x3, y3)
    }


    fun hashMessage(message: ByteArray, seedId: UUID): BigInteger {
        val params = hashSeedService.getParams(seedId)

        val hashInt = HashUtil.hash(message, params)
        val hash = ByteBuffer.allocate(Int.SIZE_BYTES).putInt(hashInt).array()
        val e = BigInteger(1, hash)
        return e.shiftRight(e.bitLength() - n.bitLength())
    }

    companion object {
        const val CURVE_NAME = "secp256k1"

        val P: BigInteger = BigInteger("FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFEFFFFFC2F", 16)
        val a = BigInteger.ZERO
        val b = BigInteger.valueOf(7)

        val G: Pair<BigInteger, BigInteger> = Pair(
            BigInteger("79BE667EF9DCBBAC55A06295CE870B07029BFCDB2DCE28D959F2815B16F81798", 16),
            BigInteger("483ADA7726A3C4655DA4FBFC0E1108A8FD17B448A68554199C47D08FFB10D4B8", 16)
        )

        val n: BigInteger = BigInteger("FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFEBAAEDCE6AF48A03BBFD25E8CD0364141", 16)
        const val h = 1
    }
}