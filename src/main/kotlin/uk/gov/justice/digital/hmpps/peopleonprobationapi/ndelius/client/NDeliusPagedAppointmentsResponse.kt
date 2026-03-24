package uk.gov.justice.digital.hmpps.peopleonprobationapi.ndelius.client

import uk.gov.justice.digital.hmpps.peopleonprobationapi.person.domain.Appointment
import uk.gov.justice.digital.hmpps.peopleonprobationapi.person.domain.AppointmentPage
import uk.gov.justice.digital.hmpps.peopleonprobationapi.person.domain.PageMetadata
import uk.gov.justice.digital.hmpps.peopleonprobationapi.person.domain.Practitioner
import java.time.LocalDate

data class NDeliusPagedAppointmentsResponse(
  val content: List<NDeliusAppointmentResponse> = emptyList(),
  val page: NDeliusPageMetadataResponse,
)

data class NDeliusAppointmentResponse(
  val date: LocalDate? = null,
  val startTime: String? = null,
  val endTime: String? = null,
  val type: String? = null,
  val description: String? = null,
  val practitioner: NDeliusPractitionerResponse? = null,
  val location: NDeliusAddressResponse? = null,
  val attended: Boolean? = null,
  val complied: Boolean? = null,
)

data class NDeliusPageMetadataResponse(
  val size: Long,
  val number: Long,
  val totalElements: Long,
  val totalPages: Long,
)

data class NDeliusPractitionerResponse(
  val name: NDeliusNameResponse? = null,
)

fun NDeliusPagedAppointmentsResponse.toDomain() = AppointmentPage(
  content = content.map(NDeliusAppointmentResponse::toDomain),
  page = page.toDomain(),
)

private fun NDeliusAppointmentResponse.toDomain() = Appointment(
  date = date,
  startTime = startTime,
  endTime = endTime,
  type = type,
  description = description,
  practitioner = practitioner?.toDomain(),
  location = location?.toDomain(),
  attended = attended,
  complied = complied,
)

private fun NDeliusPageMetadataResponse.toDomain() = PageMetadata(
  size = size,
  number = number,
  totalElements = totalElements,
  totalPages = totalPages,
)

private fun NDeliusPractitionerResponse.toDomain() = Practitioner(
  name = name?.toDomain(),
)
