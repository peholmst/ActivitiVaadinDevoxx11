package org.vaadin.activiti.simpletravel.domain.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.apache.commons.lang.time.DateUtils;
import org.vaadin.activiti.simpletravel.domain.TravelRequest;

public class ReturnsAfterDepartureValidator implements ConstraintValidator<ReturnsAfterDeparture, TravelRequest> {

    @Override
    public void initialize(ReturnsAfterDeparture constraintAnnotation) {
    }

    @Override
    public boolean isValid(TravelRequest value, ConstraintValidatorContext context) {
        if (value.getDepartureDate() == null || value.getReturnDate() == null) {
            return false;
        }
        if (DateUtils.isSameDay(value.getDepartureDate(), value.getReturnDate())) {
            return true;
        } else {
            return value.getDepartureDate().before(value.getReturnDate());
        }
    }


    
}
