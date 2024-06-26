package com.projekt.studiengangsorganisation.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import jakarta.servlet.http.HttpServletResponse;

/**
 * Konfigurationsklasse für die Sicherheitseinstellungen der Webanwendung.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

        /**
         * Definiert die Sicherheitsfilterkette.
         * 
         * @param http Die HttpSecurity-Konfiguration.
         * @return Die konfigurierte Sicherheitsfilterkette.
         * @throws Exception Falls ein Fehler bei der Konfiguration auftritt.
         */
        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
                AuthenticationManager authenticationManager = authenticationManager(
                                http.getSharedObject(AuthenticationConfiguration.class));
                CustomUsernamePasswordAuthenticationFilter customAuthenticationFilter = new CustomUsernamePasswordAuthenticationFilter(
                                authenticationManager);
                customAuthenticationFilter.setFilterProcessesUrl("/auth/login");

                http
                                // Deaktiviert CSRF-Schutz
                                .csrf(AbstractHttpConfigurer::disable)
                                // Konfiguriert die Zugriffsberechtigungen für verschiedene URLs
                                .authorizeHttpRequests(authz -> authz
                                                .requestMatchers("/api/testdata").permitAll()
                                                .requestMatchers("/api/**", "/auth/info").authenticated()
                                                .anyRequest().permitAll())
                                // Fügt den benutzerdefinierten Authentifizierungsfilter hinzu
                                .addFilterBefore(customAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                                // Konfiguriert die Sitzungsverwaltung
                                .sessionManagement(session -> session
                                                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED))
                                // Konfiguriert den Logout-Prozess
                                .logout(logout -> logout
                                                .logoutUrl("/auth/logout")
                                                .deleteCookies("JSESSIONID")
                                                .invalidateHttpSession(true)
                                                .clearAuthentication(true)
                                                .logoutSuccessHandler((request, response, authentication) -> {
                                                        response.setStatus(HttpServletResponse.SC_OK);
                                                }));

                return http.build();
        }

        /**
         * Bean für die Passwortverschlüsselung.
         * 
         * @return Ein Passwortencoder.
         */
        @Bean
        public PasswordEncoder passwordEncoder() {
                return new BCryptPasswordEncoder();
        }

        /**
         * Bean für den AuthenticationManager.
         * 
         * @param authenticationConfiguration Die Authentifizierungskonfiguration.
         * @return Ein AuthenticationManager.
         * @throws Exception Falls ein Fehler beim Abrufen des AuthenticationManagers
         *                   auftritt.
         */
        @Bean
        public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
                        throws Exception {
                return authenticationConfiguration.getAuthenticationManager();
        }
}