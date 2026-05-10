package de.hsrm.mi.web.projekt.benutzer.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class GutesPasswortValidator implements ConstraintValidator<GutesPasswort, String> {

    @Override
    public boolean isValid(String passwort, ConstraintValidatorContext context) {

        if (passwort == null || passwort.isEmpty()) {
            return true;
        }

        String lower = passwort.toLowerCase();
        return !lower.contains("42") && !lower.contains("zweiundvierzig");
    }
}
