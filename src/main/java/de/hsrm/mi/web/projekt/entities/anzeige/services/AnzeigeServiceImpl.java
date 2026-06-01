package de.hsrm.mi.web.projekt.entities.anzeige.services;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Sort;


import de.hsrm.mi.web.projekt.entities.anzeige.Anzeige;
import de.hsrm.mi.web.projekt.entities.anzeige.AnzeigeRepository;


@Service
public class AnzeigeServiceImpl implements AnzeigeService{

  //Logger
  private static final Logger logger= LoggerFactory.getLogger(AnzeigeServiceImpl.class);

  private final AnzeigeRepository anzeigeRepository;

  public AnzeigeServiceImpl(AnzeigeRepository anzeigeRepository){
    this.anzeigeRepository=anzeigeRepository;
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

}
