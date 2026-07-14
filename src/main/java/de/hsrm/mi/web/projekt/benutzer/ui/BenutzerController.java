package de.hsrm.mi.web.projekt.benutzer.ui;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

import org.springframework.security.crypto.password.PasswordEncoder;

import de.hsrm.mi.web.projekt.entities.anzeige.Anzeige;
import de.hsrm.mi.web.projekt.entities.benutzer.Benutzer;
import de.hsrm.mi.web.projekt.entities.benutzer.mapper.BenutzerMapper;
import de.hsrm.mi.web.projekt.entities.benutzer.services.BenutzerService;
import de.hsrm.mi.web.projekt.geo.GeoAdresse;
import de.hsrm.mi.web.projekt.geo.GeoService;

import org.springframework.validation.BindingResult;

import jakarta.validation.Valid;



@Controller
@SessionAttributes("benutzerVersion")
public class BenutzerController {

    // Logger
     private static final Logger logger = LoggerFactory.getLogger(BenutzerController.class);

     private final BenutzerService benutzerService;
     private final BenutzerMapper benutzerMapper;
     private final GeoService geoService;
     private final PasswordEncoder passwordEncoder;


     public BenutzerController(BenutzerService benutzerService, BenutzerMapper benutzerMapper, GeoService geoService, PasswordEncoder passwordEncoder){

        this.benutzerService=benutzerService;
        this.benutzerMapper=benutzerMapper;
        this.geoService=geoService;
        this.passwordEncoder=passwordEncoder;
     }

    //GET Handler to Show User Form
    @GetMapping("/admin/benutzer/{loginname}")
    public String showUserForm(@PathVariable("loginname") String loginName, Model model){

        logger.info("GET /admin/benutzer/{}", loginName);

        BenutzerFormular formular= new BenutzerFormular();
        Optional<Benutzer> benutzer = benutzerService.findBenutzerById(loginName);

        if (benutzer.isPresent()) {
        logger.info("User found in database: {}", loginName);
        formular = benutzerMapper.benutzerToBenutzerFormular(benutzer.get());
        model.addAttribute("benutzerVersion", benutzer.get().getVersion());
        List<Anzeige> bestellungen = new ArrayList<>(benutzer.get().getBestellungen());
        bestellungen.sort(Comparator.comparing(Anzeige::getTitel));
        model.addAttribute("bestellungen", bestellungen);
        } else {
        logger.info("User not found, creating empty form for: {}", loginName);
        model.addAttribute("benutzerVersion", 0L);
        }


        model.addAttribute("loginname", loginName);
        model.addAttribute("formular",formular);


        return "benutzer/bearbeiten";
    }
    //GET Handler to show useres list
    @GetMapping("/admin/benutzer")
    public String showBenutzerListe(Model model) {
    logger.info("GET /admin/benutzer");
    model.addAttribute("benutzerliste", benutzerService.findAllBenutzer());
    return "benutzer/liste";
    }

    //GET Handler to delete user and show the remaining list
    @GetMapping("/admin/benutzer/{loginname}/delete")
    public String deleteBenutzer(@PathVariable("loginname") String loginName) {
    logger.info("GET /admin/benutzer/{}/delete", loginName);
    benutzerService.deleteBenutzerById(loginName);
    logger.info("Deleted user: {}", loginName);
    return "redirect:/admin/benutzer";
}
@GetMapping("/admin/benutzer/{loginName}/hx/feld/{feldname}")
public String hxGetFeld(
        @PathVariable("loginName") String loginName,
        @PathVariable("feldname") String feldname,
        Model model) {

    logger.info("GET /admin/benutzer/{}/hx/feld/{}", loginName, feldname);
    Benutzer benutzer = benutzerService.findBenutzerById(loginName)
        .orElseThrow(() -> new BenutzerException("Benutzer nicht gefunden: " + loginName));

    String wert = switch (feldname) {
        case "name" -> benutzer.getName();
        case "email" -> benutzer.getEmail();
        default -> "";
    };

    model.addAttribute("loginName", loginName);
    model.addAttribute("feldname", feldname);
    model.addAttribute("wert", wert);

    return "benutzer/eingabefeld :: bearbeiten";
}

@PutMapping("/admin/benutzer/{loginName}/hx/feld/{feldname}")
public String hxPutFeld(
        @PathVariable("loginName") String loginName,
        @PathVariable("feldname") String feldname,
        @RequestParam("wert") String wert,
        Model model) {

    logger.info("PUT /admin/benutzer/{}/hx/feld/{} wert='{}'", loginName, feldname, wert);

    try {
        Benutzer benutzer = benutzerService.aktualisiereBenutzerAttribut(loginName, feldname, wert);

        String aktuellerWert = switch (feldname) {
            case "name" -> benutzer.getName();
            case "email" -> benutzer.getEmail();
            default -> "";
        };

        logger.info("PUT /admin/benutzer/{}/hx/feld/{} -> gespeichert", loginName, feldname);
        model.addAttribute("loginName", loginName);
        model.addAttribute("feldname", feldname);
        model.addAttribute("wert", aktuellerWert);

        return "benutzer/eingabefeld :: ausgeben";

    } catch (Exception e) {
        logger.warn("PUT /admin/benutzer/{}/hx/feld/{} -> Fehler: {}", loginName, feldname, e.getMessage());

        Benutzer benutzer = benutzerService.findBenutzerById(loginName)
            .orElseThrow(() -> new BenutzerException("Benutzer nicht gefunden: " + loginName));

        String aktuellerWert = switch (feldname) {
            case "name" -> benutzer.getName();
            case "email" -> benutzer.getEmail();
            default -> "";
        };

        String fehlermeldung = switch (feldname) {
            case "name" -> "Größe muss zwischen 3 und 60 sein";
            case "email" -> "Muss eine korrekt formatierte E-Mail-Adresse sein";
            default -> "Ungültiger Wert";
        };

        model.addAttribute("loginName", loginName);
        model.addAttribute("feldname", feldname);
        model.addAttribute("wert", aktuellerWert);
        model.addAttribute("fehlermeldung", fehlermeldung);

        return "benutzer/eingabefeld :: bearbeiten";
    }
}

    // POST Handler
    @PostMapping("/admin/benutzer/{loginname}")
    public String postUserForm(@PathVariable("loginname") String loginName,
    @ModelAttribute("benutzerVersion") long benutzerVersion,
    @Valid @ModelAttribute("formular") BenutzerFormular benutzerFormular,
    BindingResult bindingResult,
    Model model) {



        logger.info("POST /admin/benutzer/{}", loginName);
        logger.info("Formular: {}", benutzerFormular);


        // Password repeat check logic
        if (!benutzerFormular.getPasswort().isEmpty() || !benutzerFormular.getPasswortWiederholung().isEmpty()) {

            if (!benutzerFormular.getPasswort().equals(benutzerFormular.getPasswortWiederholung())) {
                logger.info("Passwort und Wiederholung stimmen nicht überein für {}", loginName);
                bindingResult.rejectValue(
                "passwortWiederholung",
                "benutzer.fehler.passwortwiederholung",
                "Passworteingaben stimmen nicht überein"
        );
    }
}

        // Adressüberprüfung nur bei Neuanlage (Benutzer noch nicht in DB vorhanden)
        boolean istNeuerBenutzer = benutzerService.findBenutzerById(loginName).isEmpty();
        if (istNeuerBenutzer && !benutzerFormular.getAdresse().isEmpty()) {
            logger.info("Geo-Adressüberprüfung für neuen Benutzer {}: '{}'", loginName, benutzerFormular.getAdresse());
            List<GeoAdresse> geoAdressen = geoService.findeAdressen(benutzerFormular.getAdresse());
            if (geoAdressen.isEmpty()) {
                logger.info("Keine Geo-Adresse gefunden für '{}', Formularfeld wird als fehlerhaft markiert", benutzerFormular.getAdresse());
                bindingResult.rejectValue(
                    "adresse",
                    "benutzer.fehler.adresse",
                    "Die eingegebene Adresse konnte nicht gefunden werden"
                );
            } else {
                String displayName = geoAdressen.get(0).display_name();
                logger.info("Geo-Adresse gefunden: '{}', ersetze Adresse durch display_name: '{}'", benutzerFormular.getAdresse(), displayName);
                benutzerFormular.setAdresse(displayName);
            }
        }

        // If validation failed, return the form view so errors can be shown
        if (bindingResult.hasErrors()) {
            logger.info("Validation errors: {}", bindingResult.getAllErrors());
            model.addAttribute("loginname", loginName);
            model.addAttribute("formular", benutzerFormular);
            return "benutzer/bearbeiten";
        }

        try {
            Benutzer benutzer = benutzerMapper.benutzerFormularToBenutzer(benutzerFormular);
            benutzer.setLoginName(loginName);
            benutzer.setVersion(benutzerVersion);
            logger.info("New version is set to: {}",benutzerVersion);


            if (benutzerFormular.getPasswort() == null || benutzerFormular.getPasswort().isEmpty()) {
                logger.warn("Password is empty for the username: {}", loginName);
                Optional<Benutzer> existingUser = benutzerService.findBenutzerById(loginName);
                if (existingUser.isPresent()) {
                    benutzer.setPasswort(existingUser.get().getPasswort()); // keep existing encoded hash
                    logger.info("Password unchanged for the existing user:{}", loginName);
                } else {
                    logger.error("New user without password is not allowed!");
                    throw new BenutzerException("Fehler: Neuer Benutzer ohne Passwort ist nicht erlaubt");
                }
            } else {
                // Encode the plain-text password before persisting
                String encodedPassword = passwordEncoder.encode(benutzerFormular.getPasswort());
                benutzer.setPasswort(encodedPassword);
                logger.info("Password encoded for user: {}", loginName);
            }

            try {
                Benutzer savedUser = benutzerService.saveBenutzer(benutzer);
                logger.info("User saved successfully: {} ", savedUser);
                return "redirect:/admin/benutzer";
            } catch (ObjectOptimisticLockingFailureException e) {
                logger.warn("Optimistic locking failure for user {}: Data was modified by another user", loginName, e);
                throw new BenutzerException("Fehler: Die Daten wurden von einem anderen Benutzer geändert.", e);
            } catch (DataAccessException e) {
                logger.error("Unable to save user {}", loginName, e);
                throw new BenutzerException("Schade, es gibt einen Fehler beim Abspeichern.", e);
            }
        } catch (BenutzerException e) {
            model.addAttribute("info", e.getMessage());
            model.addAttribute("loginname", loginName);
            model.addAttribute("formular", benutzerFormular);
            return "benutzer/bearbeiten";
        }
    }


}
