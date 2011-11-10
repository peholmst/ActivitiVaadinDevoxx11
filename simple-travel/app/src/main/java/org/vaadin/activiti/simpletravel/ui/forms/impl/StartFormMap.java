package org.vaadin.activiti.simpletravel.ui.forms.impl;

import java.util.HashMap;
import org.activiti.engine.repository.ProcessDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vaadin.activiti.simpletravel.ui.forms.StartFormView;

public class StartFormMap {
    
    private final Logger logger = LoggerFactory.getLogger(getClass());    

    private final HashMap<String, Class<? extends StartFormView>> formTypes = new HashMap<String, Class<? extends StartFormView>>();
    
    public Class<? extends StartFormView> get(ProcessDefinition pd) {
        return get(pd.getKey());
    }

    protected Class<? extends StartFormView> get(String processDefinitionKey) {
        logger.info("Retrieving start form for process definition '{}'", processDefinitionKey);
        return formTypes.get(processDefinitionKey);
    }
    
    public boolean containsKey(ProcessDefinition pd) {
        return containsKey(pd.getKey());
    }

    protected boolean containsKey(String processDefinitionKey) {
        logger.info("Checking if process definition '{}' has a start form", processDefinitionKey);
        return formTypes.containsKey(processDefinitionKey);
    }
    
    public void put(ProcessDefinition pd, Class<?> type) {
        put(pd.getKey(), type);
    }
    
    public void put(String processDefinitionKey, Class<?> type) {
        logger.info("Storing start form {} for process definition '{}'", type.getName(), processDefinitionKey);
        formTypes.put(processDefinitionKey, (Class<? extends StartFormView>) type);
    }    
}
