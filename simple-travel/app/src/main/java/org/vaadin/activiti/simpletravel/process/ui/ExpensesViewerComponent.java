package org.vaadin.activiti.simpletravel.process.ui;

import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;
import java.util.Collection;
import org.vaadin.activiti.simpletravel.domain.Expense;

public class ExpensesViewerComponent extends CustomComponent {
    
    protected VerticalLayout layout;
    
    protected Table table;
    
    protected BeanItemContainer<Expense> container = new BeanItemContainer<Expense>(Expense.class);

    public ExpensesViewerComponent() {
        layout = new VerticalLayout();
        layout.setSpacing(true);
        setCompositionRoot(layout);
        
        table = new Table();
        table.setContainerDataSource(container);
        table.setVisibleColumns(new String[] {"description", "quantity", "price"});
        layout.addComponent(table);
        layout.setExpandRatio(table, 1.0f);
    }
    
    public void setExpenses(Collection<Expense> expenses) {
        container.removeAllItems();
        container.addAll(expenses);
    }
    
    public Collection<Expense> getExpenses() {
        return container.getItemIds();
    }

    @Override
    public void setSizeFull() {
        super.setSizeFull();
        table.setSizeFull();
    }

    @Override
    public void setSizeUndefined() {
        super.setSizeUndefined();
        table.setSizeUndefined();
    }
    
}
