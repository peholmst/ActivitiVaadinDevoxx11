package org.vaadin.activiti.simpletravel.process.ui;

import org.vaadin.activiti.simpletravel.domain.TravelInvoice;
import org.vaadin.activiti.simpletravel.ui.forms.TaskFormView;

public interface EnterExpensesFormView extends TaskFormView {
    
    void setInvoice(TravelInvoice invoice);    
}
