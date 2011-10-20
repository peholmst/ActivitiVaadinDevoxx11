package org.vaadin.activiti.simpletravel.ui.forms.impl;

import java.util.HashMap;
import org.activiti.engine.FormService;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.vaadin.activiti.simpletravel.ui.forms.TaskForm;
import org.vaadin.activiti.simpletravel.ui.forms.TaskFormView;

@Configurable
public class TaskFormMap {
 
    @Autowired
    private FormService formService;
    
    private final HashMap<String, Class<? extends TaskFormView>> formTypes = new HashMap<String, Class<? extends TaskFormView>>();
    
    public Class<? extends TaskFormView> get(Task task) {
        return formTypes.get(getFormKeyForTask(task));
    }
    
    public boolean containsKey(Task task) {
        return formTypes.containsKey(getFormKeyForTask(task));
    }
    
    public void put(TaskForm taskForm, Class<?> type) {
        formTypes.put(taskForm.formKey(), (Class<? extends TaskFormView>) type);
    }
    
    private String getFormKeyForTask(Task task) {
        return formService.getTaskFormData(task.getId()).getFormKey();        
    }
}
