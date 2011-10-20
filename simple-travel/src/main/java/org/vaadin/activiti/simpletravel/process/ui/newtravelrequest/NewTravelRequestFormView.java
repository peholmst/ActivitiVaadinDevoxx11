package org.vaadin.activiti.simpletravel.process.ui.newtravelrequest;

import org.vaadin.activiti.simpletravel.domain.TravelRequest;
import org.vaadin.activiti.simpletravel.service.ValidationException;
import org.vaadin.activiti.simpletravel.ui.forms.StartFormView;

public interface NewTravelRequestFormView extends StartFormView {
 
    void setRequest(TravelRequest request);
    
    void setValidationError(ValidationException error);    
    
}
