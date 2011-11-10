package org.vaadin.activiti.simpletravel.process.ui;

import org.vaadin.activiti.simpletravel.domain.TravelRequest;
import org.vaadin.activiti.simpletravel.ui.forms.TaskFormView;

public interface BookTicketsFormView extends TaskFormView {
    
    void setRequest(TravelRequest request);
}
