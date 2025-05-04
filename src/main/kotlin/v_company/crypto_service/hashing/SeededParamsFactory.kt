package v_company.crypto_service.hashing

import kotlin.random.Random

object SeededParamsFactory {
    fun generate(seed: Long, rounds: Int = 8, blocks: Int = 4): SeededParams {
        val random = Random(seed)
        val iv = IntArray(blocks) { random.nextInt() }
        val sigma = Array(rounds) {
            val perm = (0 until blocks).toMutableList()
            perm.shuffle(random)
            perm.toIntArray()
        }
        return SeededParams(seed, iv, sigma)
    }

    fun random(rounds: Int = 8, blocks: Int = 4): SeededParams =
        generate(Random(System.nanoTime()).nextLong(), rounds, blocks)
}