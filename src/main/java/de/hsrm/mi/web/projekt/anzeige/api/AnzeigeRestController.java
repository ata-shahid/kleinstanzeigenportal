package de.hsrm.mi.web.projekt.anzeige.api;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import de.hsrm.mi.web.projekt.entities.anzeige.mapper.AnzeigeMapper;
import de.hsrm.mi.web.projekt.entities.anzeige.services.AnzeigeService;

@RestController
@RequestMapping("/api")
public class AnzeigeRestController {

    private static final Logger logger = LoggerFactory.getLogger(AnzeigeRestController.class);

    private final AnzeigeService anzeigeService;
    private final AnzeigeMapper anzeigeMapper;

    public AnzeigeRestController(AnzeigeService anzeigeService, AnzeigeMapper anzeigeMapper) {
        this.anzeigeService = anzeigeService;
        this.anzeigeMapper = anzeigeMapper;
    }

    @GetMapping("/anzeige")
    public List<AnzeigeDTO> getAllAnzeigen() {
        logger.info("GET /api/anzeige");
        List<AnzeigeDTO> result = anzeigeMapper.anzeigeListToAnzeigeDTOList(anzeigeService.findAllAnzeigen());
        logger.info("GET /api/anzeige -> {} Anzeigen", result.size());
        return result;
    }

    @GetMapping("/anzeige/{id}")
    public AnzeigeDTO getAnzeigeById(@PathVariable("id") long id) {
        logger.info("GET /api/anzeige/{}", id);
        return anzeigeService.findAnzeigeById(id)
            .map(a -> { logger.info("GET /api/anzeige/{} -> {}", id, a.getTitel()); return anzeigeMapper.anzeigeToAnzeigeDTO(a); })
            .orElseThrow(() -> { logger.warn("GET /api/anzeige/{} -> not found", id); return new AnzeigeNotFoundException("Anzeige nicht gefunden: " + id); });
    }
}
