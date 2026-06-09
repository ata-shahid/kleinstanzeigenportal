package de.hsrm.mi.web.projekt.anzeige.api;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class AnzeigeNotFoundException extends RuntimeException {
    public AnzeigeNotFoundException(String message) {
        super(message);
    }
}
