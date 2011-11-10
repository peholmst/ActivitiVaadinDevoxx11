package org.vaadin.activiti.simpletravel.ui.forms.impl;

import com.github.peholmst.mvp4vaadin.View;
import java.util.Set;
import javax.annotation.PostConstruct;
import javax.swing.text.html.FormView;
import org.activiti.engine.FormService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.task.Task;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.vaadin.activiti.simpletravel.ui.forms.FormViewService;
import org.vaadin.activiti.simpletravel.ui.forms.StartForm;
import org.vaadin.activiti.simpletravel.ui.forms.StartFormView;
import org.vaadin.activiti.simpletravel.ui.forms.TaskForm;
import org.vaadin.activiti.simpletravel.ui.forms.TaskFormView;

public class FormViewServiceImpl implements FormViewService {
    
    private Reflections reflections;
    
    private String[] packagesToScan;
    
    private final Logger logger = LoggerFactory.getLogger(getClass());    
    
    private StartFormMap startForms = new StartFormMap();
    
    private TaskFormMap taskForms = new TaskFormMap();
    
    @Autowired
    protected FormService formService;
    
    @Autowired
    protected RuntimeService runtimeService;
    
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
            final String processDefinitionKey = type.getAnnotation(StartForm.class).processDefinitionKey();
            if (StartFormView.class.isAssignableFrom(type)) {
                logger.info("Found start form {} for process definition '{}'", type.getName(), processDefinitionKey);
                startForms.put(processDefinitionKey, type);
            } else {
                logger.warn("Ignoring start form {}: StartFormView interface not implemented", type.getName());
            }
        }       
    }
    
    private void lookupTaskForms() {
        logger.info("Looking for task forms");
        Set<Class<?>> types = reflections.getTypesAnnotatedWith(TaskForm.class);
        for (Class<?> type : types) {            
            final TaskForm taskFormInfo = type.getAnnotation(TaskForm.class);
            if (TaskFormView.class.isAssignableFrom(type)) {
                logger.info("Found task form {} for form key '{}'", type.getName(), taskFormInfo.formKey());
                taskForms.put(taskFormInfo, type);
            } else {
                logger.warn("Ignoring task form {}: TaskFormView interface not implemented", type.getName());
            }
        }       
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
        final StartFormView formView = createFormView(formType);
        formView.setStartFormData(formService.getStartFormData(processDefinition.getId()));
        return formView;
    }

    @Override
    public boolean hasTaskFormView(Task task) {
        return taskForms.containsKey(task);
    }

    @Override
    public TaskFormView getTaskFormView(Task task) {
        final Class<? extends TaskFormView> formType = taskForms.get(task);
        if (formType == null) {
            throw new IllegalArgumentException("No task form found");
        }
        final TaskFormView formView = createFormView(formType);
        formView.setTaskFormData(formService.getTaskFormData(task.getId()));
        formView.setProcessVariables(runtimeService.getVariables(task.getProcessInstanceId()));
        return formView;
    }
 
    private <T extends View> T createFormView(Class<? extends T> type) {
        try {
            return type.newInstance();
        } catch (Exception e) {
            logger.error("Could not create an instance of {}", type);
            throw new RuntimeException(e);
        }
    }
}
