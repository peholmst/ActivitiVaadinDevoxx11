package org.vaadin.activiti.simpletravel.process.ui;

import com.vaadin.terminal.UserError;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;
import org.springframework.beans.factory.annotation.Configurable;
import org.vaadin.activiti.simpletravel.domain.TravelInvoice;
import org.vaadin.activiti.simpletravel.domain.validation.ValidationException;
import org.vaadin.activiti.simpletravel.ui.forms.TaskForm;
import org.vaadin.activiti.simpletravel.ui.forms.TaskFormViewComponent;

@TaskForm(formKey = "enterExpenses")
@Configurable
public class EnterExpensesFormViewComponent extends TaskFormViewComponent<EnterExpensesFormView, EnterExpensesFormPresenter> implements EnterExpensesFormView {

    private VerticalLayout layout;
    private TravelRequestViewerComponent requestViewer;
    private ExpensesEditorComponent expensesEditor;
    private Button save;
    private Button cancel;

    @Override
    protected Component createCompositionRoot() {
        layout = new VerticalLayout();
        layout.setMargin(true);
        layout.setSpacing(true);
        layout.setSizeFull();
        setSizeFull();

        final Label header = new Label("Enter Expenses");
        header.addStyleName(Reindeer.LABEL_H1);
        layout.addComponent(header);

        requestViewer = new TravelRequestViewerComponent();
        layout.addComponent(requestViewer);

        expensesEditor = new ExpensesEditorComponent();
        expensesEditor.setSizeFull();
        layout.addComponent(expensesEditor);
        layout.setExpandRatio(expensesEditor, 1.0f);

        HorizontalLayout buttons = new HorizontalLayout();
        buttons.setSpacing(true);

        save = new Button("Save", new Button.ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {
                saveClick();
            }
        });
        save.setDisableOnClick(true);
        buttons.addComponent(save);

        cancel = new Button("Cancel", new Button.ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {
                cancelClick();
            }
        });
        cancel.addStyleName(Reindeer.BUTTON_LINK);
        cancel.setDisableOnClick(true);
        buttons.addComponent(cancel);
        buttons.setComponentAlignment(cancel, Alignment.MIDDLE_LEFT);

        layout.addComponent(buttons);

        return layout;
    }

    @Override
    public void setInvoice(TravelInvoice invoice) {
        requestViewer.setRequest(invoice.getRequest());
        expensesEditor.setExpenses(invoice.getExpenses());
    }

    private void saveClick() {
        getPresenter().submitTravelInvoice(expensesEditor.getExpenses());
    }

    private void cancelClick() {
        getPresenter().cancel();
    }

    @Override
    public void setValidationError(ValidationException error) {
        expensesEditor.setComponentError(new UserError(error.getMessage()));
        save.setEnabled(true);
        cancel.setEnabled(true);
    }

    @Override
    public void clearValidationError() {
        expensesEditor.setComponentError(null);
    }
}
