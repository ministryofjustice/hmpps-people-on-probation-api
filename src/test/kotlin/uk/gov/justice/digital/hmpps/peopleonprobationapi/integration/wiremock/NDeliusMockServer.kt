package uk.gov.justice.digital.hmpps.peopleonprobationapi.integration.wiremock

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock.aResponse
import com.github.tomakehurst.wiremock.client.WireMock.equalTo
import com.github.tomakehurst.wiremock.client.WireMock.get
import com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo
import com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo
import com.github.tomakehurst.wiremock.http.HttpHeader
import com.github.tomakehurst.wiremock.http.HttpHeaders
import org.junit.jupiter.api.extension.AfterAllCallback
import org.junit.jupiter.api.extension.BeforeAllCallback
import org.junit.jupiter.api.extension.BeforeEachCallback
import org.junit.jupiter.api.extension.ExtensionContext

class NDeliusExtension :
  BeforeAllCallback,
  AfterAllCallback,
  BeforeEachCallback {
  companion object {
    @JvmField
    val ndelius = NDeliusMockServer()
  }

  override fun beforeAll(context: ExtensionContext) {
    ndelius.start()
  }

  override fun beforeEach(context: ExtensionContext) {
    ndelius.resetRequests()
  }

  override fun afterAll(context: ExtensionContext) {
    ndelius.stop()
  }
}

class NDeliusMockServer : WireMockServer(WIREMOCK_PORT) {
  fun stubGetName(crn: String, forename: String, middleName: String?, surname: String) {
    stubFor(
      get(urlEqualTo("/person/$crn/name"))
        .willReturn(
          aResponse()
            .withStatus(200)
            .withHeaders(HttpHeaders(HttpHeader("Content-Type", "application/json")))
            .withBody(
              """
              {
                "forename": "$forename",
                "middleName": ${middleName?.let { "\"$it\"" } ?: "null"},
                "surname": "$surname"
              }
              """.trimIndent(),
            ),
        ),
    )
  }

  fun stubGetNameNotFound(crn: String) {
    stubFor(
      get(urlEqualTo("/person/$crn/name"))
        .willReturn(
          aResponse().withStatus(404),
        ),
    )
  }

  fun stubGetPersonalDetails(crn: String, response: String) {
    stubFor(
      get(urlEqualTo("/person/$crn/personal-details"))
        .willReturn(
          aResponse()
            .withStatus(200)
            .withHeaders(HttpHeaders(HttpHeader("Content-Type", "application/json")))
            .withBody(response),
        ),
    )
  }

  fun stubGetPersonalDetailsNotFound(crn: String) {
    stubFor(
      get(urlEqualTo("/person/$crn/personal-details"))
        .willReturn(
          aResponse().withStatus(404),
        ),
    )
  }

  fun stubGetSentenceProgress(crn: String, response: String) {
    stubFor(
      get(urlEqualTo("/person/$crn/sentences"))
        .willReturn(
          aResponse()
            .withStatus(200)
            .withHeaders(HttpHeaders(HttpHeader("Content-Type", "application/json")))
            .withBody(response),
        ),
    )
  }

  fun stubGetSentenceProgressNotFound(crn: String) {
    stubFor(
      get(urlEqualTo("/person/$crn/sentences"))
        .willReturn(
          aResponse().withStatus(404),
        ),
    )
  }

  fun stubGetPastAppointments(crn: String, page: Int, size: Int, response: String) {
    stubFor(
      get(urlPathEqualTo("/person/$crn/past-appointments"))
        .withQueryParam("page", equalTo(page.toString()))
        .withQueryParam("size", equalTo(size.toString()))
        .willReturn(
          aResponse()
            .withStatus(200)
            .withHeaders(HttpHeaders(HttpHeader("Content-Type", "application/json")))
            .withBody(response),
        ),
    )
  }

  fun stubGetPastAppointmentsNotFound(crn: String, page: Int, size: Int) {
    stubFor(
      get(urlPathEqualTo("/person/$crn/past-appointments"))
        .withQueryParam("page", equalTo(page.toString()))
        .withQueryParam("size", equalTo(size.toString()))
        .willReturn(
          aResponse().withStatus(404),
        ),
    )
  }

  fun stubGetFutureAppointments(crn: String, page: Int, size: Int, response: String) {
    stubFor(
      get(urlPathEqualTo("/person/$crn/future-appointments"))
        .withQueryParam("page", equalTo(page.toString()))
        .withQueryParam("size", equalTo(size.toString()))
        .willReturn(
          aResponse()
            .withStatus(200)
            .withHeaders(HttpHeaders(HttpHeader("Content-Type", "application/json")))
            .withBody(response),
        ),
    )
  }

  fun stubGetFutureAppointmentsNotFound(crn: String, page: Int, size: Int) {
    stubFor(
      get(urlPathEqualTo("/person/$crn/future-appointments"))
        .withQueryParam("page", equalTo(page.toString()))
        .withQueryParam("size", equalTo(size.toString()))
        .willReturn(
          aResponse().withStatus(404),
        ),
    )
  }

  companion object {
    private const val WIREMOCK_PORT = 18091
  }
}
