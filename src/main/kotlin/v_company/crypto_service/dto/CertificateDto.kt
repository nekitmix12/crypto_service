package v_company.crypto_service.dto

import java.math.BigInteger


data class CertificateDto(
    val publicKey: Pair<BigInteger, BigInteger>,
    val userData: UserDto
)