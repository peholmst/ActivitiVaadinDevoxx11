package org.vaadin.activiti.simpletravel.ui.dashboard.components;

import com.vaadin.ui.Button;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.PopupView;
import com.vaadin.ui.PopupView.PopupVisibilityEvent;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;
import java.util.List;
import org.activiti.engine.repository.ProcessDefinition;
import org.vaadin.activiti.simpletravel.ui.dashboard.DashboardPresenter;

public class ProcessDefinitionList extends Panel {

    private List<ProcessDefinition> processDefinitions;

    private final DashboardPresenter presenter;
    
    public ProcessDefinitionList(DashboardPresenter presenter) {
        this.presenter = presenter;
        setStyleName(Reindeer.PANEL_LIGHT);
    }  
    
    public void setProcessDefinitions(List<ProcessDefinition> processDefinitions) {
        this.processDefinitions = processDefinitions;
        updateComponent();
    }

    public List<ProcessDefinition> getProcessDefinitions() {
        return processDefinitions;
    }

    private void updateComponent() {
        removeAllComponents();
        if (processDefinitions != null) {
            for (ProcessDefinition pd : processDefinitions) {
                final PopupView popupView = createProcessDefinitionPopup(pd.getName(), pd.getKey());
                addComponent(popupView);
            }
        }
    }

    private PopupView createProcessDefinitionPopup(final String name, final String processDefinitionKey) {
        final VerticalLayout layout = new VerticalLayout();
        layout.setMargin(true);
        layout.setSpacing(true);
        layout.setSizeUndefined();
        
        final Label label = new Label(name);
        label.setSizeUndefined();
        layout.addComponent(label);
        
        final Button startProcess = new Button("Start new instance");
        startProcess.setDisableOnClick(true);
        startProcess.addStyleName(Reindeer.BUTTON_SMALL);
        layout.addComponent(startProcess);
        
        final PopupView popup = new PopupView(name, layout);
        popup.addListener(new PopupView.PopupVisibilityListener() {

            @Override
            public void popupVisibilityChange(PopupVisibilityEvent event) {
                if (event.isPopupVisible()) {
                    startProcess.setEnabled(true);
                }
            }
        });
        
        startProcess.addListener(new Button.ClickListener() {

            @Override
            public void buttonClick(Button.ClickEvent event) {
                presenter.startProcessInstance(processDefinitionKey);
                popup.setPopupVisible(false);
            }
        });
        
        return popup;
    }
}
