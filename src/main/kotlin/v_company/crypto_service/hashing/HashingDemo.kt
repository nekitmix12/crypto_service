package v_company.crypto_service.hashing

import java.util.Base64

object HashingDemo {
    @JvmStatic
    fun main(args: Array<String>) {
        val params = SeededParamsFactory.random()

        val input = "Пример хэширования".encodeToByteArray()
        val hashValue = HashUtil.hash(input, params)
        println("Входные данные в формате base64: ${Base64.getEncoder().encodeToString(input)}")
        println("Seed: ${params.seed}")
        println("Hash: $hashValue")
        println("IV: ${params.iv.contentToString()}")
        println("SIGMA: ${params.sigma.joinToString { it.contentToString() }}")

        val hashAgain = HashUtil.hash(input, params)
        println("Проверка на то, что они одинаковые: $hashAgain")

        println("Поиск коллизии (используется полный перебор для коротких входных данных)")
        val shortInput = byteArrayOf('A'.code.toByte(), 'B'.code.toByte(), 'C'.code.toByte())
        val shortHash = HashUtil.hash(shortInput, params)
        println("Демо для коротких входных данных: ${Base64.getEncoder().encodeToString(shortInput)} -> $shortHash")

        val collision = HashUtil.findCollisionParallel(shortInput, params, minLen = 1, maxLen = 4)
        if (collision != null) {
            println("Коллизия найдена:")
            println("Коллизия в base64: ${Base64.getEncoder().encodeToString(collision)}")
            println("Хэш коллизии: ${HashUtil.hash(collision, params)}")
        } else {
            println("Коллизия не найдена в заданном диапазоне.")
        }
    }
}