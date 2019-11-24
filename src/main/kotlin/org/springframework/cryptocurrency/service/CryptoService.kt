package org.springframework.cryptocurrency.service

import com.google.gson.Gson
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cryptocurrency.crypto.CryptoDetails
import org.springframework.cryptocurrency.crypto.CryptoDetailsForm
import org.springframework.cryptocurrency.model.gbp.Json4Kotlin_Base
import org.springframework.stereotype.Service
import java.io.IOException
import java.util.*


@Service
class CryptoService {

    @Autowired
    lateinit var appProperties: AppProperties

    // Create this as a singleton
    private val okHttpClient = OkHttpClient()


    fun createCryptoForm(): CryptoDetailsForm {
        return CryptoDetailsForm()
    }

    fun getCryptoDetails(cryptoForm: CryptoDetailsForm): CryptoDetailsForm {
        return getCryptoDetailsForCurrency(cryptoForm)
    }

    private fun getCryptoDetailsForCurrency(cryptoForm: CryptoDetailsForm): CryptoDetailsForm {
        var cryptoUrl= appProperties.cryptoCurrencyUrl
        val metroAreaUrl = cryptoUrl!!.toHttpUrlOrNull()!!.newBuilder()
                .addQueryParameter("apikey", appProperties.cryptoCurrencyApiKey)
                .addQueryParameter("limit", "10")
                .addQueryParameter("tsym", cryptoForm.currency.toUpperCase())
                .build()

        val request = Request.Builder()
                .get()
                .url(metroAreaUrl)
                .build()

        var jsonAsString = ""

        val response = okHttpClient.newCall(request).execute()
        response.use {
            if (!response.isSuccessful) {
                throw IOException("Unexpected code $response")
            }

            jsonAsString = response.body!!.string()
        }

        var cryptoList: ArrayList<CryptoDetails> = ArrayList()
        val jsonObj = Gson().fromJson(jsonAsString, Json4Kotlin_Base::class.java)

        if (jsonObj.data.isNotEmpty()) {
            for (data in jsonObj.data) {
                var cryptoDetails: CryptoDetails = CryptoDetails()
                cryptoDetails.name  = data.coinInfo.name
                cryptoDetails.price = data.raw.gdp.price
                cryptoDetails.high24HourPrice = data.raw.gdp.high24Hour
                cryptoDetails.low24HourPrice = data.raw.gdp.low24Hour
                cryptoDetails.changePct24Hour = "%.2f".format(data.raw.gdp.changePct24Hour)
                cryptoList.add(cryptoDetails)
            }
        }

        cryptoForm.cryptoList = cryptoList
        return cryptoForm
    }
}
