package uk.gov.justice.digital.hmpps.peopleonprobationapi.ndelius.client

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.WebClientResponseException
import org.springframework.web.reactive.function.client.bodyToMono
import reactor.core.publisher.Mono
import uk.gov.justice.digital.hmpps.peopleonprobationapi.person.domain.AppointmentPage
import uk.gov.justice.digital.hmpps.peopleonprobationapi.person.domain.Name
import uk.gov.justice.digital.hmpps.peopleonprobationapi.person.domain.PersonalDetails
import uk.gov.justice.digital.hmpps.peopleonprobationapi.person.domain.SentenceProgress

@Component
class NDeliusClient(
  private val ndeliusWebClient: WebClient,
) {
  fun getFutureAppointments(crn: String, page: Int, size: Int): AppointmentPage? {
    log.info("Fetching future appointments from NDelius for CRN {} page {} size {}", crn, page, size)

    return try {
      ndeliusWebClient.get()
        .uri { builder ->
          builder.path("/person/{crn}/future-appointments")
            .queryParam("page", page)
            .queryParam("size", size)
            .build(crn)
        }
        .retrieve()
        .bodyToMono<NDeliusPagedAppointmentsResponse>()
        .map(NDeliusPagedAppointmentsResponse::toDomain)
        .doOnNext { appointmentPage ->
          log.info("NDelius returned {} future appointment(s) for CRN {}", appointmentPage.content.size, crn)
        }
        .onErrorResume(WebClientResponseException.NotFound::class.java) {
          log.info("Future appointments not found in NDelius for CRN {}", crn)
          Mono.empty()
        }
        .block()
    } catch (e: WebClientResponseException) {
      log.error("NDelius future appointments lookup failed for CRN {} with status {}", crn, e.statusCode.value(), e)
      throw e
    } catch (e: Exception) {
      log.error("NDelius future appointments lookup failed for CRN {}", crn, e)
      throw e
    }
  }

  fun getPastAppointments(crn: String, page: Int, size: Int): AppointmentPage? {
    log.info("Fetching past appointments from NDelius for CRN {} page {} size {}", crn, page, size)

    return try {
      ndeliusWebClient.get()
        .uri { builder ->
          builder.path("/person/{crn}/past-appointments")
            .queryParam("page", page)
            .queryParam("size", size)
            .build(crn)
        }
        .retrieve()
        .bodyToMono<NDeliusPagedAppointmentsResponse>()
        .map(NDeliusPagedAppointmentsResponse::toDomain)
        .doOnNext { appointmentPage ->
          log.info("NDelius returned {} past appointment(s) for CRN {}", appointmentPage.content.size, crn)
        }
        .onErrorResume(WebClientResponseException.NotFound::class.java) {
          log.info("Past appointments not found in NDelius for CRN {}", crn)
          Mono.empty()
        }
        .block()
    } catch (e: WebClientResponseException) {
      log.error("NDelius past appointments lookup failed for CRN {} with status {}", crn, e.statusCode.value(), e)
      throw e
    } catch (e: Exception) {
      log.error("NDelius past appointments lookup failed for CRN {}", crn, e)
      throw e
    }
  }

  fun getName(crn: String): Name? {
    log.info("Fetching name from NDelius for CRN {}", crn)

    return try {
      ndeliusWebClient.get()
        .uri("/person/{crn}/name", crn)
        .retrieve()
        .bodyToMono<NDeliusNameResponse>()
        .map(NDeliusNameResponse::toDomain)
        .doOnNext { log.info("NDelius returned name for CRN {}", crn) }
        .onErrorResume(WebClientResponseException.NotFound::class.java) {
          log.info("Name not found in NDelius for CRN {}", crn)
          Mono.empty()
        }
        .block()
    } catch (e: WebClientResponseException) {
      log.error("NDelius name lookup failed for CRN {} with status {}", crn, e.statusCode.value(), e)
      throw e
    } catch (e: Exception) {
      log.error("NDelius name lookup failed for CRN {}", crn, e)
      throw e
    }
  }

  fun getPersonalDetails(crn: String): PersonalDetails? {
    log.info("Fetching personal details from NDelius for CRN {}", crn)

    return try {
      ndeliusWebClient.get()
        .uri("/person/{crn}/personal-details", crn)
        .retrieve()
        .bodyToMono<NDeliusPersonalDetailsResponse>()
        .map(NDeliusPersonalDetailsResponse::toDomain)
        .doOnNext { log.info("NDelius returned personal details for CRN {}", crn) }
        .onErrorResume(WebClientResponseException.NotFound::class.java) {
          log.info("Personal details not found in NDelius for CRN {}", crn)
          Mono.empty()
        }
        .block()
    } catch (e: WebClientResponseException) {
      log.error("NDelius personal details lookup failed for CRN {} with status {}", crn, e.statusCode.value(), e)
      throw e
    } catch (e: Exception) {
      log.error("NDelius personal details lookup failed for CRN {}", crn, e)
      throw e
    }
  }

  fun getSentenceProgress(crn: String): SentenceProgress? {
    log.info("Fetching sentence progress from NDelius for CRN {}", crn)

    return try {
      ndeliusWebClient.get()
        .uri("/person/{crn}/sentences", crn)
        .retrieve()
        .bodyToMono<NDeliusSentenceProgressResponse>()
        .map(NDeliusSentenceProgressResponse::toDomain)
        .doOnNext { sentenceProgress ->
          log.info("NDelius returned sentence progress for CRN {} with {} sentence(s)", crn, sentenceProgress.sentences.size)
        }
        .onErrorResume(WebClientResponseException.NotFound::class.java) {
          log.info("Sentence progress not found in NDelius for CRN {}", crn)
          Mono.empty()
        }
        .block()
    } catch (e: WebClientResponseException) {
      log.error("NDelius sentence progress lookup failed for CRN {} with status {}", crn, e.statusCode.value(), e)
      throw e
    } catch (e: Exception) {
      log.error("NDelius sentence progress lookup failed for CRN {}", crn, e)
      throw e
    }
  }

  private companion object {
    private val log = LoggerFactory.getLogger(this::class.java)
  }
}
