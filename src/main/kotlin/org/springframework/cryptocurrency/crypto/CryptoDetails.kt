package org.springframework.cryptocurrency.crypto

data class CryptoDetails (

    var name: String             = "",

    var price: String            = "",

    var high24HourPrice: String  = "",

    var low24HourPrice: String   = "",

    var changePct24Hour : String = ""

)
