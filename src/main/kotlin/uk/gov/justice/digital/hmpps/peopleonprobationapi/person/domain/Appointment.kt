package uk.gov.justice.digital.hmpps.peopleonprobationapi.person.domain

import java.time.LocalDate

data class AppointmentPage(
  val content: List<Appointment> = emptyList(),
  val page: PageMetadata,
)

data class Appointment(
  val date: LocalDate? = null,
  val startTime: String? = null,
  val endTime: String? = null,
  val type: String? = null,
  val description: String? = null,
  val practitioner: Practitioner? = null,
  val location: Address? = null,
  val attended: Boolean? = null,
  val complied: Boolean? = null,
)

data class Practitioner(
  val name: Name? = null,
)

data class PageMetadata(
  val size: Long,
  val number: Long,
  val totalElements: Long,
  val totalPages: Long,
)
