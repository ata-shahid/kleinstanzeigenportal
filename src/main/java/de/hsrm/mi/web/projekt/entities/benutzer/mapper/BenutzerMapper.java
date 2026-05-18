package de.hsrm.mi.web.projekt.entities.benutzer.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import de.hsrm.mi.web.projekt.benutzer.ui.BenutzerFormular;
import de.hsrm.mi.web.projekt.entities.benutzer.Benutzer;

@Mapper(componentModel = "spring")
public interface BenutzerMapper {

  // BenutzerFormular aus Benutzer-Entitaet fuellen
  @Mapping(target = "passwortWiederholung", ignore = true)
  BenutzerFormular benutzerToBenutzerFormular(Benutzer benutzer);

  // Benutzer-Entitaet aus Formularinhalt fuellen
  @Mapping(target = "loginName", ignore = true)
  Benutzer benutzerFormularToBenutzer(BenutzerFormular formular);

}
