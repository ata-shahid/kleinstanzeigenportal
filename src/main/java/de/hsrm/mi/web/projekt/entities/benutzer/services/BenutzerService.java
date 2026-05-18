package de.hsrm.mi.web.projekt.entities.benutzer.services;

import java.util.Collection;
import java.util.Optional;

import de.hsrm.mi.web.projekt.entities.benutzer.Benutzer;

public interface BenutzerService {
Benutzer saveBenutzer(Benutzer benutzer);

Optional<Benutzer> findBenutzerById(String loginName);

Collection<Benutzer> findAllBenutzer();

void deleteBenutzerById(String loginName);
}
