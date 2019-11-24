package org.springframework.cryptocurrency.service

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
class AppProperties {

    @Value("\${cryptocurrency.api.key}")
    lateinit var cryptoApiKey: String

    @Value("\${cryptocurrency.url}")
    var cryptoUrl: String? = null

}
