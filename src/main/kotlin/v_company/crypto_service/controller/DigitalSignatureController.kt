package v_company.crypto_service.controller

import org.apache.juli.logging.Log
import org.springframework.core.io.ByteArrayResource
import org.springframework.http.ContentDisposition
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import v_company.crypto_service.dto.UserDto
import v_company.crypto_service.services.CertificateService
import v_company.crypto_service.services.DigitalSignatureService
import java.math.BigInteger
import java.time.Instant
import java.util.*

@Controller
@RequestMapping("api/digital_signature")
class DigitalSignatureController(
    private val certificateService: CertificateService, private val digitalSignatureService: DigitalSignatureService
) {


    @PostMapping("/create")
    fun createCertificate(@RequestBody userDto: UserDto) =
        ResponseEntity.ok().body(certificateService.createCertificate(userDto, digitalSignatureService.makeKeypair()))


    @GetMapping("/confirm", consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    fun confirmCertificate(
        @RequestPart("digitalSignature") digitalSignature: String,
        @RequestPart("file") file: MultipartFile,
        @RequestPart("user") userDto: UserDto
    ): ResponseEntity<Boolean> {
        val bytes = Base64.getDecoder().decode(digitalSignature)
        val rBytes = bytes.sliceArray(0 until 32)
        val sBytes = bytes.sliceArray(32 until 64)
        val keys = certificateService.getKeys(userDto).get()
        return ResponseEntity.ok(
            digitalSignatureService.verifySignature(
                keys.publicKeyR.toBigInteger() to keys.publicKeyS.toBigInteger(),
                file.bytes,
                BigInteger(1, rBytes) to BigInteger(1, sBytes)
            )
        )
    }

    @GetMapping("/sign", consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    fun signDoc(
        @RequestPart("user") userDto: UserDto,
        @RequestPart("file") file: MultipartFile,
    ): ResponseEntity<ByteArrayResource> {
        val privateKey = certificateService.getPrivateKey(userDto) ?: return ResponseEntity.status(404).build()

        val (r, s) = digitalSignatureService.signMessage(privateKey, file.bytes)
        val signatureBytes = r + s
        val signatureBase64 = Base64.getEncoder().encodeToString(signatureBytes)

        val headers = HttpHeaders().apply {
            contentType = MediaType.APPLICATION_PDF
            contentDisposition = ContentDisposition.attachment().build()

            add("X-Signature", signatureBase64)
            add("X-Signature-Algorithm", "ECDSA-secp256k1")
            add("X-Signature-Timestamp", Instant.now().toString())
        }
        return ResponseEntity.ok().headers(headers).body(ByteArrayResource(file.bytes))
    }

}