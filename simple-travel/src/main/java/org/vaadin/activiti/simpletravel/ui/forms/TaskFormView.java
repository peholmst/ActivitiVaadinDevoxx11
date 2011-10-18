package org.vaadin.activiti.simpletravel.ui.forms;

import com.github.peholmst.mvp4vaadin.View;
import org.activiti.engine.task.Task;

public interface TaskFormView extends View {
    
    void setTask(Task task);
    
}
