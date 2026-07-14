package de.hsrm.mi.web.projekt.anzeige.api;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import de.hsrm.mi.web.projekt.anzeige.ui.AnzeigeException;
import de.hsrm.mi.web.projekt.entities.anzeige.Anzeige;
import de.hsrm.mi.web.projekt.entities.anzeige.mapper.AnzeigeMapper;
import de.hsrm.mi.web.projekt.entities.anzeige.services.AnzeigeService;
import de.hsrm.mi.web.projekt.entities.benutzer.Benutzer;
import de.hsrm.mi.web.projekt.entities.benutzer.services.BenutzerService;

@RestController
@RequestMapping("/api")
public class AnzeigeRestController {

    private static final Logger logger = LoggerFactory.getLogger(AnzeigeRestController.class);

    private final AnzeigeService anzeigeService;
    private final AnzeigeMapper anzeigeMapper;
    private final BenutzerService benutzerService;

    public AnzeigeRestController(AnzeigeService anzeigeService, AnzeigeMapper anzeigeMapper,
            BenutzerService benutzerService) {
        this.anzeigeService = anzeigeService;
        this.anzeigeMapper = anzeigeMapper;
        this.benutzerService = benutzerService;
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
                .map(a -> {
                    logger.info("GET /api/anzeige/{} -> {}", id, a.getTitel());
                    return anzeigeMapper.anzeigeToAnzeigeDTO(a);
                })
                .orElseThrow(() -> {
                    logger.warn("GET /api/anzeige/{} -> not found", id);
                    return new AnzeigeNotFoundException("Anzeige nicht gefunden: " + id);
                });
    }

    /**
     * POST /api/anzeige/{id}/bestellen
     * Body: { "loginName": "..." }
     * Bestellt den Artikel der Anzeige für den angegebenen Benutzer.
     */
    @PostMapping("/anzeige/{id}/bestellen")
    public ResponseEntity<String> bestellen(
            @PathVariable("id") long id,
            @RequestBody BestellungRequest request) {

        logger.info("POST /api/anzeige/{}/bestellen fuer loginName={}", id, request.loginName());

        Anzeige anzeige = anzeigeService.findAnzeigeById(id)
                .orElseThrow(() -> new AnzeigeNotFoundException("Anzeige nicht gefunden: " + id));

        Benutzer benutzer = benutzerService.findBenutzerById(request.loginName())
                .orElseThrow(() -> new AnzeigeNotFoundException("Benutzer nicht gefunden: " + request.loginName()));

        try {
            anzeigeService.bestellen(anzeige, benutzer);
            logger.info("POST /api/anzeige/{}/bestellen -> erfolgreich fuer {}", id, request.loginName());
            return ResponseEntity.ok("Bestellung erfolgreich");
        } catch (AnzeigeException e) {
            logger.warn("POST /api/anzeige/{}/bestellen -> Fehler: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    /**
     * DELETE /api/anzeige/{id}/bestellen/{loginName}
     * Storniert die Bestellung des Benutzers für die Anzeige.
     */
    @DeleteMapping("/anzeige/{id}/bestellen/{loginName}")
    public ResponseEntity<String> stornieren(
            @PathVariable("id") long id,
            @PathVariable("loginName") String loginName) {

        logger.info("DELETE /api/anzeige/{}/bestellen/{}", id, loginName);

        Anzeige anzeige = anzeigeService.findAnzeigeById(id)
                .orElseThrow(() -> new AnzeigeNotFoundException("Anzeige nicht gefunden: " + id));

        Benutzer benutzer = benutzerService.findBenutzerById(loginName)
                .orElseThrow(() -> new AnzeigeNotFoundException("Benutzer nicht gefunden: " + loginName));

        try {
            anzeigeService.stornieren(anzeige, benutzer);
            logger.info("DELETE /api/anzeige/{}/bestellen/{} -> erfolgreich", id, loginName);
            return ResponseEntity.ok("Stornierung erfolgreich");
        } catch (AnzeigeException e) {
            logger.warn("DELETE /api/anzeige/{}/bestellen/{} -> Fehler: {}", id, loginName, e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    /** Simple request body record for bestellen */
    public record BestellungRequest(String loginName) {}
}
