package org.vaadin.activiti.simpletravel.process.ui;

import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;
import org.springframework.beans.factory.annotation.Configurable;
import org.vaadin.activiti.simpletravel.domain.TravelInvoice;
import org.vaadin.activiti.simpletravel.ui.forms.TaskForm;
import org.vaadin.activiti.simpletravel.ui.forms.TaskFormViewComponent;

@TaskForm(formKey = "payoutExpenses")
@Configurable
public class PayExpencesFormViewComponent extends TaskFormViewComponent<PayExpencesFormView, PayExpencesFormPresenter> implements PayExpencesFormView {

    private VerticalLayout layout;
    private TravelRequestViewerComponent requestViewer;
    private ExpensesViewerComponent expensesViewer;
    private Button expensesPaid;
    private Button cancel;

    @Override
    protected Component createCompositionRoot() {
        layout = new VerticalLayout();
        layout.setMargin(true);
        layout.setSpacing(true);
        layout.setSizeFull();
        setSizeFull();

        final Label header = new Label("Pay Expenses");
        header.addStyleName(Reindeer.LABEL_H1);
        layout.addComponent(header);

        requestViewer = new TravelRequestViewerComponent();
        layout.addComponent(requestViewer);

        expensesViewer = new ExpensesViewerComponent();
        expensesViewer.setSizeFull();
        layout.addComponent(expensesViewer);
        layout.setExpandRatio(expensesViewer, 1.0f);

        HorizontalLayout buttons = new HorizontalLayout();
        buttons.setSpacing(true);

        expensesPaid = new Button("Expenses have been paid", new Button.ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {
                expensesPaidClick();
            }
        });
        expensesPaid.setDisableOnClick(true);
        buttons.addComponent(expensesPaid);

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
    public void setInvoice(TravelInvoice invoice) {
        requestViewer.setRequest(invoice.getRequest());
        expensesViewer.setExpenses(invoice.getExpenses());
    }

    private void cancelClick() {
        getPresenter().cancel();
    }

    private void expensesPaidClick() {
        getPresenter().payExpenses();
    }
}
