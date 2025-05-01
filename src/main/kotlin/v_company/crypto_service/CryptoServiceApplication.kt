package v_company.crypto_service

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan

@SpringBootApplication
@ComponentScan("v_company")
class CryptoServiceApplication

fun main(args: Array<String>) {
	runApplication<CryptoServiceApplication>(*args)
}
