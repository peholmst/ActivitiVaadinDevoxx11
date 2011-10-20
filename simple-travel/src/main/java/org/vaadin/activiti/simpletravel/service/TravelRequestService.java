package org.vaadin.activiti.simpletravel.service;

import org.vaadin.activiti.simpletravel.domain.TravelRequest;

public interface TravelRequestService {

    TravelRequest submitNewTravelRequest(TravelRequest request);
    
    TravelRequest findTravelRequestById(long id);
    
    void approveTravelRequest(TravelRequest request, String motivation);
    
    void denyTravelRequest(TravelRequest request, String motivation);
    
}
