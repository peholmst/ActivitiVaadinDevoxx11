package org.vaadin.activiti.simpletravel.process.ui;

import org.activiti.engine.form.TaskFormData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.vaadin.activiti.simpletravel.domain.TravelInvoice;
import org.vaadin.activiti.simpletravel.domain.validation.ValidationException;
import org.vaadin.activiti.simpletravel.service.TravelInvoiceService;
import org.vaadin.activiti.simpletravel.ui.forms.TaskFormPresenter;

@Configurable
public class ApproveExpensesPresenter extends TaskFormPresenter<ApproveExpensesView> {

    @Autowired
    protected transient TravelInvoiceService service;
    private TravelInvoice invoice;

    @Override
    public void setTaskFormData(TaskFormData taskFormData) {
        super.setTaskFormData(taskFormData);
        invoice = service.findTravelInvoiceByProcessInstanceId(taskFormData.getTask().getProcessInstanceId());
        if (invoice == null) {
            throw new IllegalStateException("No invoice found for task");
        }
        getView().setInvoice(invoice);
    }
        
    
    public void approveInvoice(String motivation) {
        try {
            service.approveTravelInvoice(invoice, motivation);
            closeForm();
        } catch (ValidationException e) {
            getView().setValidationError(e);
        }
    }
    
    public void rejectInvoice(String motivation) {
        try {
            service.rejectTravelInvoice(invoice, motivation);
            closeForm();
        } catch (ValidationException e) {
            getView().setValidationError(e);
        }
    }
    
}
