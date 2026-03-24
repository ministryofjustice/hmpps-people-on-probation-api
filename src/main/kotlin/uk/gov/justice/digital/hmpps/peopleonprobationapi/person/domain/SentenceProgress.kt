package uk.gov.justice.digital.hmpps.peopleonprobationapi.person.domain

import java.time.LocalDate

data class SentenceProgress(
  val sentences: List<Sentence> = emptyList(),
)

data class Sentence(
  val type: String? = null,
  val startDate: LocalDate? = null,
  val expectedEndDate: LocalDate? = null,
  val requirements: List<Requirement> = emptyList(),
  val licenceConditions: List<LicenceCondition> = emptyList(),
)

data class Requirement(
  val type: String? = null,
  val description: String? = null,
  val required: Int? = null,
  val completed: Int? = null,
  val unit: RequirementUnit? = null,
)

data class LicenceCondition(
  val type: String? = null,
  val description: String? = null,
  val startDate: LocalDate? = null,
  val expectedEndDate: LocalDate? = null,
)

enum class RequirementUnit {
  HOURS,
  DAYS,
  WEEKS,
  MONTHS,
  YEARS,
}
