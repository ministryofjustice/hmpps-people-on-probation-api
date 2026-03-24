package uk.gov.justice.digital.hmpps.peopleonprobationapi.integration.person

import org.junit.jupiter.api.Test
import uk.gov.justice.digital.hmpps.peopleonprobationapi.integration.IntegrationTestBase

class PersonFutureAppointmentsTest : IntegrationTestBase() {
  @Test
  fun `gets a person's future appointments`() {
    stubGrantToken()
    stubFutureAppointments(
      crn = "X123456",
      page = 0,
      size = 10,
      response =
      """
      {
        "content": [
          {
            "date": "2026-04-20",
            "startTime": "14:00",
            "endTime": "14:30",
            "type": "Office Appointment",
            "description": "Planned review",
            "practitioner": {
              "name": {
                "forename": "Pat",
                "surname": "Jones"
              }
            },
            "location": {
              "buildingName": "Probation Office",
              "street": "Market Road",
              "town": "Leeds",
              "postcode": "LS2 2BB"
            },
            "attended": false,
            "complied": false
          }
        ],
        "page": {
          "size": 10,
          "number": 0,
          "totalElements": 1,
          "totalPages": 1
        }
      }
      """.trimIndent(),
    )

    webTestClient.get()
      .uri("/v1/person/X123456/future-appointments?page=0&size=10")
      .headers(setAuthorisation())
      .exchange()
      .expectStatus().isOk
      .expectBody()
      .jsonPath("content[0].type").isEqualTo("Office Appointment")
      .jsonPath("content[0].practitioner.name.surname").isEqualTo("Jones")
      .jsonPath("content[0].attended").isEqualTo(false)
      .jsonPath("page.totalElements").isEqualTo(1)
  }

  @Test
  fun `returns not found when future appointments do not exist`() {
    stubGrantToken()
    stubFutureAppointmentsNotFound("X000000", 0, 10)

    webTestClient.get()
      .uri("/v1/person/X000000/future-appointments?page=0&size=10")
      .headers(setAuthorisation())
      .exchange()
      .expectStatus().isNotFound
      .expectBody()
      .jsonPath("userMessage").isEqualTo("Person not found for CRN: X000000")
  }
}
