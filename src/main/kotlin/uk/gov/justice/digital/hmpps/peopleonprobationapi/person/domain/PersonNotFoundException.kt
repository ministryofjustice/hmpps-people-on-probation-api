package uk.gov.justice.digital.hmpps.peopleonprobationapi.person.domain

class PersonNotFoundException(
  message: String,
) : RuntimeException(message)
