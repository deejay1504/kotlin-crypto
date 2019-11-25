package org.springframework.cryptocurrency.system


import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentMatchers
import org.mockito.BDDMockito.any
import org.mockito.BDDMockito.given
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.cryptocurrency.crypto.CryptoDetailsForm
import org.springframework.cryptocurrency.service.CryptoService
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

@ExtendWith(SpringExtension::class)
@WebMvcTest(HomeController::class)
class HomeControllerTest {

    @Autowired
    lateinit var mockMvc: MockMvc

    @MockBean
    private lateinit var cryptoService: CryptoService

    private lateinit var cryptoForm: CryptoDetailsForm

    val VIEWS_GIG_DETAILS_FORM = "cryptocurrency/cryptoForm"

    @BeforeEach
    fun setup() {
        cryptoForm = CryptoDetailsForm()
        given(cryptoService.createCryptoForm()).willReturn(cryptoForm)
        given(cryptoService.getCryptoDetails(cryptoForm)).willReturn(cryptoForm)
    }

    @Test
    fun shouldCreateCryptoDetailsForm() {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk)
                .andExpect(model().attributeExists("cryptoForm"))
                .andExpect(view().name(VIEWS_GIG_DETAILS_FORM))
    }

    /**
    @Test
    fun shouldReturnGigListInForm() {

        given(gigService.getGigs(cryptoForm)).willReturn(cryptoForm)

        mockMvc.perform(post("/cryptocurrency")
                        .param("totalEntries", "1")
                        .param("gigLocation", "Brighton")
                        .param("metroAreaId", "24555")
                        .param("gigStartDate", "02-11-2019")
                        .param("gigEndDate", "02-11-2019")
                        .param("currentPage", "02-11-2019")
                        .param("resultsPerPage", "20")
                        .param("numberOfPages", "0")
//                        .param("gigList", "")
//                        .param("pageNumbers", "")
                )
                .andExpect(status().isOk)
                .andExpect(model().attributeExists("cryptoForm"))
                .andExpect(model().attribute("cryptoForm", Matchers.hasProperty<Any>("gigStartDate", Matchers.`is`("Franklin"))))
                .andExpect(model().attribute("cryptoForm", Matchers.hasProperty<Any>("gigEndDate", Matchers.`is`("Franklin"))))
                .andExpect(view().name(VIEWS_GIG_DETAILS_FORM))
    }
*/

}
