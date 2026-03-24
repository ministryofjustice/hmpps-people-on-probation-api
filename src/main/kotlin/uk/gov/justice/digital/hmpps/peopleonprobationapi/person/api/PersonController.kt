package uk.gov.justice.digital.hmpps.peopleonprobationapi.person.api

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import uk.gov.justice.digital.hmpps.peopleonprobationapi.person.service.PersonService

@RestController
@RequestMapping("/v1/person", produces = ["application/json"])
@Tag(name = "person")
class PersonController(
  private val personService: PersonService,
) {
  @GetMapping("/{crn}/future-appointments")
  @PreAuthorize("hasAuthority('SCOPE_read')")
  @Operation(
    operationId = "getFutureAppointments",
    summary = "Get a person's future appointments by CRN",
    security = [SecurityRequirement(name = "hmpps-auth-token")],
  )
  fun getFutureAppointments(
    @Parameter(description = "Case reference number")
    @PathVariable crn: String,
    @RequestParam(defaultValue = "0") page: Int,
    @RequestParam(defaultValue = "10") size: Int,
  ): PagedAppointmentsResponse = personService.getFutureAppointments(crn, page, size).toResponse()

  @GetMapping("/{crn}/past-appointments")
  @PreAuthorize("hasAuthority('SCOPE_read')")
  @Operation(
    operationId = "getPastAppointments",
    summary = "Get a person's past appointments by CRN",
    security = [SecurityRequirement(name = "hmpps-auth-token")],
  )
  fun getPastAppointments(
    @Parameter(description = "Case reference number")
    @PathVariable crn: String,
    @RequestParam(defaultValue = "0") page: Int,
    @RequestParam(defaultValue = "10") size: Int,
  ): PagedAppointmentsResponse = personService.getPastAppointments(crn, page, size).toResponse()

  @GetMapping("/{crn}/sentences")
  @PreAuthorize("hasAuthority('SCOPE_read')")
  @Operation(
    operationId = "getSentences",
    summary = "Get a person's sentence progress by CRN",
    security = [SecurityRequirement(name = "hmpps-auth-token")],
  )
  fun getSentences(
    @Parameter(description = "Case reference number")
    @PathVariable crn: String,
  ): SentenceProgressResponse = personService.getSentenceProgress(crn).toResponse()

  @GetMapping("/{crn}/personal-details")
  @PreAuthorize("hasAuthority('SCOPE_read')")
  @Operation(
    operationId = "getPersonalDetails",
    summary = "Get a person's personal details by CRN",
    security = [SecurityRequirement(name = "hmpps-auth-token")],
  )
  fun getPersonalDetails(
    @Parameter(description = "Case reference number")
    @PathVariable crn: String,
  ): PersonalDetailsResponse = personService.getPersonalDetails(crn).toResponse()

  @GetMapping("/{crn}/name")
  @PreAuthorize("hasAuthority('SCOPE_read')")
  @Operation(
    operationId = "getName",
    summary = "Get a person's name by CRN",
    security = [SecurityRequirement(name = "hmpps-auth-token")],
  )
  fun getName(
    @Parameter(description = "Case reference number")
    @PathVariable crn: String,
  ): PersonNameResponse = personService.getName(crn).toResponse()
}
