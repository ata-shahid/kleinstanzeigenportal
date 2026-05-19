package de.hsrm.mi.web.projekt.benutzer.ui;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import de.hsrm.mi.web.projekt.entities.benutzer.Benutzer;
import de.hsrm.mi.web.projekt.entities.benutzer.mapper.BenutzerMapper;
import de.hsrm.mi.web.projekt.entities.benutzer.services.BenutzerService;

import org.springframework.validation.BindingResult;

import jakarta.validation.Valid;


@Controller
//@SessionAttributes("formularMap")
public class BenutzerController {

    // Logger
     private static final Logger logger = LoggerFactory.getLogger(BenutzerController.class);

     private final BenutzerService benutzerService;
     private final BenutzerMapper benutzerMapper;


     public BenutzerController(BenutzerService benutzerService, BenutzerMapper benutzerMapper){

        this.benutzerService=benutzerService;
        this.benutzerMapper=benutzerMapper;
     }


    //Make empty map at the start of the session (Depricated)
    /*@ModelAttribute("formularMap")
    public Map<String, BenutzerFormular> initFormularMap() {
        logger.info("creating new formularMap");
        return new HashMap<String, BenutzerFormular>();
    }*/

    // GET handler (Depricated)
    /*@GetMapping("/admin/benutzer/{loginname}")
    public String showUserForm(@PathVariable("loginname") String loginName,
            @ModelAttribute("formularMap") Map<String, BenutzerFormular> benutzerFormularMap,
            Model model) {

        logger.info("GET /admin/benutzer/{}", loginName);

        // If the name is not in the map, create the form and save it in the map
        if(!benutzerFormularMap.containsKey(loginName)){

            logger.info("creating form for {}",loginName);
            benutzerFormularMap.put(loginName, new BenutzerFormular());

        }
        // get the correct form for the user
        BenutzerFormular formular= benutzerFormularMap.get(loginName);

        model.addAttribute("loginname", loginName);
        model.addAttribute("formular",formular);



        return "benutzer/bearbeiten";
    }*/

    //GET Handler
    @GetMapping("/admin/benutzer/{loginname}")
    public String showUserForm(@PathVariable("loginname") String loginName, Model model){

        BenutzerFormular formular= new BenutzerFormular();
        Optional<Benutzer> benutzer = benutzerService.findBenutzerById(loginName);

        if(benutzer.isPresent()){
            logger.info("User found: {}",loginName);
            formular=benutzerMapper.benutzerToBenutzerFormular(benutzer.get());
            model.addAttribute("loginname", loginName);
            model.addAttribute("formular",formular);

        }else{
            model.addAttribute("loginname", loginName);
            model.addAttribute("formular",formular);
        }

        return "benutzer/bearbeiten";
    }



    //POST handler (Depricated)
    /*@PostMapping("/admin/benutzer/{loginname}")
    public String postUserForm(@PathVariable("loginname") String loginName,
    @Valid @ModelAttribute("formular") BenutzerFormular benutzerFormular,
    BindingResult bindingResult,
    @ModelAttribute("formularMap") Map<String, BenutzerFormular> benutzerFormularMap,
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

        // If validation failed, return the form view so errors can be shown
        if (bindingResult.hasErrors()) {
            logger.info("Validation errors: {}", bindingResult.getAllErrors());
            model.addAttribute("loginname", loginName);
            model.addAttribute("formular", benutzerFormular);
            return "benutzer/bearbeiten";
        }

        // update in the Map
        benutzerFormularMap.put(loginName, benutzerFormular);

        model.addAttribute("loginname", loginName);
        model.addAttribute("formular", benutzerFormular);

        return "benutzer/bearbeiten";
    }*/

    // POST Handler
    @PostMapping("/admin/benutzer/{loginname}")
    public String postUserForm(@PathVariable("loginname") String loginName,
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

            if (benutzerFormular.getPasswort() == null || benutzerFormular.getPasswort().isEmpty()) {
                logger.warn("Password is empty for the username: {}", loginName);
                Optional<Benutzer> existingUser = benutzerService.findBenutzerById(loginName);
                if (existingUser.isPresent()) {
                    benutzer.setPasswort(existingUser.get().getPasswort());
                    logger.info("Password unchanged for the exisiting user:{}", loginName);
                } else {
                    logger.error("New user without password is not allowed!");
                    throw new BenutzerException("Fehler: Neuer Benutzer ohne Passwort ist nicht erlaubt");
                }
            }

            try {
                Benutzer savedUser = benutzerService.saveBenutzer(benutzer);
                logger.info("User saved successfully: {} ", savedUser);
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



        return "benutzer/bearbeiten";
    }


}
