package uk.gov.justice.digital.hmpps.peopleonprobationapi.config

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.web.filter.OncePerRequestFilter

@Configuration
@ConditionalOnProperty(name = ["app.dev-auth-bypass"], havingValue = "true")
class DevSecurityConfiguration {
  @Bean
  fun devSecurityFilterChain(http: HttpSecurity): SecurityFilterChain = http
    .csrf { it.disable() }
    .authorizeHttpRequests { it.anyRequest().permitAll() }
    .addFilterBefore(devAuthenticationFilter(), UsernamePasswordAuthenticationFilter::class.java)
    .build()

  @Bean
  fun devAuthenticationFilter() = object : OncePerRequestFilter() {
    override fun doFilterInternal(
      request: HttpServletRequest,
      response: HttpServletResponse,
      filterChain: FilterChain,
    ) {
      val context = SecurityContextHolder.createEmptyContext()
      context.authentication = UsernamePasswordAuthenticationToken(
        "dev-user",
        "N/A",
        listOf(SimpleGrantedAuthority("SCOPE_read")),
      )
      SecurityContextHolder.setContext(context)

      try {
        filterChain.doFilter(request, response)
      } finally {
        SecurityContextHolder.clearContext()
      }
    }
  }
}
