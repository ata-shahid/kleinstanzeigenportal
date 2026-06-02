package de.hsrm.mi.web.projekt.entities.anzeige.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Sort;

import de.hsrm.mi.web.projekt.anzeige.ui.AnzeigeException;
import de.hsrm.mi.web.projekt.entities.anzeige.Anzeige;
import de.hsrm.mi.web.projekt.entities.anzeige.AnzeigeRepository;
import de.hsrm.mi.web.projekt.entities.benutzer.Benutzer;
import de.hsrm.mi.web.projekt.entities.benutzer.services.BenutzerService;


@Service
public class AnzeigeServiceImpl implements AnzeigeService{

  //Logger
  private static final Logger logger= LoggerFactory.getLogger(AnzeigeServiceImpl.class);

  private final AnzeigeRepository anzeigeRepository;
  private final BenutzerService benutzerService;

  public AnzeigeServiceImpl(AnzeigeRepository anzeigeRepository, BenutzerService benutzerService){
    this.anzeigeRepository=anzeigeRepository;
    this.benutzerService=benutzerService;
  }

  @Override
  public Anzeige saveAnzeige(Anzeige anzeige) {
    logger.info("saveAnzeige({})", anzeige);
    Anzeige savedAnzeige= anzeigeRepository.save(anzeige);

    logger.info("saveAnzeige ->{}", savedAnzeige);

    return savedAnzeige;

  }

  @Override
  public Optional<Anzeige> findAnzeigeById(long id) {

    logger.info("findAnzeigeById({})", id);
    Optional<Anzeige> anzeige = anzeigeRepository.findById(id);
    logger.info("findAnzeigeById({}) -> {}",id,anzeige);
    return anzeige;
  }

  @Override
  public List<Anzeige> findAllAnzeigen() {

    logger.info("findAllAnzeigen()");
    List<Anzeige> anzeige = anzeigeRepository.findAll(Sort.by("ablaufdatum").ascending());
    logger.info("findAllAnzeigen() -> {} Anzeige", anzeige.size());
    return anzeige;

  }

  @Override
  public void deleteAnzeigeById(long id) {
    logger.info("deleteAnzeigeById({})", id);
    anzeigeRepository.deleteById(id);
    logger.info("deleteAnzeigeById({}) done", id);
  }

  @Override
  @Transactional
  public void bestellen(Anzeige a, Benutzer b) {


    if(a.getBesteller().contains(b)){
      logger.info("bestellen(): Benutzer {} ist bereits Besteller", b.getLoginName());
      throw new AnzeigeException("Der Benutzer ist bereits Besteller");
    }
    if (b.equals(a.getAnbieter())) {
      logger.info("bestellen(): Benutzer {} ist Anbieter", b.getLoginName());
      throw new AnzeigeException("Der Benutzer ist Anbieter");
    }
    // check if the stock is available
    if(a.getAnzahl()<=a.getBesteller().size()){
      logger.info("bestellen(): Das Ding {} ist ausverkauft", a.getTitel());
      throw new AnzeigeException("Das Ding ist ausverkauft.");
    }

    a.getBesteller().add(b);
    saveAnzeige(a);
    logger.info("bestellen(): Benutzer {} hat Anzeige {} bestellt", b.getLoginName(), a.getTitel());
  }

  @Override
  @Transactional
  public void stornieren(Anzeige a, Benutzer b) {

    if(a.getBesteller().contains(b)){
        a.getBesteller().remove(b);
        saveAnzeige(a);
        logger.info("stornieren(): Benutzer {} hat Anzeige {} storniert", b.getLoginName(), a.getTitel());
    } else {
        logger.info("stornieren(): Benutzer {} ist kein Besteller von {}", b.getLoginName(), a.getTitel());
        throw new AnzeigeException("Der Benutzer ist kein Besteller dieser Anzeige");
    }
  }

  @Override
  @Transactional
  public void verlosen() {
    List<Anzeige> alleAnzeigen = findAllAnzeigen();
    List<Benutzer> alleBenutzer = new ArrayList<>(benutzerService.findAllBenutzer());

    for(Anzeige a : alleAnzeigen) {
      int randomIndex = new Random().nextInt(alleBenutzer.size());
      Benutzer randomBenutzer = alleBenutzer.get(randomIndex);
      a.setAnbieter(randomBenutzer);
      saveAnzeige(a);
      logger.info("verlosen(): Anzeige {} bekommt Anbieter {}", a.getTitel(), randomBenutzer.getLoginName());
  }
  for(Benutzer b : alleBenutzer) {
    for(int i = 0; i < 3; i++) {
        Anzeige randomAnzeige = null;
        try {
            int randomIndex = new Random().nextInt(alleAnzeigen.size());
            randomAnzeige = alleAnzeigen.get(randomIndex);
            bestellen(randomAnzeige, b);
            logger.info("verlosen(): Benutzer {} hat Anzeige {} bestellt", b.getLoginName(), randomAnzeige.getTitel());
        } catch(AnzeigeException e) {
            logger.info("verlosen(): Benutzer {} konnte Anzeige {} nicht bestellen: {}",
            b.getLoginName(),
            randomAnzeige != null ? randomAnzeige.getTitel() : "unbekannt",
            e.getMessage());
        }
    }
}


  }

}
