package org.springframework.cryptocurrency.service



import com.google.gson.Gson
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cryptocurrency.crypto.CryptoDetails
import org.springframework.cryptocurrency.crypto.CryptoDetailsForm
import org.springframework.cryptocurrency.model.gbp.Json4Kotlin_Base
import org.springframework.cryptocurrency.model.gbp.Data
import org.springframework.stereotype.Service
import java.io.IOException
import java.util.*

const val DECIMAL_FORMAT = "%.2f"

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
                val currencyName = StringBuilder()
                currencyName.append(data.coinInfo.name).append("/").append(cryptoForm.currency)
                cryptoDetails.name  = currencyName.toString()
                cryptoDetails = setCryptoDetails(data, cryptoForm.currency, cryptoDetails)

                cryptoList.add(cryptoDetails)
            }
        }

        cryptoForm.cryptoList = cryptoList
        return cryptoForm
    }

    private fun setCryptoDetails(data: Data, currency: String, cryptoDetails: CryptoDetails): CryptoDetails {
        val cryptoPrice = StringBuilder()
        val changePct24Hour = StringBuilder()

        if ("GBP".equals(currency)) {
            cryptoPrice.append(DECIMAL_FORMAT.format(data.raw.gdp.price.toDoubleOrNull())).append(" ").append(currency)
            changePct24Hour.append(DECIMAL_FORMAT.format(data.raw.gdp.changePct24Hour)).append("%")
            cryptoDetails.price = cryptoPrice.toString()
            cryptoDetails.high24HourPrice = DECIMAL_FORMAT.format(data.raw.gdp.high24Hour.toDoubleOrNull())
            cryptoDetails.low24HourPrice  = DECIMAL_FORMAT.format(data.raw.gdp.low24Hour.toDoubleOrNull())
            cryptoDetails.changePct24Hour = changePct24Hour.toString()
            cryptoDetails.negativePct     = changePct24Hour.toString().startsWith("-")
        } else if ("USD".equals(currency)) {
            cryptoPrice.append(DECIMAL_FORMAT.format(data.raw.usd.price.toDoubleOrNull())).append(" ").append(currency)
            changePct24Hour.append(DECIMAL_FORMAT.format(data.raw.usd.changePct24Hour)).append("%")
            cryptoDetails.price = cryptoPrice.toString()
            cryptoDetails.high24HourPrice = DECIMAL_FORMAT.format(data.raw.usd.high24Hour.toDoubleOrNull())
            cryptoDetails.low24HourPrice  = DECIMAL_FORMAT.format(data.raw.usd.low24Hour.toDoubleOrNull())
            cryptoDetails.changePct24Hour = changePct24Hour.toString()
            cryptoDetails.negativePct     = changePct24Hour.toString().startsWith("-")
        }
        return cryptoDetails
    }
}
