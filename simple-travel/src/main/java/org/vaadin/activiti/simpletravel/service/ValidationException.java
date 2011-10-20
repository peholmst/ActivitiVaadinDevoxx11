package org.vaadin.activiti.simpletravel.service;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import javax.validation.ConstraintViolation;

public class ValidationException extends RuntimeException {

    private final HashSet<ConstraintViolation<Object>> violations;

    public ValidationException(Collection<ConstraintViolation<Object>> violations) {
        this.violations = new HashSet<ConstraintViolation<Object>>(violations);
    }

    public Set<ConstraintViolation<Object>> getViolations() {
        return Collections.unmodifiableSet(violations);
    }
}
