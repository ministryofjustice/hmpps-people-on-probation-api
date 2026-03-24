package uk.gov.justice.digital.hmpps.peopleonprobationapi.person.service

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.peopleonprobationapi.ndelius.client.NDeliusClient
import uk.gov.justice.digital.hmpps.peopleonprobationapi.person.domain.AppointmentPage
import uk.gov.justice.digital.hmpps.peopleonprobationapi.person.domain.Name
import uk.gov.justice.digital.hmpps.peopleonprobationapi.person.domain.PersonNotFoundException
import uk.gov.justice.digital.hmpps.peopleonprobationapi.person.domain.PersonalDetails
import uk.gov.justice.digital.hmpps.peopleonprobationapi.person.domain.SentenceProgress

@Service
class PersonService(
  private val ndeliusClient: NDeliusClient,
) {
  fun getFutureAppointments(crn: String, page: Int, size: Int): AppointmentPage {
    log.info("Looking up future appointments for CRN {} page {} size {}", crn, page, size)

    return ndeliusClient.getFutureAppointments(crn, page, size)
      ?.also { log.info("Future appointments found for CRN {} with {} appointment(s)", crn, it.content.size) }
      ?: run {
        log.warn("Future appointments not found for CRN {}", crn)
        throw PersonNotFoundException("Person not found for CRN: $crn")
      }
  }

  fun getPastAppointments(crn: String, page: Int, size: Int): AppointmentPage {
    log.info("Looking up past appointments for CRN {} page {} size {}", crn, page, size)

    return ndeliusClient.getPastAppointments(crn, page, size)
      ?.also { log.info("Past appointments found for CRN {} with {} appointment(s)", crn, it.content.size) }
      ?: run {
        log.warn("Past appointments not found for CRN {}", crn)
        throw PersonNotFoundException("Person not found for CRN: $crn")
      }
  }

  fun getName(crn: String): Name {
    log.info("Looking up name for CRN {}", crn)

    return ndeliusClient.getName(crn)
      ?.also { log.info("Name found for CRN {}", crn) }
      ?: run {
        log.warn("Name not found for CRN {}", crn)
        throw PersonNotFoundException("Person not found for CRN: $crn")
      }
  }

  fun getPersonalDetails(crn: String): PersonalDetails {
    log.info("Looking up personal details for CRN {}", crn)

    return ndeliusClient.getPersonalDetails(crn)
      ?.also { log.info("Personal details found for CRN {}", crn) }
      ?: run {
        log.warn("Personal details not found for CRN {}", crn)
        throw PersonNotFoundException("Person not found for CRN: $crn")
      }
  }

  fun getSentenceProgress(crn: String): SentenceProgress {
    log.info("Looking up sentence progress for CRN {}", crn)

    return ndeliusClient.getSentenceProgress(crn)
      ?.also { log.info("Sentence progress found for CRN {}", crn) }
      ?: run {
        log.warn("Sentence progress not found for CRN {}", crn)
        throw PersonNotFoundException("Person not found for CRN: $crn")
      }
  }

  private companion object {
    private val log = LoggerFactory.getLogger(this::class.java)
  }
}
