package org.vaadin.activiti.simpletravel.ui.forms.impl;

import java.util.HashMap;
import org.activiti.engine.repository.ProcessDefinition;
import org.vaadin.activiti.simpletravel.ui.forms.StartFormView;

public class StartFormMap {

    private HashMap<String, Class<? extends StartFormView>> formTypes = new HashMap<String, Class<? extends StartFormView>>();
    
    public Class<? extends StartFormView> get(ProcessDefinition pd) {
        return get(pd.getId());
    }

    public Class<? extends StartFormView> get(String processDefinitionId) {
        return formTypes.get(processDefinitionId);
    }
    
    public boolean containsKey(ProcessDefinition pd) {
        return containsKey(pd.getId());
    }

    public boolean containsKey(String processDefinitionId) {
        return formTypes.containsKey(processDefinitionId);
    }
    
    public void put(ProcessDefinition pd, Class<?> type) {
        put(pd.getId(), type);
    }
    
    public void put(String processDefinitionId, Class<?> type) {
        formTypes.put(processDefinitionId, (Class<? extends StartFormView>) type);
    }    
}
