package v_company.crypto_service.common

data class DigitalSignature(val signature: ByteArray, val seed: Long) {
    override fun equals(other: Any?): Boolean =
        other is DigitalSignature &&
                seed == other.seed &&
                signature contentEquals other.signature

    override fun hashCode(): Int =
        31 * seed.hashCode() + signature.contentHashCode()
}
