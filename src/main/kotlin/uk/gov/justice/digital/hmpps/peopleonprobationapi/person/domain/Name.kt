package uk.gov.justice.digital.hmpps.peopleonprobationapi.person.domain

data class Name(
  val forename: String,
  val middleName: String? = null,
  val surname: String,
)
