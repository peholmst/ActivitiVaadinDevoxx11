package org.vaadin.activiti.simpletravel.process.ui.approvetravelrequest.components;

import com.github.peholmst.mvp4vaadin.AbstractViewComponent;
import com.vaadin.ui.Component;
import java.util.Date;
import java.util.Map;
import org.activiti.engine.form.TaskFormData;
import org.springframework.beans.factory.annotation.Configurable;
import org.vaadin.activiti.simpletravel.process.ui.approvetravelrequest.ApproveTravelRequestFormPresenter;
import org.vaadin.activiti.simpletravel.process.ui.approvetravelrequest.ApproveTravelRequestFormView;
import org.vaadin.activiti.simpletravel.ui.forms.TaskForm;

@TaskForm(formKey="approveTravelRequest")
@Configurable
public class ApproveTravelRequestFormViewComponent extends AbstractViewComponent<ApproveTravelRequestFormView, ApproveTravelRequestFormPresenter> implements ApproveTravelRequestFormView {

    @Override
    protected Component createCompositionRoot() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setNameOfTraveler(String traveler) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setTravelTime(Date departureDate, Date returnDate) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setDestination(String name, String city, String country, String description) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setTaskFormData(TaskFormData taskFormData) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setProcessVariables(Map<String, Object> variables) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
}
