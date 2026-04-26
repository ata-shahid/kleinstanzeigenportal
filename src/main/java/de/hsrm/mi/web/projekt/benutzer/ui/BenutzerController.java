package de.hsrm.mi.web.projekt.benutzer.ui;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;


@Controller
public class BenutzerController {

    // Logger
    Logger logger = LoggerFactory.getLogger(BenutzerController.class);

    // GET handler
    @GetMapping("/admin/benutzer/{loginname}")
    public String showUserForm(
            @PathVariable("loginname") String loginName,
            Model model) {

        logger.info("GET /admin/benutzer/{}", loginName);

        //Creating BenutzerFormular object here to add data
        BenutzerFormular benutzerFormular=new BenutzerFormular();


        model.addAttribute("loginname", loginName);
        model.addAttribute("formular",benutzerFormular);


        return "benutzer/bearbeiten";
    }


    //POST handler
    @PostMapping("/admin/benutzer/{loginname}")
    public String postUserForm(@PathVariable("loginname") String loginName,
    @ModelAttribute("formular") BenutzerFormular benutzerFormular,
    Model model) {



        logger.info("POST /admin/benutzer/{}", loginName);
        logger.info("Formular: {}", benutzerFormular);

        model.addAttribute("loginname",loginName);
        model.addAttribute("formular",benutzerFormular);


        return "benutzer/bearbeiten";
    }

}
