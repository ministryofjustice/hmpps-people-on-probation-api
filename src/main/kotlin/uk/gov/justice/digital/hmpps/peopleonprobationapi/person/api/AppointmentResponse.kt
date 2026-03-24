package uk.gov.justice.digital.hmpps.peopleonprobationapi.person.api

import java.time.LocalDate

data class PagedAppointmentsResponse(
  val content: List<AppointmentResponse> = emptyList(),
  val page: PageMetadataResponse,
)

data class AppointmentResponse(
  val date: LocalDate? = null,
  val startTime: String? = null,
  val endTime: String? = null,
  val type: String? = null,
  val description: String? = null,
  val practitioner: PractitionerResponse? = null,
  val location: AddressResponse? = null,
  val attended: Boolean? = null,
  val complied: Boolean? = null,
)

data class PractitionerResponse(
  val name: PersonNameResponse? = null,
)

data class PageMetadataResponse(
  val size: Long,
  val number: Long,
  val totalElements: Long,
  val totalPages: Long,
)
