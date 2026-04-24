package de.hsrm.mi.web.projekt.benutzer.ui;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class BenutzerController {

    // Logger
    Logger logger = LoggerFactory.getLogger(BenutzerController.class);

    // GET handler
    @GetMapping("/admin/benutzer/{loginname}")
    public String showUserForm(
            @PathVariable String loginname,
            Model model) {

        logger.info("GET /admin/benutzer/{}", loginname);


        model.addAttribute("loginname", loginname);


        return "benutzer/bearbeiten";
    }
}
