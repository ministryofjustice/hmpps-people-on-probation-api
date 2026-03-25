package uk.gov.justice.digital.hmpps.peopleonprobationapi.integration.person

import org.junit.jupiter.api.Test
import uk.gov.justice.digital.hmpps.peopleonprobationapi.integration.IntegrationTestBase

class PersonNameTest : IntegrationTestBase() {
  @Test
  fun `gets a person's name`() {
    stubGrantToken()
    stubPersonName(crn = "X123456", forename = "Alex", middleName = "James", surname = "Smith")

    webTestClient.get()
      .uri("/v1/person/X123456/name")
      .headers(setAuthorisation())
      .exchange()
      .expectStatus().isOk
      .expectBody()
      .jsonPath("forename").isEqualTo("Alex")
      .jsonPath("middleName").isEqualTo("James")
      .jsonPath("surname").isEqualTo("Smith")
  }

  @Test
  fun `returns not found when a person does not exist`() {
    stubGrantToken()
    stubPersonNameNotFound("X000000")

    webTestClient.get()
      .uri("/v1/person/X000000/name")
      .headers(setAuthorisation())
      .exchange()
      .expectStatus().isNotFound
      .expectBody()
      .jsonPath("userMessage").isEqualTo("Person not found for CRN: X000000")
  }

  @Test
  fun `returns unauthorized without a token`() {
    webTestClient.get()
      .uri("/v1/person/X123456/name")
      .exchange()
      .expectStatus().isUnauthorized
  }

  @Test
  fun `returns forbidden when token does not have required role`() {
    webTestClient.get()
      .uri("/v1/person/X123456/name")
      .headers(setAuthorisation(roles = listOf("OTHER_ROLE")))
      .exchange()
      .expectStatus().isForbidden
  }
}
