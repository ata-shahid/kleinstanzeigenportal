package de.hsrm.mi.web.projekt.entities.anzeige.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import de.hsrm.mi.web.projekt.anzeige.ui.AnzeigeFormular;
import de.hsrm.mi.web.projekt.entities.anzeige.Anzeige;

@Mapper(componentModel = "spring")
public interface AnzeigeMapper {

  //AnzeigeFormular aus Anzeige-Entität fuellen
  AnzeigeFormular anzeigeToAnzeigeFormular(Anzeige anzeige);

  //Benutzer-Entitaet aus Formularinhalt fuellen
  @Mapping(target = "version", ignore = true)
  @Mapping(target = "anbieter", ignore = true)
  @Mapping(target = "besteller", ignore = true)
  Anzeige anzeigeFormularToAnzeige(AnzeigeFormular formular);


}

