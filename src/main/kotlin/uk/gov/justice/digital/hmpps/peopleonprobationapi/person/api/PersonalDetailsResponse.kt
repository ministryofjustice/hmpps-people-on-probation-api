package uk.gov.justice.digital.hmpps.peopleonprobationapi.person.api

import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDate

@Schema(description = "A person's personal details")
data class PersonalDetailsResponse(
  val name: PersonNameResponse,
  val preferredName: String? = null,
  val dateOfBirth: LocalDate? = null,
  val mainAddress: AddressResponse? = null,
  val telephoneNumber: String? = null,
  val mobileNumber: String? = null,
  val emailAddress: String? = null,
  val emergencyContacts: List<PersonalContactResponse> = emptyList(),
  val practitioner: ManagerResponse? = null,
)

data class AddressResponse(
  val houseNumber: String? = null,
  val buildingName: String? = null,
  val street: String? = null,
  val town: String? = null,
  val district: String? = null,
  val county: String? = null,
  val postcode: String? = null,
)

data class PersonalContactResponse(
  val name: PersonNameResponse,
  val relationship: String? = null,
  val mobileNumber: String? = null,
  val emailAddress: String? = null,
)

data class ManagerResponse(
  val name: PersonNameResponse,
  val telephoneNumber: String? = null,
  val team: TeamResponse? = null,
)

data class TeamResponse(
  val officeAddresses: List<AddressResponse> = emptyList(),
)
