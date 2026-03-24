package uk.gov.justice.digital.hmpps.peopleonprobationapi.person.api

import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDate

@Schema(description = "A person's sentence progress")
data class SentenceProgressResponse(
  val sentences: List<SentenceResponse> = emptyList(),
)

data class SentenceResponse(
  val type: String? = null,
  val startDate: LocalDate? = null,
  val expectedEndDate: LocalDate? = null,
  val requirements: List<RequirementResponse> = emptyList(),
  val licenceConditions: List<LicenceConditionResponse> = emptyList(),
)

data class RequirementResponse(
  val type: String? = null,
  val description: String? = null,
  val required: Int? = null,
  val completed: Int? = null,
  val unit: String? = null,
)

data class LicenceConditionResponse(
  val type: String? = null,
  val description: String? = null,
  val startDate: LocalDate? = null,
  val expectedEndDate: LocalDate? = null,
)
