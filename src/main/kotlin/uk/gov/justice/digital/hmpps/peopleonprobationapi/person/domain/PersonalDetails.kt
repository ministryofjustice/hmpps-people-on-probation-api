package uk.gov.justice.digital.hmpps.peopleonprobationapi.person.domain

import java.time.LocalDate

data class PersonalDetails(
  val name: Name,
  val preferredName: String? = null,
  val dateOfBirth: LocalDate? = null,
  val mainAddress: Address? = null,
  val telephoneNumber: String? = null,
  val mobileNumber: String? = null,
  val emailAddress: String? = null,
  val emergencyContacts: List<PersonalContact> = emptyList(),
  val practitioner: Manager? = null,
)

data class Address(
  val houseNumber: String? = null,
  val buildingName: String? = null,
  val street: String? = null,
  val town: String? = null,
  val district: String? = null,
  val county: String? = null,
  val postcode: String? = null,
)

data class PersonalContact(
  val name: Name,
  val relationship: String? = null,
  val mobileNumber: String? = null,
  val emailAddress: String? = null,
)

data class Manager(
  val name: Name,
  val telephoneNumber: String? = null,
  val team: Team? = null,
)

data class Team(
  val officeAddresses: List<Address> = emptyList(),
)
