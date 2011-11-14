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
import org.vaadin.activiti.simpletravel.domain.TravelInvoice;
import org.vaadin.activiti.simpletravel.domain.validation.ValidationException;
import org.vaadin.activiti.simpletravel.ui.forms.TaskForm;
import org.vaadin.activiti.simpletravel.ui.forms.TaskFormViewComponent;

@Configurable
@TaskForm(formKey="approveExpenses")
public class ApproveExpensesViewComponent extends TaskFormViewComponent<ApproveExpensesView, ApproveExpensesPresenter> implements ApproveExpensesView {

    private VerticalLayout layout;
    
    private TravelRequestViewerComponent requestViewer;
    
    private ExpensesViewerComponent expensesViewer;
    
    private TextArea motivation;
    
    private Button approve;
    
    private Button reject;
    
    private Button cancel;
    
    @Override
    protected Component createCompositionRoot() {
        layout = new VerticalLayout();
        layout.setMargin(true);
        layout.setSpacing(true);
        
        Label header = new Label("Approve Expenses");
        header.addStyleName(Reindeer.LABEL_H1);
        layout.addComponent(layout);
        
        requestViewer = new TravelRequestViewerComponent();
        layout.addComponent(layout);
        
        expensesViewer = new ExpensesViewerComponent();
        expensesViewer.setWidth("400px");
        expensesViewer.setHeight("300px");
        layout.addComponent(layout);
        
        motivation = new TextArea("Motivation");
        motivation.setRows(10);
        motivation.setWidth("300px");
        layout.addComponent(motivation);
        
        HorizontalLayout buttons = new HorizontalLayout();
        buttons.setSpacing(true);
        layout.addComponent(buttons);
        
        approve = new Button("Accept", new Button.ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {
                getPresenter().approveInvoice((String) motivation.getValue());
            }
        });
        buttons.addComponent(approve);
        
        reject = new Button("Reject", new Button.ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {
                getPresenter().rejectInvoice((String) motivation.getValue());
            }
        });
        buttons.addComponent(reject);
        
        cancel = new Button("Cancel", new Button.ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {
                getPresenter().cancel();
            }
        });
        buttons.addComponent(cancel);
        
        return layout;
    }

    @Override
    public void setInvoice(TravelInvoice invoice) {
        requestViewer.setRequest(invoice.getRequest());
        expensesViewer.setExpenses(invoice.getExpenses());
    }

    @Override
    public void setValidationError(ValidationException error) {
        super.setValidationError(error);
        motivation.setComponentError(new UserError(error.getMessage()));
    }
    
    
}
