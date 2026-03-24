package uk.gov.justice.digital.hmpps.peopleonprobationapi.ndelius.client

import uk.gov.justice.digital.hmpps.peopleonprobationapi.person.domain.Address
import uk.gov.justice.digital.hmpps.peopleonprobationapi.person.domain.Manager
import uk.gov.justice.digital.hmpps.peopleonprobationapi.person.domain.PersonalContact
import uk.gov.justice.digital.hmpps.peopleonprobationapi.person.domain.PersonalDetails
import uk.gov.justice.digital.hmpps.peopleonprobationapi.person.domain.Team
import java.time.LocalDate

data class NDeliusPersonalDetailsResponse(
  val name: NDeliusNameResponse,
  val preferredName: String? = null,
  val dateOfBirth: LocalDate? = null,
  val mainAddress: NDeliusAddressResponse? = null,
  val telephoneNumber: String? = null,
  val mobileNumber: String? = null,
  val emailAddress: String? = null,
  val emergencyContacts: List<NDeliusPersonalContactResponse> = emptyList(),
  val practitioner: NDeliusManagerResponse? = null,
)

data class NDeliusAddressResponse(
  val houseNumber: String? = null,
  val buildingName: String? = null,
  val street: String? = null,
  val town: String? = null,
  val district: String? = null,
  val county: String? = null,
  val postcode: String? = null,
)

data class NDeliusPersonalContactResponse(
  val name: NDeliusNameResponse,
  val relationship: String? = null,
  val mobileNumber: String? = null,
  val emailAddress: String? = null,
)

data class NDeliusManagerResponse(
  val name: NDeliusNameResponse,
  val telephoneNumber: String? = null,
  val team: NDeliusTeamResponse? = null,
)

data class NDeliusTeamResponse(
  val officeAddresses: List<NDeliusAddressResponse> = emptyList(),
)

fun NDeliusPersonalDetailsResponse.toDomain() = PersonalDetails(
  name = name.toDomain(),
  preferredName = preferredName,
  dateOfBirth = dateOfBirth,
  mainAddress = mainAddress?.toDomain(),
  telephoneNumber = telephoneNumber,
  mobileNumber = mobileNumber,
  emailAddress = emailAddress,
  emergencyContacts = emergencyContacts.map(NDeliusPersonalContactResponse::toDomain),
  practitioner = practitioner?.toDomain(),
)

internal fun NDeliusAddressResponse.toDomain() = Address(
  houseNumber = houseNumber,
  buildingName = buildingName,
  street = street,
  town = town,
  district = district,
  county = county,
  postcode = postcode,
)

private fun NDeliusPersonalContactResponse.toDomain() = PersonalContact(
  name = name.toDomain(),
  relationship = relationship,
  mobileNumber = mobileNumber,
  emailAddress = emailAddress,
)

private fun NDeliusManagerResponse.toDomain() = Manager(
  name = name.toDomain(),
  telephoneNumber = telephoneNumber,
  team = team?.toDomain(),
)

private fun NDeliusTeamResponse.toDomain() = Team(
  officeAddresses = officeAddresses.map(NDeliusAddressResponse::toDomain),
)
