package org.vaadin.activiti.simpletravel.process.ui;

import org.activiti.engine.form.TaskFormData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.vaadin.activiti.simpletravel.domain.TravelRequest;
import org.vaadin.activiti.simpletravel.service.TravelRequestService;
import org.vaadin.activiti.simpletravel.ui.forms.TaskFormPresenter;

@Configurable
public class BookTicketsFormPresenter extends TaskFormPresenter<BookTicketsFormView> {

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
    
    public void bookTickets() {
        service.bookTickets(request);
        closeForm();
    }
    
}
