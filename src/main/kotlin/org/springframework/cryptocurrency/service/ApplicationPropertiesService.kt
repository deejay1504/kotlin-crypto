package org.springframework.cryptocurrency.service

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
class AppProperties {

    @Value("\${crypto.currency.api.key}")
    lateinit var cryptoCurrencyApiKey: String

    @Value("\${crypto.currency.url}")
    var cryptoCurrencyUrl: String? = null

}
