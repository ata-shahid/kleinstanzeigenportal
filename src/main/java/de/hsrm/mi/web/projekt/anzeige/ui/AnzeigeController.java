package de.hsrm.mi.web.projekt.anzeige.ui;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

import de.hsrm.mi.web.projekt.entities.anzeige.Anzeige;
import de.hsrm.mi.web.projekt.entities.anzeige.mapper.AnzeigeMapper;
import de.hsrm.mi.web.projekt.entities.anzeige.services.AnzeigeService;

import org.springframework.validation.BindingResult;

import jakarta.validation.Valid;


@Controller
@SessionAttributes("anzeigeVersion")
public class AnzeigeController {

    // Logger
    private static final Logger logger = LoggerFactory.getLogger(AnzeigeController.class);

    private final AnzeigeService anzeigeService;
    private final AnzeigeMapper anzeigeMapper;

    public AnzeigeController(AnzeigeService anzeigeService, AnzeigeMapper anzeigeMapper) {
        this.anzeigeService = anzeigeService;
        this.anzeigeMapper = anzeigeMapper;
    }

    // GET Handler to show the Anzeige Form
    @GetMapping("/admin/anzeige/{id}")
    public String showAnzeigeForm(@PathVariable("id") long id, Model model) {
        logger.info("GET /admin/anzeige/{}", id);

        AnzeigeFormular formular = new AnzeigeFormular();

        if (id == 0) {
            logger.info("Creating empty anzeige form");
            model.addAttribute("anzeigeVersion", 0L);
        } else {
            Optional<Anzeige> anzeige = anzeigeService.findAnzeigeById(id);
            if (anzeige.isPresent()) {
                logger.info("Anzeige found in database: {}", id);
                formular = anzeigeMapper.anzeigeToAnzeigeFormular(anzeige.get());
                model.addAttribute("anzeigeVersion", anzeige.get().getVersion());
            } else {
                logger.info("Anzeige not found for id: {}, redirecting to new form", id);
                return "redirect:/admin/anzeige/0";
            }
        }

        model.addAttribute("id", id);
        model.addAttribute("formular", formular);

        return "anzeige/bearbeiten";
    }

    // GET Handler to show the Anzeigenliste
    @GetMapping("/admin/anzeige")
    public String showAnzeigeListe(Model model) {
        logger.info("GET /admin/anzeige");
        model.addAttribute("anzeigeliste", anzeigeService.findAllAnzeigen());
        return "anzeige/liste";
    }

    // GET Handler to delete Anzeige and show the remaining list
    @GetMapping("/admin/anzeige/{id}/delete")
    public String deleteAnzeige(@PathVariable("id") long id) {
        logger.info("GET /admin/anzeige/{}/delete", id);
        anzeigeService.deleteAnzeigeById(id);
        logger.info("Deleted Anzeige with id: {}", id);
        return "redirect:/admin/anzeige";
    }

    // POST Handler
    @PostMapping("/admin/anzeige/{id}")
    public String postAnzeigeForm(@PathVariable("id") long id,
    @ModelAttribute("anzeigeVersion") long anzeigeVersion,
    @Valid @ModelAttribute("formular") AnzeigeFormular anzeigeFormular,
    BindingResult bindingResult,
    Model model) {

        logger.info("POST /admin/anzeige/{}", id);
        logger.info("Formular: {}", anzeigeFormular);

        // If validation failed, return the form view so errors can be shown
        if (bindingResult.hasErrors()) {
            logger.info("Validation errors: {}", bindingResult.getAllErrors());
            model.addAttribute("id", id);
            model.addAttribute("formular", anzeigeFormular);
            return "anzeige/bearbeiten";
        }

        try {
            Anzeige anzeige = anzeigeMapper.anzeigeFormularToAnzeige(anzeigeFormular);
            anzeige.setId(id);
            anzeige.setVersion(anzeigeVersion);
            logger.info("New version is set to: {}", anzeigeVersion);

            try {
                Anzeige savedAnzeige = anzeigeService.saveAnzeige(anzeige);
                logger.info("Anzeige saved successfully: {}", savedAnzeige);
                return "redirect:/admin/anzeige";
            } catch (ObjectOptimisticLockingFailureException e) {
                logger.warn("Optimistic locking failure for Anzeige id {}: Data was modified by another user", id, e);
                throw new AnzeigeException("Fehler: Die Daten wurden von einem anderen Benutzer geändert.", e);
            } catch (DataAccessException e) {
                logger.error("Unable to save Anzeige {}", id, e);
                throw new AnzeigeException("Schade, es gibt einen Fehler beim Abspeichern.", e);
            }
        } catch (AnzeigeException e) {
            model.addAttribute("info", e.getMessage());
            model.addAttribute("id", id);
            model.addAttribute("formular", anzeigeFormular);
            return "anzeige/bearbeiten";
        }
    }


}
