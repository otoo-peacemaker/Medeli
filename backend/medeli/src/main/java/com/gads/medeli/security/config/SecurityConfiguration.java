package com.gads.medeli.security.config;

import com.gads.medeli.security.jwt.JwtAuthenticationFilter;
import com.gads.medeli.util.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {
  private static final String[] WHITE_LIST_URLS = {
          "**/user/**",
          "/", "/css/*", "index", "/js/*"};
  private final JwtAuthenticationFilter jwtAuthFilter;
  private final AuthenticationProvider authenticationProvider;

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
        .csrf().disable()
            .authorizeHttpRequests((authorizeHttpRequests) ->
                    authorizeHttpRequests
                            .requestMatchers(WHITE_LIST_URLS).hasAnyRole(Role.USER.name(),Role.ADMIN.name())
                            .anyRequest().permitAll())
        .sessionManagement()
        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and()
        .authenticationProvider(authenticationProvider)
        .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
    return http.build();
  }
}

/**
 * .authorizeHttpRequests()
 *         .requestMatchers("/api/v1/auth/**")
 *         .permitAll()
 *         .anyRequest()
 *         .authenticated()
 *         .and()
 * */