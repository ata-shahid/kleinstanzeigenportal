package de.hsrm.mi.web.projekt.benutzer.validators;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = GutesPasswortValidator.class)
public @interface GutesPasswort {

    String message() default "{benutzer.fehler.passwortungeeignet}";

    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
