package uk.gov.justice.digital.hmpps.peopleonprobationapi.person.api

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "A person's name")
data class PersonNameResponse(
  @Schema(description = "The person's first name", example = "Alex")
  val forename: String,
  @Schema(description = "The person's middle name", example = "James")
  val middleName: String? = null,
  @Schema(description = "The person's surname", example = "Smith")
  val surname: String,
)
