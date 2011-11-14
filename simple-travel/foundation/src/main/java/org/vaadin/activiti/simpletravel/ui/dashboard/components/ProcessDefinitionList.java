package org.vaadin.activiti.simpletravel.ui.dashboard.components;

import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.Panel;
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
                final Component processComponent = createProcessDefinitionComponent(pd.getName(), pd.getKey());
                addComponent(processComponent);
            }
        }
    }

    private Component createProcessDefinitionComponent(final String name, final String processDefinitionKey) {
        final Button startProcess = new Button(name);
        startProcess.addStyleName(Reindeer.BUTTON_LINK);
                startProcess.addListener(new Button.ClickListener() {

            @Override
            public void buttonClick(Button.ClickEvent event) {
                presenter.startProcessInstance(processDefinitionKey);
            }
        });
        
        return startProcess;
    }
}
