package uk.gov.justice.digital.hmpps.peopleonprobationapi.integration.person

import org.junit.jupiter.api.Test
import uk.gov.justice.digital.hmpps.peopleonprobationapi.integration.IntegrationTestBase

class PersonPersonalDetailsTest : IntegrationTestBase() {
  @Test
  fun `gets a person's personal details`() {
    stubGrantToken()
    stubPersonalDetails(
      crn = "X123456",
      response =
      """
      {
        "name": {
          "forename": "Alex",
          "middleName": "James",
          "surname": "Smith"
        },
        "preferredName": "AJ",
        "dateOfBirth": "1985-02-01",
        "mainAddress": {
          "houseNumber": "10",
          "street": "High Street",
          "town": "Leeds",
          "postcode": "LS1 1AA"
        },
        "telephoneNumber": "01131234567",
        "mobileNumber": "07700111222",
        "emailAddress": "alex.smith@example.com",
        "emergencyContacts": [
          {
            "name": {
              "forename": "Jamie",
              "surname": "Smith"
            },
            "relationship": "Sibling",
            "mobileNumber": "07700999888",
            "emailAddress": "jamie.smith@example.com"
          }
        ],
        "practitioner": {
          "name": {
            "forename": "Pat",
            "surname": "Jones"
          },
          "telephoneNumber": "01130000000",
          "team": {
            "officeAddresses": [
              {
                "buildingName": "Probation Office",
                "street": "Market Road",
                "town": "Leeds",
                "postcode": "LS2 2BB"
              }
            ]
          }
        }
      }
      """.trimIndent(),
    )

    webTestClient.get()
      .uri("/v1/person/X123456/personal-details")
      .headers(setAuthorisation())
      .exchange()
      .expectStatus().isOk
      .expectBody()
      .jsonPath("name.forename").isEqualTo("Alex")
      .jsonPath("preferredName").isEqualTo("AJ")
      .jsonPath("dateOfBirth").isEqualTo("1985-02-01")
      .jsonPath("mainAddress.street").isEqualTo("High Street")
      .jsonPath("emergencyContacts[0].relationship").isEqualTo("Sibling")
      .jsonPath("practitioner.name.surname").isEqualTo("Jones")
  }

  @Test
  fun `returns not found when personal details do not exist`() {
    stubGrantToken()
    stubPersonalDetailsNotFound("X000000")

    webTestClient.get()
      .uri("/v1/person/X000000/personal-details")
      .headers(setAuthorisation())
      .exchange()
      .expectStatus().isNotFound
      .expectBody()
      .jsonPath("userMessage").isEqualTo("Person not found for CRN: X000000")
  }
}
