package org.vaadin.activiti.simpletravel.domain.repositories;

import org.vaadin.activiti.simpletravel.domain.TravelInvoice;
import org.vaadin.activiti.simpletravel.domain.TravelRequest;

public interface TravelInvoiceRepository extends Repository<TravelInvoice> {
    
    TravelInvoice findByTravelRequest(TravelRequest request);
    
}
