package v_company.crypto_service.hashing

data class SeededParams(
    val seed: Long,
    val iv: IntArray,
    val sigma: Array<IntArray>
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is SeededParams) return false
        if (seed != other.seed) return false
        if (!iv.contentEquals(other.iv)) return false
        if (!sigma.contentDeepEquals(other.sigma)) return false
        return true
    }

    override fun hashCode(): Int {
        var result = seed.hashCode()
        result = 31 * result + iv.contentHashCode()
        result = 31 * result + sigma.contentDeepHashCode()
        return result
    }
}