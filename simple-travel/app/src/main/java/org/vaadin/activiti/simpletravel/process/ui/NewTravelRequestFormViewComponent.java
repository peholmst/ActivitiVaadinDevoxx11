package org.vaadin.activiti.simpletravel.process.ui;

import com.vaadin.addon.beanvalidation.BeanValidationForm;
import com.vaadin.data.Validator.InvalidValueException;
import com.vaadin.data.util.BeanItem;
import com.vaadin.terminal.UserError;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;
import java.util.Arrays;
import java.util.Locale;
import org.springframework.beans.factory.annotation.Configurable;
import org.vaadin.activiti.simpletravel.domain.TravelRequest;
import org.vaadin.activiti.simpletravel.domain.validation.ValidationException;
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
        requestForm.setComponentError(new UserError(error.getMessage()));
        commit.setEnabled(true); // was disabled when clicked
    }

    @Override
    public void clearValidationError() {
        requestForm.setValidationVisible(false);
        requestForm.setComponentError(null);
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
        form.setFormFieldFactory(new TravelRequestFormFieldFactory());
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
