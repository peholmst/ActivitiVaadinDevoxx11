package org.vaadin.activiti.simpletravel.process.ui;

import java.util.Collection;
import org.activiti.engine.form.TaskFormData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.vaadin.activiti.simpletravel.domain.Expense;
import org.vaadin.activiti.simpletravel.domain.TravelInvoice;
import org.vaadin.activiti.simpletravel.domain.TravelRequest;
import org.vaadin.activiti.simpletravel.domain.validation.ValidationException;
import org.vaadin.activiti.simpletravel.service.TravelInvoiceService;
import org.vaadin.activiti.simpletravel.service.TravelRequestService;
import org.vaadin.activiti.simpletravel.ui.forms.TaskFormPresenter;

@Configurable
public class EnterExpensesFormPresenter extends TaskFormPresenter<EnterExpensesFormView> {

    @Autowired
    protected transient TravelInvoiceService invoiceService;
    @Autowired
    protected transient TravelRequestService requestService;
    private TravelInvoice invoice;
    private TravelRequest request;

    @Override
    public void setTaskFormData(TaskFormData taskFormData) {
        super.setTaskFormData(taskFormData);
        request = requestService.findTravelRequestByProcessInstanceId(taskFormData.getTask().getProcessInstanceId());
        if (request == null) {
            throw new IllegalStateException("No request found for task");
        }
        invoice = invoiceService.findInvoiceForRequest(request);
        if (invoice == null) {
            invoice = new TravelInvoice(request);
        }
        getView().setInvoice(invoice);
    }

    public void submitTravelInvoice(Collection<Expense> expenses) {
        invoice.setExpenses(expenses);
        try {
            invoiceService.saveTravelInvoice(invoice);
            closeForm();
        } catch (ValidationException validationError) {
            getView().setValidationError(validationError);
        }
    }
}
