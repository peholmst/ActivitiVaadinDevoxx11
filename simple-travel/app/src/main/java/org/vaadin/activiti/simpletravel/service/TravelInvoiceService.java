package org.vaadin.activiti.simpletravel.service;

import org.vaadin.activiti.simpletravel.domain.TravelInvoice;

public interface TravelInvoiceService {
    
    TravelInvoice submitNewTravelInvoice(TravelInvoice invoice);

    TravelInvoice updateRejectedTravelInvoice(TravelInvoice invoice);
    
    TravelInvoice findTravelInvoiceById(long id);

    TravelInvoice findTravelInvoiceByProcessInstanceId(String processInstanceId);

    void approveTravelInvoice(TravelInvoice invoice, String motivation);

    void denyTravelInvoice(TravelInvoice invoice, String motivation);

    void payExpences(TravelInvoice invoice);    
}
