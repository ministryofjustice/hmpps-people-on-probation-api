package uk.gov.justice.digital.hmpps.peopleonprobationapi.config

import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Contact
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.security.SecurityRequirement
import io.swagger.v3.oas.models.security.SecurityScheme
import io.swagger.v3.oas.models.servers.Server
import org.springframework.boot.info.BuildProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class OpenApiConfiguration(buildProperties: BuildProperties) {
  private val version: String = buildProperties.version!!

  @Bean
  fun customOpenAPI(): OpenAPI = OpenAPI()
    .servers(
      listOf(
        Server().url("https://people-on-probation-api-dev.hmpps.service.justice.gov.uk").description("Development"),
        Server().url("https://people-on-probation-api-preprod.hmpps.service.justice.gov.uk").description("Pre-Production"),
        Server().url("https://people-on-probation-api.hmpps.service.justice.gov.uk").description("Production"),
        Server().url("http://localhost:8080").description("Local"),
      ),
    )
    .tags(
      listOf(),
    )
    .info(
      Info().title("HMPPS People On Probation Api").version(version)
        .contact(Contact().name("HMPPS Digital Studio").email("feedback@digital.justice.gov.uk")),
    )
    .components(
      io.swagger.v3.oas.models.Components().addSecuritySchemes(
        "hmpps-auth-token",
        SecurityScheme().addBearerJwtRequirement("ROLE_PEOPLE_ON_PROBATION__PEOPLE_ON_PROBATION_UI"),
      ),
    )
    .addSecurityItem(SecurityRequirement().addList("hmpps-auth-token"))
}

private fun SecurityScheme.addBearerJwtRequirement(role: String): SecurityScheme = type(SecurityScheme.Type.HTTP)
  .scheme("bearer")
  .bearerFormat("JWT")
  .`in`(SecurityScheme.In.HEADER)
  .name("Authorization")
  .description("A HMPPS Auth access token with the `$role` role.")
