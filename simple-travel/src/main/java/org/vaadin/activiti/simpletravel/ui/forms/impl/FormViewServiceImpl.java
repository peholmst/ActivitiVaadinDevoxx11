package org.vaadin.activiti.simpletravel.ui.forms.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import javax.annotation.PostConstruct;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.task.Task;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.vaadin.activiti.simpletravel.ui.forms.FormViewService;
import org.vaadin.activiti.simpletravel.ui.forms.StartForm;
import org.vaadin.activiti.simpletravel.ui.forms.StartFormView;
import org.vaadin.activiti.simpletravel.ui.forms.TaskFormView;

public class FormViewServiceImpl implements FormViewService {
    
    private Reflections reflections;
    
    private String[] packagesToScan;
    
    private final Logger logger = LoggerFactory.getLogger(getClass());    
    
    private StartFormMap startForms = new StartFormMap();
    
    @PostConstruct
    public void setUp() {
        initReflections();
        lookupStartForms();
        lookupTaskForms();
    }
    
    private void initReflections() {
        ConfigurationBuilder builder = new ConfigurationBuilder();
        for (String packageName : packagesToScan) {
            logger.info("Scanning package {} for forms", packageName);
            builder.addUrls(ClasspathHelper.forPackage(packageName));
        }
        builder.setScanners(new SubTypesScanner(), new TypeAnnotationsScanner());
        reflections = new Reflections(builder);        
    }
    
    private void lookupStartForms() {
        logger.info("Looking for start forms");
        Set<Class<?>> types = reflections.getTypesAnnotatedWith(StartForm.class);
        for (Class<?> type : types) {            
            final String processDefinitionId = type.getAnnotation(StartForm.class).processDefinitionId();
            if (StartFormView.class.isAssignableFrom(type)) {
                logger.info("Found start form {} for process definition '{}'", type.getName(), processDefinitionId);
                startForms.put(processDefinitionId, type);
            } else {
                logger.warn("Ignoring start form {}: StartFormView interface not implemented", type.getName());
            }
        }       
    }
    
    private void lookupTaskForms() {
        logger.info("Looking for task forms");
        // TODO Implement me!
    }
    
    @Required
    public void setPackagesToScan(String[] packagesToScan) {
        this.packagesToScan = packagesToScan;
    }
    
    @Override
    public boolean hasStartFormView(ProcessDefinition processDefinition) {
        return startForms.containsKey(processDefinition);
    }

    @Override
    public StartFormView getStartFormView(ProcessDefinition processDefinition) {
        final Class<? extends StartFormView> formType = startForms.get(processDefinition);
        if (formType == null) {
            throw new IllegalArgumentException("No start form found");
        }
        final StartFormView formView = create(formType);
        formView.setProcessDefinition(processDefinition);
        return formView;
    }

    @Override
    public boolean hasTaskFormView(Task task) {
        // TODO implement me
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public TaskFormView getTaskFormView(Task task) {
        // TODO implement me
        throw new UnsupportedOperationException("Not supported yet.");
    }
 
    private StartFormView create(Class<? extends StartFormView> type) {
        try {
            return type.newInstance();
        } catch (Exception e) {
            logger.error("Could not create an instance of {}", type);
            throw new RuntimeException(e);
        }
    }
}
