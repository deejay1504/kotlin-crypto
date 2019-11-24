package org.springframework.cryptocurrency

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

/**
 * Gig Finder Application usinng the Songkick API
 *
 * @author Dee Jay
 */
@SpringBootApplication
class GigFinderApplication

fun main(args: Array<String>) {
    runApplication<GigFinderApplication>(*args)
}
