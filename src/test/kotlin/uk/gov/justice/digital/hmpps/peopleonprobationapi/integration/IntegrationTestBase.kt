package uk.gov.justice.digital.hmpps.peopleonprobationapi.integration

import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT
import org.springframework.boot.webtestclient.autoconfigure.AutoConfigureWebTestClient
import org.springframework.http.HttpHeaders
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.reactive.server.WebTestClient
import uk.gov.justice.digital.hmpps.peopleonprobationapi.integration.wiremock.HmppsAuthApiExtension
import uk.gov.justice.digital.hmpps.peopleonprobationapi.integration.wiremock.HmppsAuthApiExtension.Companion.hmppsAuth
import uk.gov.justice.digital.hmpps.peopleonprobationapi.integration.wiremock.NDeliusExtension
import uk.gov.justice.digital.hmpps.peopleonprobationapi.integration.wiremock.NDeliusExtension.Companion.ndelius
import uk.gov.justice.hmpps.test.kotlin.auth.JwtAuthorisationHelper

@ExtendWith(HmppsAuthApiExtension::class, NDeliusExtension::class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
@ActiveProfiles("test")
@AutoConfigureWebTestClient
abstract class IntegrationTestBase {

  @Autowired
  protected lateinit var webTestClient: WebTestClient

  @Autowired
  protected lateinit var jwtAuthHelper: JwtAuthorisationHelper

  internal fun setAuthorisation(
    username: String? = "AUTH_ADM",
    roles: List<String> = listOf(),
    scopes: List<String> = listOf("read"),
  ): (HttpHeaders) -> Unit = jwtAuthHelper.setAuthorisationHeader(username = username, scope = scopes, roles = roles)

  protected fun stubPingWithResponse(status: Int) {
    hmppsAuth.stubHealthPing(status)
  }

  protected fun stubGrantToken() {
    hmppsAuth.stubGrantToken()
  }

  protected fun stubPersonName(crn: String, forename: String, middleName: String? = null, surname: String) {
    ndelius.stubGetName(crn, forename, middleName, surname)
  }

  protected fun stubPersonNameNotFound(crn: String) {
    ndelius.stubGetNameNotFound(crn)
  }

  protected fun stubPersonalDetails(
    crn: String,
    response: String,
  ) {
    ndelius.stubGetPersonalDetails(crn, response)
  }

  protected fun stubPersonalDetailsNotFound(crn: String) {
    ndelius.stubGetPersonalDetailsNotFound(crn)
  }

  protected fun stubSentenceProgress(
    crn: String,
    response: String,
  ) {
    ndelius.stubGetSentenceProgress(crn, response)
  }

  protected fun stubSentenceProgressNotFound(crn: String) {
    ndelius.stubGetSentenceProgressNotFound(crn)
  }

  protected fun stubPastAppointments(
    crn: String,
    page: Int,
    size: Int,
    response: String,
  ) {
    ndelius.stubGetPastAppointments(crn, page, size, response)
  }

  protected fun stubPastAppointmentsNotFound(crn: String, page: Int, size: Int) {
    ndelius.stubGetPastAppointmentsNotFound(crn, page, size)
  }

  protected fun stubFutureAppointments(
    crn: String,
    page: Int,
    size: Int,
    response: String,
  ) {
    ndelius.stubGetFutureAppointments(crn, page, size, response)
  }

  protected fun stubFutureAppointmentsNotFound(crn: String, page: Int, size: Int) {
    ndelius.stubGetFutureAppointmentsNotFound(crn, page, size)
  }
}
