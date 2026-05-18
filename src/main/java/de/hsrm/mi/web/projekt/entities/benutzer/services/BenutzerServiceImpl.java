package de.hsrm.mi.web.projekt.entities.benutzer.services;

import java.util.Collection;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import de.hsrm.mi.web.projekt.entities.benutzer.Benutzer;
import de.hsrm.mi.web.projekt.entities.benutzer.BenutzerRepository;

@Service
public class BenutzerServiceImpl implements BenutzerService {
  // Logger
  private static final Logger logger = LoggerFactory.getLogger(BenutzerServiceImpl.class);

  private final BenutzerRepository benutzerRepository;

  public BenutzerServiceImpl(BenutzerRepository benutzerRepository) {
    this.benutzerRepository = benutzerRepository;
  }

  @Override
  public Benutzer saveBenutzer(Benutzer b) {
    logger.info("saveBenutzer({})", b);
    Benutzer savedBenutzer = benutzerRepository.save(b);
    logger.info("saveBenutzer -> {}", savedBenutzer);
    return savedBenutzer;
  }

  @Override
  public Optional<Benutzer> findBenutzerById(String loginName) {
    logger.info("findBenutzerById({})", loginName);
    Optional<Benutzer> benutzer = benutzerRepository.findById(loginName);
    logger.info("findBenutzerById({}) -> {}", loginName, benutzer);
    return benutzer;
  }

  @Override
  public Collection<Benutzer> findAllBenutzer() {
    logger.info("findAllBenutzer()");
    Collection<Benutzer> benutzer = benutzerRepository.findAll(Sort.by("loginName").ascending());
    logger.info("findAllBenutzer() -> {} Benutzer", benutzer.size());
    return benutzer;
  }

  @Override
  public void deleteBenutzerById(String loginName) {
    logger.info("deleteBenutzerById({})", loginName);
    benutzerRepository.deleteById(loginName);
    logger.info("deleteBenutzerById({}) done", loginName);
  }

}
