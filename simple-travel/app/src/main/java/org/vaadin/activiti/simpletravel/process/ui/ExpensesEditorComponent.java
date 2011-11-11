package org.vaadin.activiti.simpletravel.process.ui;

import com.vaadin.addon.beanvalidation.BeanValidationForm;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.DefaultFieldFactory;
import com.vaadin.ui.Field;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;
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
        form.setFormFieldFactory(new DefaultFieldFactory() {

            @Override
            public Field createField(Item item, Object propertyId, Component uiContext) {
                final Field f = super.createField(item, propertyId, uiContext);
                if (f instanceof TextField) {
                    ((TextField) f).setNullRepresentation("");
                }
                if ("quantity".equals(propertyId) || "price".equals(propertyId)) {
                    f.setWidth("50px");
                }
                return f;
            }
        });
        form.setLayout(formLayout);
        form.setWriteThrough(true);
        form.setValidationVisible(true);
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
        buttons.setWidth("100%");
        buttons.setSpacing(true);
        add = new Button("Add", new Button.ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {
                addItem();
            }
        });
        buttons.addComponent(add);
        buttons.setExpandRatio(add, 1.0f);
        buttons.setComponentAlignment(add, Alignment.MIDDLE_RIGHT);
        
        remove = new Button("Remove", new Button.ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {
                removeItem();
            }
        });
        buttons.addComponent(remove);
        buttons.setComponentAlignment(remove, Alignment.MIDDLE_RIGHT);
        
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
            table.setValue(null);
        }
    }
    
    protected void selectionChanged() {
        final Expense selectedExpense = (Expense) table.getValue();
        if (selectedExpense != null) {
            final Item item = table.getItem(selectedExpense);
            form.setItemDataSource(item);
            form.setVisibleItemProperties(new String[] {"description", "quantity", "price"});            
        }
        form.setVisible(selectedExpense != null);
        remove.setEnabled(selectedExpense != null);
    }
}
