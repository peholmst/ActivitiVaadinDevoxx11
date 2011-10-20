package org.vaadin.activiti.simpletravel.process.ui.approvetravelrequest;

import java.util.Date;
import org.vaadin.activiti.simpletravel.ui.forms.TaskFormView;

public interface ApproveTravelRequestFormView extends TaskFormView {

    void setNameOfTraveler(String traveler);
    
    void setTravelTime(Date departureDate, Date returnDate);
    
    void setDestination(String name, String city, String country, String description);
    
}
