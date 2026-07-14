package de.hsrm.mi.web.projekt.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import de.hsrm.mi.web.projekt.entities.benutzer.Benutzer;
import de.hsrm.mi.web.projekt.entities.benutzer.services.BenutzerService;


@Service
public class BenutzerUserDetailsService implements UserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(BenutzerUserDetailsService.class);

    private final PasswordEncoder passwordEncoder;
    private final BenutzerService benutzerService;

    public BenutzerUserDetailsService(PasswordEncoder passwordEncoder, BenutzerService benutzerService) {
        this.passwordEncoder = passwordEncoder;
        this.benutzerService = benutzerService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        logger.info("loadUserByUsername({})", username);

        // Hardcoded admin user — always available regardless of the database
        if ("admin".equals(username)) {
            logger.info("loadUserByUsername: hardcoded admin user returned");
            return User.builder()
                    .username("admin")
                    .password(passwordEncoder.encode("admin"))
                    .roles("ADMINISTRATOR")
                    .build();
        }

        // Look up in the database
        Benutzer benutzer = benutzerService.findBenutzerById(username)
                .orElseThrow(() -> {
                    logger.warn("loadUserByUsername: Benutzer '{}' nicht gefunden", username);
                    return new UsernameNotFoundException("Benutzer nicht gefunden: " + username);
                });

        logger.info("loadUserByUsername: DB-Benutzer '{}' mit Rolle '{}' geladen", username, benutzer.getRolle());


        return User.builder()
                .username(benutzer.getLoginName())
                .password(benutzer.getPasswort())
                .roles(benutzer.getRolle())
                .build();
    }
}
