package uk.gov.justice.digital.hmpps.peopleonprobationapi

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class PeopleOnProbationApi

fun main(args: Array<String>) {
  runApplication<PeopleOnProbationApi>(*args)
}
