package de.hsrm.mi.web.projekt.benutzer.ui;

public class BenutzerException extends RuntimeException {
    public BenutzerException(String message) {
        super(message);
    }

    public BenutzerException(String message, Throwable cause) {
        super(message, cause);
    }
}
