package org.vaadin.activiti.simpletravel.process.ui.newtravelrequest.components;

import com.vaadin.addon.beanvalidation.BeanValidationForm;
import com.vaadin.data.Item;
import com.vaadin.data.Validator.InvalidValueException;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.ui.AbstractTextField;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.DateField;
import com.vaadin.ui.DefaultFieldFactory;
import com.vaadin.ui.Field;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window.Notification;
import com.vaadin.ui.themes.Reindeer;
import java.util.Arrays;
import java.util.Locale;
import org.springframework.beans.factory.annotation.Configurable;
import org.vaadin.activiti.simpletravel.domain.Country;
import org.vaadin.activiti.simpletravel.domain.TravelRequest;
import org.vaadin.activiti.simpletravel.process.ui.newtravelrequest.NewTravelRequestFormPresenter;
import org.vaadin.activiti.simpletravel.process.ui.newtravelrequest.NewTravelRequestFormView;
import org.vaadin.activiti.simpletravel.service.ValidationException;
import org.vaadin.activiti.simpletravel.ui.forms.StartForm;
import org.vaadin.activiti.simpletravel.ui.forms.StartFormViewComponent;

@StartForm(processDefinitionKey = "simple-travel")
@Configurable
public class NewTravelRequestFormViewComponent extends StartFormViewComponent<NewTravelRequestFormView, NewTravelRequestFormPresenter> implements NewTravelRequestFormView {

    private BeanValidationForm<TravelRequest> requestForm;
    private BeanItem<TravelRequest> request;
    private VerticalLayout layout;
    private Button commit;
    private Button cancel;

    @Override
    public void setRequest(TravelRequest request) {
        this.request = new BeanItem<TravelRequest>(request);
        requestForm.setItemDataSource(this.request, Arrays.asList(TravelRequest.PROP_DEPARTURE_DATE,
                TravelRequest.PROP_RETURN_DATE,
                TravelRequest.PROP_COUNTRY,
                TravelRequest.PROP_DESCRIPTION));

    }

    @Override
    public void setValidationError(ValidationException error) {
        // TODO Implement me!
        getWindow().showNotification("A validation error has occured in the backend.", "TODO: update the UI to emphasize the invalid fields.", Notification.TYPE_WARNING_MESSAGE);
        commit.setEnabled(true); // was disabled when clicked
    }

    private class RequestFormFieldFactory extends DefaultFieldFactory {

        @Override
        public Field createField(Item item, Object propertyId, Component uiContext) {
            Field f;
            if (propertyId.equals(TravelRequest.PROP_COUNTRY)) {
                ComboBox c = new ComboBox("Country", new BeanItemContainer<Country>(Country.class, Arrays.asList(Country.values())));
                c.setItemCaptionPropertyId("displayName");
                c.setNullSelectionAllowed(false);
                c.setInputPrompt("Please select...");
                f = c;
            } else if (propertyId.equals(TravelRequest.PROP_DESCRIPTION)) {
                TextArea t = new TextArea("Description");
                t.setWidth("300px");
                t.setRows(10);
                f = t;
            } else {
                f = super.createField(item, propertyId, uiContext);
            }

            if (propertyId.equals(TravelRequest.PROP_DEPARTURE_DATE) || propertyId.equals(TravelRequest.PROP_RETURN_DATE)) {
                ((DateField) f).setResolution(DateField.RESOLUTION_DAY);
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

    private BeanValidationForm<TravelRequest> createForm() {
        final BeanValidationForm<TravelRequest> form = new BeanValidationForm<TravelRequest>(TravelRequest.class);
        form.setLocale(Locale.US);
        form.setWriteThrough(false);
        form.setFormFieldFactory(new RequestFormFieldFactory());
        form.setImmediate(true);

        final HorizontalLayout buttons = new HorizontalLayout();
        buttons.setSpacing(true);

        commit = new Button("Commit", new Button.ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {
                submit();
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

    private void submit() {
        try {
            requestForm.commit();
            getPresenter().submitTravelRequest(request.getBean());
        } catch (InvalidValueException e) {
            // The form will handle the errors
            commit.setEnabled(true); // was disabled when clicked
        }
    }
}
