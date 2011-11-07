package org.vaadin.activiti.simpletravel.domain.validation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy=ReturnsAfterDepartureValidator.class)
@Documented
public @interface ReturnsAfterDeparture {
    
    String message() default "{validator.returnsAfterDeparture}";
    
    Class<?>[] groups() default {};
        
    Class<? extends Payload>[] payload() default {};
    
}
