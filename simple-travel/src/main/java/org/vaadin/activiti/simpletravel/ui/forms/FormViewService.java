package org.vaadin.activiti.simpletravel.ui.forms;

import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.task.Task;

public interface FormViewService {

    boolean hasStartFormView(ProcessDefinition processDefinition);
        
    StartFormView getStartFormView(ProcessDefinition processDefinition);
        
    boolean hasTaskFormView(Task task);
    
    TaskFormView getTaskFormView(Task task);    
    
}
