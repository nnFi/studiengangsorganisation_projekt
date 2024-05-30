package com.projekt.studiengangsorganisation.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Ein benutzerdefinierter Filter für die Authentifizierung über Benutzername und Passwort.
 */
public class CustomUsernamePasswordAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    //Objektmapper für die Konvertierung von JSON.
    private final ObjectMapper objectMapper = new ObjectMapper();
    // Sicherheitskontext-Repository für die Sitzungsbasierte Sicherheitskontextspeicherung.
    private final SecurityContextRepository securityContextRepository = new HttpSessionSecurityContextRepository();


    /**
     * Konstruktor für den benutzerdefinierten Authentifizierungsfilter.
     * @param authenticationManager Der AuthenticationManager für die Authentifizierung.
     */
    public CustomUsernamePasswordAuthenticationFilter(AuthenticationManager authenticationManager) {
        super(new AntPathRequestMatcher("/auth/login", "POST"));
        setAuthenticationManager(authenticationManager);
    }

    /**
     * Versucht die Authentifizierung anhand der erhaltenen Anmeldeinformationen.
     * @param request  Das HttpServletRequest-Objekt.
     * @param response Das HttpServletResponse-Objekt.
     * @return Die Authentifizierungsinformationen.
     * @throws AuthenticationException Falls die Authentifizierung fehlschlägt.
     * @throws IOException              Falls ein Fehler beim Lesen der Anforderung auftritt.
     */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException, IOException {
                // Unterdrückt Warnungen
                @SuppressWarnings("unchecked")
                
                // HTTP-Request-Körper wird in ein Map-Objekt umgewandelt, das Key-Value-Paare enthält
                Map<String, String> requestBody = objectMapper.readValue(request.getInputStream(), HashMap.class);
                
                // Holt den Benutzernamen
                String username = requestBody.get("username");
                
                // Holt das Passwort
                String password = requestBody.get("password");
                
                // Erstellt eine Authentifizierungsanfrage mit Benutzername und Passwort
                UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(username, password);
                
                // Authentifiziert den Benutzer und gibt das Ergebnis zurück
                return getAuthenticationManager().authenticate(authRequest);
    }

    /**
     * Wird aufgerufen, wenn die Authentifizierung erfolgreich ist.
     * @param request    Das HttpServletRequest-Objekt.
     * @param response   Das HttpServletResponse-Objekt.
     * @param chain      Die Filterkette.
     * @param authResult Die Authentifizierungsergebnisse.
     * @throws IOException      Falls ein Fehler beim Schreiben der Antwort auftritt.
     * @throws ServletException Falls ein Servlet-Fehler auftritt.
     */
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
            Authentication authResult) throws IOException, ServletException {
        // Setzt die Authentifizierungsinformationen im Sicherheitskontext
        SecurityContextHolder.getContext().setAuthentication(authResult);
        // Speichert den aktualisierten Sicherheitskontext in der aktuellen Sitzung
        securityContextRepository.saveContext(SecurityContextHolder.getContext(), request, response);

        // Setzt den HTTP-Statuscode auf "OK" (200)
        response.setStatus(HttpServletResponse.SC_OK);

    }

    /**
     * Wird aufgerufen, wenn die Authentifizierung fehlschlägt.
     * @param request Das HttpServletRequest-Objekt.
     * @param response Das HttpServletResponse-Objekt.
     * @param failed   Die fehlgeschlagene Authentifizierungsausnahme.
     * @throws IOException      Falls ein Fehler beim Schreiben der Antwort auftritt.
     * @throws ServletException Falls ein Servlet-Fehler auftritt.
     */
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException failed) throws IOException, ServletException {
        // Setzt den HTTP-Statuscode auf "UNAUTHORIZED" (401), um anzuzeigen, dass die Authentifizierung fehlgeschlagen ist
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

    }
}
