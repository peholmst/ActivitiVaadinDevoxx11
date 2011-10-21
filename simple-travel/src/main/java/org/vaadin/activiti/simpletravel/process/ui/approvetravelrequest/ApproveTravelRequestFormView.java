package org.vaadin.activiti.simpletravel.process.ui.approvetravelrequest;

import org.vaadin.activiti.simpletravel.domain.TravelRequest;
import org.vaadin.activiti.simpletravel.ui.forms.TaskFormView;

public interface ApproveTravelRequestFormView extends TaskFormView {

    void setRequest(TravelRequest request);
}
