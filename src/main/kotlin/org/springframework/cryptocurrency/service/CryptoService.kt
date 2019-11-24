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
import java.time.LocalDate
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
        var cryptoUrl= appProperties.cryptoUrl
        val metroAreaUrl = cryptoUrl!!.toHttpUrlOrNull()!!.newBuilder()
                .addQueryParameter("apikey", appProperties.cryptoApiKey)
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

        var cryptoList: ArrayList<CryptoDetails> = ArrayList<CryptoDetails>()
        val jsonObj = Gson().fromJson(jsonAsString, Json4Kotlin_Base::class.java)

        if (jsonObj.data.isNotEmpty()) {
            for (data in jsonObj.data) {
                var cryptoDetails: CryptoDetails = CryptoDetails()
                cryptoDetails.name  = data.coinInfo.name
                cryptoDetails.price = data.raw.gBP.pRICE
                cryptoDetails.location = data.location.city
                cryptoDetails.startDate = LocalDate.parse(data.start.date).format(df).toString()
                cryptoDetails.songkickUrl = data.uri
                cryptoDetails.gigToday = if (todaysDate.equals(data.start.date)) true else false
                if (!data.start.time.isNullOrBlank()) {
                    cryptoDetails.startTime = data.start.time.substring(0, 5)
                }

                cryptoList.add(cryptoDetails)
            }
            var pageNumbers: ArrayList<Int> = ArrayList<Int>()
            for (pageNo in 1 ..cryptoForm.numberOfPages) {
                pageNumbers.add(pageNo)
            }
            cryptoForm.pageNumbers = pageNumbers

            sortedGigList = cryptoList.sortedWith(compareBy({it.startDate}, {it.artist}))
        }

        cryptoForm.cryptoList = cryptoList

        return cryptoForm

    }

}
