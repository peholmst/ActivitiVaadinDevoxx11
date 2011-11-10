package org.vaadin.activiti.simpletravel.process.ui;

import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.vaadin.activiti.simpletravel.domain.TravelRequest;
import org.vaadin.activiti.simpletravel.domain.validation.ValidationException;
import org.vaadin.activiti.simpletravel.service.TravelRequestService;
import org.vaadin.activiti.simpletravel.ui.forms.StartFormPresenter;

@Configurable
public class NewTravelRequestFormPresenter extends StartFormPresenter<NewTravelRequestFormView> {

    @Autowired
    protected transient TravelRequestService travelRequestService;

    @Override
    public void init() {
        TravelRequest request = new TravelRequest();
        request.setDepartureDate(new Date());
        request.setReturnDate(new Date());
        getView().setRequest(request);
    }

    public void submitTravelRequest(TravelRequest request) {
        try {
            travelRequestService.submitNewTravelRequest(request);
            closeForm();
        } catch (ValidationException validationError) {
            getView().setValidationError(validationError);
        }
    }

}
