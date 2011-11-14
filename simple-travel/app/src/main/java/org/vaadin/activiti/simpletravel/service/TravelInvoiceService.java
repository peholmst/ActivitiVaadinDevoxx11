package org.vaadin.activiti.simpletravel.service;

import org.vaadin.activiti.simpletravel.domain.TravelInvoice;
import org.vaadin.activiti.simpletravel.domain.TravelRequest;

public interface TravelInvoiceService {
    
    TravelInvoice findInvoiceForRequest(TravelRequest request);

    TravelInvoice saveTravelInvoice(TravelInvoice invoice);
    
    TravelInvoice findTravelInvoiceByProcessInstanceId(String processInstanceId);

    void approveTravelInvoice(TravelInvoice invoice, String motivation);

    void rejectTravelInvoice(TravelInvoice invoice, String motivation);
    
    void payExpences(TravelInvoice invoice);    
}
