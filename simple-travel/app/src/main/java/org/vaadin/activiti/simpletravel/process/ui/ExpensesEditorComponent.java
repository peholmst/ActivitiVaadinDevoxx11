package org.vaadin.activiti.simpletravel.process.ui;

import com.vaadin.addon.beanvalidation.BeanValidationForm;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.util.BeanItem;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.HorizontalLayout;
import org.vaadin.activiti.simpletravel.domain.Expense;

public class ExpensesEditorComponent extends ExpensesViewerComponent {

    protected BeanValidationForm<Expense> form;
    
    protected Button add;
    
    protected Button remove;

    public ExpensesEditorComponent() {
        super();
        final HorizontalLayout formLayout = new HorizontalLayout();
        formLayout.setSpacing(true);
        
        form = new BeanValidationForm<Expense>(Expense.class);
        form.setLayout(formLayout);
        form.setWriteThrough(true);
        form.setImmediate(true);
        
        table.setSelectable(true);
        table.setImmediate(true);
        table.addListener(new Property.ValueChangeListener() {

            @Override
            public void valueChange(ValueChangeEvent event) {
                selectionChanged();
            }
        });
        
        layout.addComponent(form);
        
        final HorizontalLayout buttons = new HorizontalLayout();
        buttons.setSpacing(true);
        add = new Button("Add", new Button.ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {
                addItem();
            }
        });
        buttons.addComponent(add);
        
        remove = new Button("Remove", new Button.ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {
                removeItem();
            }
        });
        buttons.addComponent(remove);
        
        layout.addComponent(buttons);
    }
    
    protected void addItem() {
        Expense expense = new Expense();
        container.addBean(expense);
        table.setValue(expense);
    }
    
    protected void removeItem() {
        final Expense selectedExpense = (Expense) table.getValue();
        if (selectedExpense != null) {
            container.removeItem(selectedExpense);
        }
    }
    
    protected void selectionChanged() {
        final Expense selectedExpense = (Expense) table.getValue();
        if (selectedExpense != null) {
            BeanItem<Expense> item = container.getItem(selectedExpense);
            form.setItemDataSource(item);
            form.setVisibleItemProperties(new String[] {"description", "quantity", "price"});            
        }
        form.setVisible(selectedExpense != null);
        remove.setEnabled(selectedExpense != null);
    }
}
