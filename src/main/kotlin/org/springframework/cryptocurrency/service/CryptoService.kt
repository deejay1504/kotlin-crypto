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
                .addQueryParameter("limit", cryptoForm.coinLimit)
                .addQueryParameter("tsym", cryptoForm.currencyType)
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

        val jsonCurrencyField = StringBuilder()
//        "GBP": {
        jsonCurrencyField.append("\"").append(cryptoForm.currencyType).append("\"").append(":{")
        jsonAsString = jsonAsString.replace(jsonCurrencyField.toString(), "\"CURRENCY\":{")

        var cryptoList: ArrayList<CryptoDetails> = ArrayList()
        val jsonObj = Gson().fromJson(jsonAsString, Json4Kotlin_Base::class.java)

        if (jsonObj.data.isNotEmpty()) {
            for (data in jsonObj.data) {
                var cryptoDetails = CryptoDetails()
                val currencyName = StringBuilder()
                currencyName.append(data.coinInfo.name).append("/").append(cryptoForm.currencyType)
                cryptoDetails.name  = currencyName.toString()

                cryptoDetails = setDetails(cryptoForm.currencyType, data.raw.currency.price.toDoubleOrNull(), data.raw.currency.changePct24Hour,
                        data.raw.currency.high24Hour.toDoubleOrNull(), data.raw.currency.low24Hour.toDoubleOrNull())

//                if ("GBP".equals(cryptoForm.currency)) {
//                    cryptoDetails = setDetails(cryptoForm.currency, data.raw.gdp.price.toDoubleOrNull(), data.raw.gdp.changePct24Hour,
//                            data.raw.gdp.high24Hour.toDoubleOrNull(), data.raw.gdp.low24Hour.toDoubleOrNull())
//                } else if ("USD".equals(cryptoForm.currency)) {
//                    cryptoDetails = setDetails(cryptoForm.currency, data.raw.usd.price.toDoubleOrNull(), data.raw.usd.changePct24Hour,
//                            data.raw.usd.high24Hour.toDoubleOrNull(), data.raw.usd.low24Hour.toDoubleOrNull())
//                } else if ("EUR".equals(cryptoForm.currency)) {
//                    cryptoDetails = setDetails(cryptoForm.currency, data.raw.eur.price.toDoubleOrNull(), data.raw.eur.changePct24Hour,
//                            data.raw.eur.high24Hour.toDoubleOrNull(), data.raw.eur.low24Hour.toDoubleOrNull())
//                }

                cryptoList.add(cryptoDetails)
            }
        }

        cryptoForm.cryptoList = cryptoList
        return cryptoForm
    }

    private fun setDetails(currency: String, price: Double?, pct: Double, high24: Double?, low24: Double?): CryptoDetails {
        val cryptoPrice = StringBuilder()
        val changePct24Hour = StringBuilder()
        var cryptoDetails = CryptoDetails()

        cryptoPrice.append(DECIMAL_FORMAT.format(price)).append(" ").append(currency)
        changePct24Hour.append(DECIMAL_FORMAT.format(pct)).append("%")
        cryptoDetails.price = cryptoPrice.toString()
        cryptoDetails.high24HourPrice = DECIMAL_FORMAT.format(high24)
        cryptoDetails.low24HourPrice  = DECIMAL_FORMAT.format(low24)
        cryptoDetails.changePct24Hour = changePct24Hour.toString()
        cryptoDetails.negativePct     = changePct24Hour.toString().startsWith("-")

        return cryptoDetails
    }
}
