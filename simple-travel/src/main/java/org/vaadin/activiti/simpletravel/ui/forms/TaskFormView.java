package org.vaadin.activiti.simpletravel.ui.forms;

import java.util.Map;
import org.activiti.engine.form.TaskFormData;

public interface TaskFormView extends FormView {
    
    void setTaskFormData(TaskFormData taskFormData);
    
    void setProcessVariables(Map<String, Object> variables);    
    
}
