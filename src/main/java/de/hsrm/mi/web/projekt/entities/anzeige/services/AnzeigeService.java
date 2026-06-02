package de.hsrm.mi.web.projekt.entities.anzeige.services;

import de.hsrm.mi.web.projekt.entities.anzeige.Anzeige;
import de.hsrm.mi.web.projekt.entities.benutzer.Benutzer;

import java.util.List;
import java.util.Optional;

public interface AnzeigeService {
    Anzeige saveAnzeige(Anzeige anzeige);
    Optional<Anzeige> findAnzeigeById(long id);
    List<Anzeige> findAllAnzeigen();
    void deleteAnzeigeById(long id);
    void bestellen(Anzeige a, Benutzer b);
    void stornieren(Anzeige a, Benutzer b);
    void verlosen();
}
