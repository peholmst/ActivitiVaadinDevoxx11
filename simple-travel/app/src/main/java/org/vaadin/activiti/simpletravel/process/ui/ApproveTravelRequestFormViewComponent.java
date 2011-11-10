package org.vaadin.activiti.simpletravel.process.ui;

import com.vaadin.terminal.UserError;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;
import org.springframework.beans.factory.annotation.Configurable;
import org.vaadin.activiti.simpletravel.domain.TravelRequest;
import org.vaadin.activiti.simpletravel.domain.validation.ValidationException;
import org.vaadin.activiti.simpletravel.ui.forms.TaskForm;
import org.vaadin.activiti.simpletravel.ui.forms.TaskFormViewComponent;

@TaskForm(formKey = "approveTravelRequest")
@Configurable
public class ApproveTravelRequestFormViewComponent extends TaskFormViewComponent<ApproveTravelRequestFormView, ApproveTravelRequestFormPresenter> implements ApproveTravelRequestFormView {

    private VerticalLayout layout;
    private TextArea motivation;
    private TravelRequestViewerComponent requestViewer;
    private Button approve;
    private Button deny;
    private Button cancel;

    @Override
    protected Component createCompositionRoot() {
        layout = new VerticalLayout();
        layout.setMargin(true);
        layout.setSpacing(true);

        final Label header = new Label("Approve Trip Request");
        header.addStyleName(Reindeer.LABEL_H1);
        layout.addComponent(header);

        requestViewer = new TravelRequestViewerComponent();
        layout.addComponent(requestViewer);

        motivation = new TextArea("Motivation");
        motivation.setRequired(true);
        motivation.setRows(10);
        motivation.setWidth("300px");
        layout.addComponent(motivation);

        HorizontalLayout buttons = new HorizontalLayout();
        buttons.setSpacing(true);

        approve = new Button("Approve", new Button.ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {
                approveClick();
            }
        });
        approve.setDisableOnClick(true);
        buttons.addComponent(approve);

        deny = new Button("Deny", new Button.ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {
                denyClick();
            }
        });
        deny.setDisableOnClick(true);
        buttons.addComponent(deny);

        cancel = new Button("Cancel", new Button.ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {
                cancelClick();
            }
        });
        cancel.addStyleName(Reindeer.BUTTON_LINK);
        cancel.setDisableOnClick(true);
        buttons.addComponent(cancel);

        layout.addComponent(buttons);

        return layout;
    }

    @Override
    public void setRequest(TravelRequest request) {
        requestViewer.setRequest(request);
    }

    private void approveClick() {
        getPresenter().approve((String) motivation.getValue());
    }

    private void denyClick() {
        getPresenter().deny((String) motivation.getValue());
    }

    private void cancelClick() {
        getPresenter().cancel();
    }

    @Override
    public void setValidationError(ValidationException error) {
        motivation.setComponentError(new UserError(error.getMessage()));
        // enable buttons that were disabled when clicked
        approve.setEnabled(true); 
        deny.setEnabled(true);
    }

    @Override
    public void clearValidationError() {
        motivation.setComponentError(null);
    }
            
}
