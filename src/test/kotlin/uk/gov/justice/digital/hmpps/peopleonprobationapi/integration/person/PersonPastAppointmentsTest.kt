package uk.gov.justice.digital.hmpps.peopleonprobationapi.integration.person

import org.junit.jupiter.api.Test
import uk.gov.justice.digital.hmpps.peopleonprobationapi.integration.IntegrationTestBase

class PersonPastAppointmentsTest : IntegrationTestBase() {
  @Test
  fun `gets a person's past appointments`() {
    stubGrantToken()
    stubPastAppointments(
      crn = "X123456",
      page = 0,
      size = 10,
      response =
      """
      {
        "content": [
          {
            "date": "2025-01-20",
            "startTime": "09:00",
            "endTime": "09:30",
            "type": "Office Appointment",
            "description": "Initial review",
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
            "attended": true,
            "complied": true
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
      .uri("/v1/person/X123456/past-appointments?page=0&size=10")
      .headers(setAuthorisation())
      .exchange()
      .expectStatus().isOk
      .expectBody()
      .jsonPath("content[0].type").isEqualTo("Office Appointment")
      .jsonPath("content[0].practitioner.name.surname").isEqualTo("Jones")
      .jsonPath("content[0].attended").isEqualTo(true)
      .jsonPath("page.totalElements").isEqualTo(1)
  }

  @Test
  fun `returns not found when past appointments do not exist`() {
    stubGrantToken()
    stubPastAppointmentsNotFound("X000000", 0, 10)

    webTestClient.get()
      .uri("/v1/person/X000000/past-appointments?page=0&size=10")
      .headers(setAuthorisation())
      .exchange()
      .expectStatus().isNotFound
      .expectBody()
      .jsonPath("userMessage").isEqualTo("Person not found for CRN: X000000")
  }
}
