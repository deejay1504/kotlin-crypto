package org.springframework.cryptocurrency.system

import org.springframework.cryptocurrency.crypto.CryptoDetailsForm
import org.springframework.cryptocurrency.service.CryptoService
import org.springframework.stereotype.Controller
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import javax.validation.Valid

@Controller
class HomeController(private val cryptoService: CryptoService) {

    val VIEWS_GIG_DETAILS_FORM = "cryptocurrency/cryptoForm"

    @GetMapping("/")
    fun welcome(model: MutableMap<String, Any>): String {

        model["cryptoForm"] = cryptoService.createCryptoForm()

        return VIEWS_GIG_DETAILS_FORM
    }

    @PostMapping("/cryptocurrency")
    fun getGigs(@Valid cryptoForm: CryptoDetailsForm, result: BindingResult, model: MutableMap<String, Any>): String {

        model["cryptoForm"] = cryptoService.getCryptoDetails(cryptoForm)

        model["noRecordsFound"] = (cryptoForm.totalEntries == 0)

        return VIEWS_GIG_DETAILS_FORM
    }

}
