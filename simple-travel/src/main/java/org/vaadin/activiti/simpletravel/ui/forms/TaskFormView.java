package org.vaadin.activiti.simpletravel.ui.forms;

import com.github.peholmst.mvp4vaadin.View;
import java.util.Map;
import org.activiti.engine.form.TaskFormData;

public interface TaskFormView extends View {
    
    void setTaskFormData(TaskFormData taskFormData);
    
    void setProcessVariables(Map<String, Object> variables);    
    
}
