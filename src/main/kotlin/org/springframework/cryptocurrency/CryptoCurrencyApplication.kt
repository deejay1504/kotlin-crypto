package org.springframework.cryptocurrency

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

/**
 * Crypto Currency Application usinng the Crypto Compare API
 *
 * @author Dee Jay
 */
@SpringBootApplication
class CryptoCurrencyApplication

fun main(args: Array<String>) {
    runApplication<CryptoCurrencyApplication>(*args)
}
