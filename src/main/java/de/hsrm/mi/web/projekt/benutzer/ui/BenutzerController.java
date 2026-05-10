package de.hsrm.mi.web.projekt.benutzer.ui;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.validation.BindingResult;

import jakarta.validation.Valid;


@Controller
@SessionAttributes("formularMap")
public class BenutzerController {

    // Logger
    Logger logger = LoggerFactory.getLogger(BenutzerController.class);

    //Make empty map at the start of the session
    @ModelAttribute("formularMap")
    public Map<String, BenutzerFormular> initFormularMap() {
        logger.info("creating new formularMap");
        return new HashMap<String, BenutzerFormular>();
    }

    // GET handler
    @GetMapping("/admin/benutzer/{loginname}")
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
    }


    //POST handler
    @PostMapping("/admin/benutzer/{loginname}")
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
    }

}
