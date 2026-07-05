package de.hsrm.mi.web.projekt.entities.anzeige.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Sort;

import de.hsrm.mi.web.projekt.anzeige.ui.AnzeigeException;
import de.hsrm.mi.web.projekt.entities.anzeige.Anzeige;
import de.hsrm.mi.web.projekt.entities.anzeige.AnzeigeRepository;
import de.hsrm.mi.web.projekt.entities.benutzer.Benutzer;
import de.hsrm.mi.web.projekt.entities.benutzer.services.BenutzerService;
import de.hsrm.mi.web.projekt.messaging.FrontendNachrichtEvent;
import de.hsrm.mi.web.projekt.messaging.FrontendNachrichtEvent.EventOperation;
import de.hsrm.mi.web.projekt.messaging.FrontendNachrichtEvent.EventTyp;


@Service
public class AnzeigeServiceImpl implements AnzeigeService {

  // Logger
  private static final Logger logger = LoggerFactory.getLogger(AnzeigeServiceImpl.class);

  private final AnzeigeRepository anzeigeRepository;
  private final BenutzerService benutzerService;
  private final ApplicationEventPublisher eventPublisher;

  public AnzeigeServiceImpl(AnzeigeRepository anzeigeRepository,
                             BenutzerService benutzerService,
                             ApplicationEventPublisher eventPublisher) {
    this.anzeigeRepository = anzeigeRepository;
    this.benutzerService = benutzerService;
    this.eventPublisher = eventPublisher;
  }

  @Override
  @Transactional
  public Anzeige saveAnzeige(Anzeige anzeige) {
    logger.info("saveAnzeige({})", anzeige);

    // Determine if this is a CREATE or UPDATE before saving
    boolean isNew = (anzeige.getId() == 0);
    Anzeige savedAnzeige = anzeigeRepository.save(anzeige);

    logger.info("saveAnzeige -> {}", savedAnzeige);

    // Publish event so the frontend gets notified after the transaction commits
    EventOperation op = isNew ? EventOperation.CREATE : EventOperation.UPDATE;
    eventPublisher.publishEvent(new FrontendNachrichtEvent(EventTyp.ANZEIGE, savedAnzeige.getId(), op));
    logger.info("saveAnzeige: publishEvent {} für Anzeige ID {}", op, savedAnzeige.getId());

    // Künstliche Verzögerung zum Testen von Race Conditions (auskommentiert lassen für Abnahme)
    // try { Thread.sleep(1000); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }

    return savedAnzeige;
  }

  @Override
  public Optional<Anzeige> findAnzeigeById(long id) {
    logger.info("findAnzeigeById({})", id);
    Optional<Anzeige> anzeige = anzeigeRepository.findById(id);
    logger.info("findAnzeigeById({}) -> {}", id, anzeige);
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
  @Transactional
  public void deleteAnzeigeById(long id) {
    logger.info("deleteAnzeigeById({})", id);
    anzeigeRepository.deleteById(id);
    logger.info("deleteAnzeigeById({}) done", id);

    // Publish DELETE event so the frontend removes the entry
    eventPublisher.publishEvent(new FrontendNachrichtEvent(EventTyp.ANZEIGE, id, EventOperation.DELETE));
    logger.info("deleteAnzeigeById: publishEvent DELETE für Anzeige ID {}", id);
  }

  @Override
  @Transactional
  public void bestellen(Anzeige a, Benutzer b) {
    if (a.getBesteller().contains(b)) {
      logger.info("bestellen(): Benutzer {} ist bereits Besteller", b.getLoginName());
      throw new AnzeigeException("Der Benutzer ist bereits Besteller");
    }
    if (b.equals(a.getAnbieter())) {
      logger.info("bestellen(): Benutzer {} ist Anbieter", b.getLoginName());
      throw new AnzeigeException("Der Benutzer ist Anbieter");
    }
    // Check if the stock is available
    if (a.getAnzahl() <= a.getBesteller().size()) {
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
    if (a.getBesteller().contains(b)) {
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

    for (Anzeige a : alleAnzeigen) {
      int randomIndex = new Random().nextInt(alleBenutzer.size());
      Benutzer randomBenutzer = alleBenutzer.get(randomIndex);
      a.setAnbieter(randomBenutzer);
      saveAnzeige(a);
      logger.info("verlosen(): Anzeige {} bekommt Anbieter {}", a.getTitel(), randomBenutzer.getLoginName());
    }
    for (Benutzer b : alleBenutzer) {
      for (int i = 0; i < 3; i++) {
        Anzeige randomAnzeige = null;
        try {
          int randomIndex = new Random().nextInt(alleAnzeigen.size());
          randomAnzeige = alleAnzeigen.get(randomIndex);
          bestellen(randomAnzeige, b);
          logger.info("verlosen(): Benutzer {} hat Anzeige {} bestellt", b.getLoginName(), randomAnzeige.getTitel());
        } catch (AnzeigeException e) {
          logger.info("verlosen(): Benutzer {} konnte Anzeige {} nicht bestellen: {}",
              b.getLoginName(),
              randomAnzeige != null ? randomAnzeige.getTitel() : "unbekannt",
              e.getMessage());
        }
      }
    }
  }
}
