package org.vaadin.activiti.simpletravel.service.impl;

import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import org.vaadin.activiti.simpletravel.service.ValidationException;

public class ValidationUtil {

    public static void validateAndThrow(Validator validator, Object objectToValidate) {
        Set<ConstraintViolation<Object>> violations = validator.validate(objectToValidate);
        if (!violations.isEmpty()) {
            throw new ValidationException(violations);
        }
    }
}
