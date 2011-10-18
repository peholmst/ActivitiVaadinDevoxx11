package org.vaadin.activiti.simpletravel.ui.dashboard.components;

import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.Panel;
import com.vaadin.ui.PopupView;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;
import java.util.List;
import org.activiti.engine.repository.ProcessDefinition;

public class ProcessDefinitionList extends Panel {

    private List<ProcessDefinition> processDefinitions;

    public ProcessDefinitionList() {
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
                final PopupView popupView = new PopupView(pd.getName(), createProcessDefinitionView(pd));
                addComponent(popupView);
            }
        }
    }

    private Component createProcessDefinitionView(ProcessDefinition pd) {
        final VerticalLayout layout = new VerticalLayout();
        layout.setMargin(true);
        layout.setSizeUndefined();
        final Button startProcess = new Button("Start Process");
        layout.addComponent(startProcess);
        return layout;
    }
}
