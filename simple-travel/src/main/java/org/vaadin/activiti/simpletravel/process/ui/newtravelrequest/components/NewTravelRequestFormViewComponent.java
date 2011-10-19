package org.vaadin.activiti.simpletravel.process.ui.newtravelrequest.components;

import com.github.peholmst.mvp4vaadin.AbstractViewComponent;
import com.vaadin.data.Item;
import com.vaadin.data.Validator.InvalidValueException;
import com.vaadin.data.util.BeanItem;
import com.vaadin.ui.AbstractTextField;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.DateField;
import com.vaadin.ui.DefaultFieldFactory;
import com.vaadin.ui.Field;
import com.vaadin.ui.Form;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;
import java.util.Arrays;
import org.activiti.engine.repository.ProcessDefinition;
import org.springframework.beans.factory.annotation.Configurable;
import org.vaadin.activiti.simpletravel.process.ui.newtravelrequest.NewTravelRequest;
import org.vaadin.activiti.simpletravel.process.ui.newtravelrequest.NewTravelRequestFormPresenter;
import org.vaadin.activiti.simpletravel.process.ui.newtravelrequest.NewTravelRequestFormView;
import org.vaadin.activiti.simpletravel.ui.forms.StartForm;

@StartForm(processDefinitionKey = "simple-travel")
@Configurable
public class NewTravelRequestFormViewComponent extends AbstractViewComponent<NewTravelRequestFormView, NewTravelRequestFormPresenter> implements NewTravelRequestFormView {

    private Form requestForm;
    
    private BeanItem<NewTravelRequest> request;
    
    private VerticalLayout layout;
    
    private Button commit;
    
    private Button cancel;

    @Override
    public void setRequest(NewTravelRequest request) {
        this.request = new BeanItem<NewTravelRequest>(request);
        requestForm.setItemDataSource(this.request, Arrays.asList(NewTravelRequest.PROP_DEPARTURE_DATE, 
                NewTravelRequest.PROP_RETURN_DATE, 
                NewTravelRequest.PROP_DESTINATION_NAME, 
                NewTravelRequest.PROP_DESTINATION_COUNTRY, 
                NewTravelRequest.PROP_DESTINATION_CITY,
                NewTravelRequest.PROP_DESTINATION_DESCRIPTION));
        
    }
    
    private class RequestFormFieldFactory extends DefaultFieldFactory {

        @Override
        public Field createField(Item item, Object propertyId, Component uiContext) {
            Field f;
            if (propertyId.equals(NewTravelRequest.PROP_DESTINATION_DESCRIPTION)) {
                f = new TextArea("Description");
                ((TextArea) f).setRows(10);
                f.setWidth("500px");
            } else {
                f = super.createField(item, propertyId, uiContext);            
            }
            
            if (propertyId.equals(NewTravelRequest.PROP_DEPARTURE_DATE) || propertyId.equals(NewTravelRequest.PROP_RETURN_DATE)) {
                ((DateField) f).setResolution(DateField.RESOLUTION_DAY);
                f.setRequired(true);
                f.setRequiredError(f.getCaption() + " is a required field");
            } else if (propertyId.equals(NewTravelRequest.PROP_DESTINATION_NAME)) {
                f.setRequired(true);
                f.setRequiredError(f.getCaption() + " is a required field");
                f.setWidth("400px");
            }
            
            if (f instanceof AbstractTextField) {
                ((AbstractTextField) f).setNullRepresentation("");
            }
            
            return f;
        }                        
    }
    
    @Override
    protected Component createCompositionRoot() {        
        layout = new VerticalLayout();
        layout.setMargin(true);
        layout.setSpacing(true);
        
        final Label header = new Label("Request New Trip");
        header.addStyleName(Reindeer.LABEL_H1);
        layout.addComponent(header);
        
        requestForm = createForm();
        layout.addComponent(requestForm);
                
        return layout;
    }
    
    private Form createForm() {
        final Form form = new Form();        
        form.setWriteThrough(false);
        form.setFormFieldFactory(new RequestFormFieldFactory());
        form.setImmediate(true);
        
        final HorizontalLayout buttons = new HorizontalLayout();
        buttons.setSpacing(true);
        
        commit = new Button("Commit", new Button.ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {
                commitFormAndStartProcess();
            }
        });
        commit.setDisableOnClick(true);
        buttons.addComponent(commit);
        buttons.setComponentAlignment(commit, Alignment.MIDDLE_LEFT);
        
        cancel = new Button("Cancel", new Button.ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {
                getPresenter().cancel();
            }
        });
        cancel.addStyleName(Reindeer.BUTTON_LINK);
        cancel.setDisableOnClick(true);
        buttons.addComponent(cancel);
        buttons.setComponentAlignment(cancel, Alignment.MIDDLE_LEFT);

        form.setFooter(buttons);
                
        return form;
    }

    private void commitFormAndStartProcess() {
        try {
            requestForm.commit();
            getPresenter().startProcess(request.getBean());
        } catch (InvalidValueException e) {
            // The form will handle the errors
            commit.setEnabled(true);
        }
    }
        
    @Override
    public void setProcessDefinition(ProcessDefinition processDefinition) {
        getPresenter().setProcessDefinition(processDefinition);
    }
}
