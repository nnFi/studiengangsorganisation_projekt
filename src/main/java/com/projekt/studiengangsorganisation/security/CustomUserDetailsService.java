package com.projekt.studiengangsorganisation.security;

import com.projekt.studiengangsorganisation.entity.Nutzer;
import com.projekt.studiengangsorganisation.repository.NutzerRepository;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Implementierung des Spring Security UserDetailsService für benutzerdefinierte
 * Authentifizierung.
 * 
 * @author Paul Rakow
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    /**
     * Injektion des NutzerRepositories.
     */
    @Autowired
    private NutzerRepository nutzerUserRepository;

    /**
     * Lädt einen Benutzer anhand seines Benutzernamens.
     * 
     * @param username Der Benutzername des Benutzers.
     * @return Ein UserDetails-Objekt, das den gefundenen Benutzer repräsentiert.
     * @throws UsernameNotFoundException Falls kein Benutzer mit dem angegebenen
     *                                   Benutzernamen gefunden wird.
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Ruft den Nutzer anhand des Benutzernamens aus dem Repository auf oder wirft
        // einen Fehler, wenn kein Benutzer gefunden wird
        Nutzer nutzerUser = nutzerUserRepository.findByUsername(username)
                .orElseThrow(
                        () -> new UsernameNotFoundException("Benutzer nicht gefunden mit Benutzernamen: " + username));

        // Erstellt und gibt ein Spring Security User-Objekt zurück, basierend auf den
        // Benutzerdaten aus der Datenbank
        return new org.springframework.security.core.userdetails.User(nutzerUser.getUsername(),
                nutzerUser.getPassword(),
                new ArrayList<>());
    }
}