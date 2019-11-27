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
        jsonCurrencyField.append("\"").append(cryptoForm.currencyType).append("\"").append(":{")
        jsonAsString = jsonAsString.replace(jsonCurrencyField.toString(), "\"CURRENCY\":{")

        var cryptoList: ArrayList<CryptoDetails> = ArrayList()
        val jsonObj = Gson().fromJson(jsonAsString, Json4Kotlin_Base::class.java)

        if (jsonObj.data.isNotEmpty()) {
            for (data in jsonObj.data) {
                val currencyName = StringBuilder()
                currencyName.append(data.coinInfo.name).append("/").append(cryptoForm.currencyType)

                val cryptoDetails = setDetails(cryptoForm.currencyType, data.raw.currency.price.toDoubleOrNull(), data.raw.currency.changePct24Hour,
                        data.raw.currency.high24Hour.toDoubleOrNull(), data.raw.currency.low24Hour.toDoubleOrNull())

                cryptoDetails.name  = currencyName.toString()

                cryptoList.add(cryptoDetails)
            }
        }

        if (cryptoForm.rightButton) {
            cryptoForm.cryptoList = cryptoList.sortedWith(compareByDescending({ it.actualPrice }))
        } else {
            cryptoForm.cryptoList = cryptoList.sortedWith(compareBy({ it.actualPrice }))
        }

        return cryptoForm
    }

    private fun setDetails(currency: String, price: Double?, pct: Double, high24: Double?, low24: Double?): CryptoDetails {
        val cryptoPrice = StringBuilder()
        val changePct24Hour = StringBuilder()
        var cryptoDetails = CryptoDetails()

        cryptoPrice.append(DECIMAL_FORMAT.format(price)).append(" ").append(currency)
        changePct24Hour.append(DECIMAL_FORMAT.format(pct)).append("%")
        cryptoDetails.actualPrice     = price!!
        cryptoDetails.price           = cryptoPrice.toString()
        cryptoDetails.high24HourPrice = DECIMAL_FORMAT.format(high24)
        cryptoDetails.low24HourPrice  = DECIMAL_FORMAT.format(low24)
        cryptoDetails.changePct24Hour = changePct24Hour.toString()
        cryptoDetails.negativePct     = changePct24Hour.toString().startsWith("-")

        return cryptoDetails
    }
}
