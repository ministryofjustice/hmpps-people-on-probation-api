package uk.gov.justice.digital.hmpps.peopleonprobationapi.person.api

import uk.gov.justice.digital.hmpps.peopleonprobationapi.person.domain.Address
import uk.gov.justice.digital.hmpps.peopleonprobationapi.person.domain.Appointment
import uk.gov.justice.digital.hmpps.peopleonprobationapi.person.domain.AppointmentPage
import uk.gov.justice.digital.hmpps.peopleonprobationapi.person.domain.LicenceCondition
import uk.gov.justice.digital.hmpps.peopleonprobationapi.person.domain.Manager
import uk.gov.justice.digital.hmpps.peopleonprobationapi.person.domain.Name
import uk.gov.justice.digital.hmpps.peopleonprobationapi.person.domain.PageMetadata
import uk.gov.justice.digital.hmpps.peopleonprobationapi.person.domain.PersonalContact
import uk.gov.justice.digital.hmpps.peopleonprobationapi.person.domain.PersonalDetails
import uk.gov.justice.digital.hmpps.peopleonprobationapi.person.domain.Practitioner
import uk.gov.justice.digital.hmpps.peopleonprobationapi.person.domain.Requirement
import uk.gov.justice.digital.hmpps.peopleonprobationapi.person.domain.Sentence
import uk.gov.justice.digital.hmpps.peopleonprobationapi.person.domain.SentenceProgress
import uk.gov.justice.digital.hmpps.peopleonprobationapi.person.domain.Team

internal fun Name.toResponse() = PersonNameResponse(
  forename = forename,
  middleName = middleName,
  surname = surname,
)

internal fun PersonalDetails.toResponse() = PersonalDetailsResponse(
  name = name.toResponse(),
  preferredName = preferredName,
  dateOfBirth = dateOfBirth,
  mainAddress = mainAddress?.toResponse(),
  telephoneNumber = telephoneNumber,
  mobileNumber = mobileNumber,
  emailAddress = emailAddress,
  emergencyContacts = emergencyContacts.map(PersonalContact::toResponse),
  practitioner = practitioner?.toResponse(),
)

internal fun SentenceProgress.toResponse() = SentenceProgressResponse(
  sentences = sentences.map(Sentence::toResponse),
)

internal fun AppointmentPage.toResponse() = PagedAppointmentsResponse(
  content = content.map(Appointment::toResponse),
  page = page.toResponse(),
)

private fun Address.toResponse() = AddressResponse(
  houseNumber = houseNumber,
  buildingName = buildingName,
  street = street,
  town = town,
  district = district,
  county = county,
  postcode = postcode,
)

private fun PersonalContact.toResponse() = PersonalContactResponse(
  name = name.toResponse(),
  relationship = relationship,
  mobileNumber = mobileNumber,
  emailAddress = emailAddress,
)

private fun Manager.toResponse() = ManagerResponse(
  name = name.toResponse(),
  telephoneNumber = telephoneNumber,
  team = team?.toResponse(),
)

private fun Team.toResponse() = TeamResponse(
  officeAddresses = officeAddresses.map(Address::toResponse),
)

private fun Appointment.toResponse() = AppointmentResponse(
  date = date,
  startTime = startTime,
  endTime = endTime,
  type = type,
  description = description,
  practitioner = practitioner?.toResponse(),
  location = location?.toResponse(),
  attended = attended,
  complied = complied,
)

private fun Practitioner.toResponse() = PractitionerResponse(
  name = name?.toResponse(),
)

private fun PageMetadata.toResponse() = PageMetadataResponse(
  size = size,
  number = number,
  totalElements = totalElements,
  totalPages = totalPages,
)

private fun Sentence.toResponse() = SentenceResponse(
  type = type,
  startDate = startDate,
  expectedEndDate = expectedEndDate,
  requirements = requirements.map(Requirement::toResponse),
  licenceConditions = licenceConditions.map(LicenceCondition::toResponse),
)

private fun Requirement.toResponse() = RequirementResponse(
  type = type,
  description = description,
  required = required,
  completed = completed,
  unit = unit?.name,
)

private fun LicenceCondition.toResponse() = LicenceConditionResponse(
  type = type,
  description = description,
  startDate = startDate,
  expectedEndDate = expectedEndDate,
)
