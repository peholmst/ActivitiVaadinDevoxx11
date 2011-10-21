package org.vaadin.activiti.simpletravel.process.ui.approvetravelrequest;

import org.activiti.engine.form.TaskFormData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.vaadin.activiti.simpletravel.domain.TravelRequest;
import org.vaadin.activiti.simpletravel.service.TravelRequestService;
import org.vaadin.activiti.simpletravel.service.ValidationException;
import org.vaadin.activiti.simpletravel.ui.forms.TaskFormPresenter;

@Configurable
public class ApproveTravelRequestFormPresenter extends TaskFormPresenter<ApproveTravelRequestFormView> {
    
    @Autowired
    protected transient TravelRequestService service;
    
    private TravelRequest request;

    @Override
    public void setTaskFormData(TaskFormData taskFormData) {
        super.setTaskFormData(taskFormData);
        request = service.findTravelRequestByProcessInstanceId(taskFormData.getTask().getProcessInstanceId());
        if (request == null) {
            throw new IllegalStateException("No request found for task");
        }
        getView().setRequest(request);
    }        
            
    public void approve(String motivation) {
        try {
            service.approveTravelRequest(request, motivation);
            closeForm();
        } catch (ValidationException validationError) {
            getView().setValidationError(validationError);
        }
    }
    
    public void deny(String motivation) {
        try {
            service.denyTravelRequest(request, motivation);
            closeForm();
        } catch (ValidationException validationError) {
            getView().setValidationError(validationError);
        }
    }
}
