package uk.gov.justice.digital.hmpps.peopleonprobationapi.person.service

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import uk.gov.justice.digital.hmpps.peopleonprobationapi.ndelius.client.NDeliusClient
import uk.gov.justice.digital.hmpps.peopleonprobationapi.person.domain.AppointmentPage
import uk.gov.justice.digital.hmpps.peopleonprobationapi.person.domain.Name
import uk.gov.justice.digital.hmpps.peopleonprobationapi.person.domain.PageMetadata
import uk.gov.justice.digital.hmpps.peopleonprobationapi.person.domain.PersonNotFoundException
import uk.gov.justice.digital.hmpps.peopleonprobationapi.person.domain.PersonalDetails
import uk.gov.justice.digital.hmpps.peopleonprobationapi.person.domain.SentenceProgress

class PersonServiceTest {
  private val ndeliusClient = mock<NDeliusClient>()
  private val personService = PersonService(ndeliusClient)

  @Test
  fun `returns name when ndelius returns a match`() {
    whenever(ndeliusClient.getName("X123456")).thenReturn(
      Name(
        forename = "Alex",
        middleName = "James",
        surname = "Smith",
      ),
    )

    val result = personService.getName("X123456")

    assertThat(result).isEqualTo(
      Name(
        forename = "Alex",
        middleName = "James",
        surname = "Smith",
      ),
    )
  }

  @Test
  fun `throws person not found when ndelius does not return a match`() {
    whenever(ndeliusClient.getName("X000000")).thenReturn(null)

    assertThatThrownBy { personService.getName("X000000") }
      .isInstanceOf(PersonNotFoundException::class.java)
      .hasMessage("Person not found for CRN: X000000")
  }

  @Test
  fun `returns personal details when ndelius returns a match`() {
    whenever(ndeliusClient.getPersonalDetails("X123456")).thenReturn(
      PersonalDetails(
        name = Name(
          forename = "Alex",
          middleName = "James",
          surname = "Smith",
        ),
        preferredName = "AJ",
      ),
    )

    val result = personService.getPersonalDetails("X123456")

    assertThat(result).isEqualTo(
      PersonalDetails(
        name = Name(
          forename = "Alex",
          middleName = "James",
          surname = "Smith",
        ),
        preferredName = "AJ",
      ),
    )
  }

  @Test
  fun `throws person not found when ndelius does not return personal details`() {
    whenever(ndeliusClient.getPersonalDetails("X000000")).thenReturn(null)

    assertThatThrownBy { personService.getPersonalDetails("X000000") }
      .isInstanceOf(PersonNotFoundException::class.java)
      .hasMessage("Person not found for CRN: X000000")
  }

  @Test
  fun `returns sentence progress when ndelius returns a match`() {
    whenever(ndeliusClient.getSentenceProgress("X123456")).thenReturn(
      SentenceProgress(sentences = emptyList()),
    )

    val result = personService.getSentenceProgress("X123456")

    assertThat(result).isEqualTo(SentenceProgress(sentences = emptyList()))
  }

  @Test
  fun `throws person not found when ndelius does not return sentence progress`() {
    whenever(ndeliusClient.getSentenceProgress("X000000")).thenReturn(null)

    assertThatThrownBy { personService.getSentenceProgress("X000000") }
      .isInstanceOf(PersonNotFoundException::class.java)
      .hasMessage("Person not found for CRN: X000000")
  }

  @Test
  fun `returns future appointments when ndelius returns a match`() {
    whenever(ndeliusClient.getFutureAppointments("X123456", 0, 10)).thenReturn(
      AppointmentPage(
        content = emptyList(),
        page = PageMetadata(size = 10, number = 0, totalElements = 0, totalPages = 0),
      ),
    )

    val result = personService.getFutureAppointments("X123456", 0, 10)

    assertThat(result).isEqualTo(
      AppointmentPage(
        content = emptyList(),
        page = PageMetadata(size = 10, number = 0, totalElements = 0, totalPages = 0),
      ),
    )
  }

  @Test
  fun `throws person not found when ndelius does not return future appointments`() {
    whenever(ndeliusClient.getFutureAppointments("X000000", 0, 10)).thenReturn(null)

    assertThatThrownBy { personService.getFutureAppointments("X000000", 0, 10) }
      .isInstanceOf(PersonNotFoundException::class.java)
      .hasMessage("Person not found for CRN: X000000")
  }

  @Test
  fun `returns past appointments when ndelius returns a match`() {
    whenever(ndeliusClient.getPastAppointments("X123456", 0, 10)).thenReturn(
      AppointmentPage(
        content = emptyList(),
        page = PageMetadata(size = 10, number = 0, totalElements = 0, totalPages = 0),
      ),
    )

    val result = personService.getPastAppointments("X123456", 0, 10)

    assertThat(result).isEqualTo(
      AppointmentPage(
        content = emptyList(),
        page = PageMetadata(size = 10, number = 0, totalElements = 0, totalPages = 0),
      ),
    )
  }

  @Test
  fun `throws person not found when ndelius does not return past appointments`() {
    whenever(ndeliusClient.getPastAppointments("X000000", 0, 10)).thenReturn(null)

    assertThatThrownBy { personService.getPastAppointments("X000000", 0, 10) }
      .isInstanceOf(PersonNotFoundException::class.java)
      .hasMessage("Person not found for CRN: X000000")
  }
}
