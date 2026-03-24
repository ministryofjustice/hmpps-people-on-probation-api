package uk.gov.justice.digital.hmpps.peopleonprobationapi.ndelius.client

import uk.gov.justice.digital.hmpps.peopleonprobationapi.person.domain.LicenceCondition
import uk.gov.justice.digital.hmpps.peopleonprobationapi.person.domain.Requirement
import uk.gov.justice.digital.hmpps.peopleonprobationapi.person.domain.RequirementUnit
import uk.gov.justice.digital.hmpps.peopleonprobationapi.person.domain.Sentence
import uk.gov.justice.digital.hmpps.peopleonprobationapi.person.domain.SentenceProgress
import java.time.LocalDate

data class NDeliusSentenceProgressResponse(
  val sentences: List<NDeliusSentenceResponse> = emptyList(),
)

data class NDeliusSentenceResponse(
  val type: String? = null,
  val startDate: LocalDate? = null,
  val expectedEndDate: LocalDate? = null,
  val requirements: List<NDeliusRequirementResponse> = emptyList(),
  val licenceConditions: List<NDeliusLicenceConditionResponse> = emptyList(),
)

data class NDeliusRequirementResponse(
  val type: String? = null,
  val description: String? = null,
  val required: Int? = null,
  val completed: Int? = null,
  val unit: RequirementUnit? = null,
)

data class NDeliusLicenceConditionResponse(
  val type: String? = null,
  val description: String? = null,
  val startDate: LocalDate? = null,
  val expectedEndDate: LocalDate? = null,
)

fun NDeliusSentenceProgressResponse.toDomain() = SentenceProgress(
  sentences = sentences.map(NDeliusSentenceResponse::toDomain),
)

private fun NDeliusSentenceResponse.toDomain() = Sentence(
  type = type,
  startDate = startDate,
  expectedEndDate = expectedEndDate,
  requirements = requirements.map(NDeliusRequirementResponse::toDomain),
  licenceConditions = licenceConditions.map(NDeliusLicenceConditionResponse::toDomain),
)

private fun NDeliusRequirementResponse.toDomain() = Requirement(
  type = type,
  description = description,
  required = required,
  completed = completed,
  unit = unit,
)

private fun NDeliusLicenceConditionResponse.toDomain() = LicenceCondition(
  type = type,
  description = description,
  startDate = startDate,
  expectedEndDate = expectedEndDate,
)
