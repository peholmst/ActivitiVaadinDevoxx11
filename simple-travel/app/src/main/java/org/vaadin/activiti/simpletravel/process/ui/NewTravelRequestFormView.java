package org.vaadin.activiti.simpletravel.process.ui;

import org.vaadin.activiti.simpletravel.domain.TravelRequest;
import org.vaadin.activiti.simpletravel.ui.forms.StartFormView;

public interface NewTravelRequestFormView extends StartFormView {
 
    void setRequest(TravelRequest request);
        
}
