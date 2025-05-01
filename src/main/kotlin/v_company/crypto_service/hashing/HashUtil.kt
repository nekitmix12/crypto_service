package v_company.crypto_service.hashing

import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicReference

object HashUtil {

    fun hash(
        input: ByteArray,
        params: SeededParams,
        rounds: Int = params.sigma.size
    ): Int {
        val blocks = params.iv.size
        val padded = if (input.size >= blocks * 4) input
        else input + ByteArray(blocks * 4 - input.size) { 0 }
        val m = IntArray(blocks) { i ->
            val idx = i * 4
            (padded[idx].toInt() and 0xFF) or
                    ((padded[idx + 1].toInt() and 0xFF) shl 8) or
                    ((padded[idx + 2].toInt() and 0xFF) shl 16) or
                    ((padded[idx + 3].toInt() and 0xFF) shl 24)
        }
        val v = params.iv.copyOf()
        repeat(rounds) { r ->
            val s = params.sigma[r % params.sigma.size]
            v[0] = v[0] + m[s[0]]
            v[1] = v[1] xor v[0]
            v[2] = v[2] + m[s[1]]
            v[3] = v[3] xor v[2]
            v[0] = Integer.rotateLeft(v[0], 7)
            v[1] = Integer.rotateLeft(v[1], 13)
            v[2] = Integer.rotateLeft(v[2], 17)
            v[3] = Integer.rotateLeft(v[3], 19)
            v[0] = v[0] xor v[3]
            v[1] = v[1] + m[s[2]]
            v[2] = v[2] xor v[1]
            v[3] = v[3] + m[s[3]]
        }
        return v.reduce { acc, i -> acc xor i }
    }

    fun findCollisionParallel(
        targetInput: ByteArray,
        params: SeededParams,
        minLen: Int = 1,
        maxLen: Int = 4,
        threads: Int = Runtime.getRuntime().availableProcessors()
    ): ByteArray? {
        val targetHash = hash(targetInput, params)
        val found = AtomicReference<ByteArray?>()
        val isDone = AtomicBoolean(false)

        for (len in minLen..maxLen) {
            if (isDone.get()) break

            val workers = (0 until threads).map { threadIdx ->
                Thread {
                    val chunkSize = 256 / threads
                    val start = chunkSize * threadIdx
                    val end = if (threadIdx == threads - 1) 256 else chunkSize * (threadIdx + 1)
                    val candidate = ByteArray(len)
                    fun searchRecursive(pos: Int): Boolean {
                        if (isDone.get()) return false
                        if (pos == len) {
                            if (!(candidate contentEquals targetInput) && hash(candidate, params) == targetHash) {
                                found.compareAndSet(null, candidate.copyOf())
                                isDone.set(true)
                                return true
                            }
                            return false
                        }
                        val range = if (pos == 0) start until end else 0..255
                        for (b in range) {
                            candidate[pos] = b.toByte()
                            if (searchRecursive(pos + 1)) return true
                        }
                        return false
                    }
                    searchRecursive(0)
                }
            }
            workers.forEach { it.start() }
            workers.forEach { it.join() }
            if (found.get() != null) break
        }
        return found.get()
    }
}