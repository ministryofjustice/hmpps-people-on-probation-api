package uk.gov.justice.digital.hmpps.peopleonprobationapi.ndelius.client

import uk.gov.justice.digital.hmpps.peopleonprobationapi.person.domain.Name

data class NDeliusNameResponse(
  val forename: String,
  val middleName: String? = null,
  val surname: String,
)

internal fun NDeliusNameResponse.toDomain() = Name(
  forename = forename,
  middleName = middleName,
  surname = surname,
)
