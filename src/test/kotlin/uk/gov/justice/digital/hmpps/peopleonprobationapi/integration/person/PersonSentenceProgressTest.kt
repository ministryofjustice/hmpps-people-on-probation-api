package uk.gov.justice.digital.hmpps.peopleonprobationapi.integration.person

import org.junit.jupiter.api.Test
import uk.gov.justice.digital.hmpps.peopleonprobationapi.integration.IntegrationTestBase

class PersonSentenceProgressTest : IntegrationTestBase() {
  @Test
  fun `gets a person's sentence progress`() {
    stubGrantToken()
    stubSentenceProgress(
      crn = "X123456",
      response =
      """
      {
        "sentences": [
          {
            "type": "Community Order",
            "startDate": "2024-01-01",
            "expectedEndDate": "2025-01-01",
            "requirements": [
              {
                "type": "Unpaid Work",
                "description": "Complete unpaid work",
                "required": 120,
                "completed": 40,
                "unit": "HOURS"
              }
            ],
            "licenceConditions": [
              {
                "type": "Residence",
                "description": "Live at approved address",
                "startDate": "2024-01-01",
                "expectedEndDate": "2024-12-31"
              }
            ]
          }
        ]
      }
      """.trimIndent(),
    )

    webTestClient.get()
      .uri("/v1/person/X123456/sentences")
      .headers(setAuthorisation())
      .exchange()
      .expectStatus().isOk
      .expectBody()
      .jsonPath("sentences[0].type").isEqualTo("Community Order")
      .jsonPath("sentences[0].requirements[0].required").isEqualTo(120)
      .jsonPath("sentences[0].requirements[0].unit").isEqualTo("HOURS")
      .jsonPath("sentences[0].licenceConditions[0].type").isEqualTo("Residence")
  }

  @Test
  fun `returns not found when sentence progress does not exist`() {
    stubGrantToken()
    stubSentenceProgressNotFound("X000000")

    webTestClient.get()
      .uri("/v1/person/X000000/sentences")
      .headers(setAuthorisation())
      .exchange()
      .expectStatus().isNotFound
      .expectBody()
      .jsonPath("userMessage").isEqualTo("Person not found for CRN: X000000")
  }
}
