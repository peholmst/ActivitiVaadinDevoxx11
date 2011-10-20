package org.vaadin.activiti.simpletravel.ui.forms;

import com.github.peholmst.mvp4vaadin.Presenter;
import java.util.Map;
import org.activiti.engine.form.TaskFormData;

public abstract class TaskFormPresenter<V extends TaskFormView> extends Presenter<V> {

    private TaskFormData taskFormData;
    private Map<String, Object> variables;

    public void setTaskFormData(TaskFormData taskFormData) {
        this.taskFormData = taskFormData;
    }

    public TaskFormData getTaskFormData() {
        return taskFormData;
    }

    public void setProcessVariables(Map<String, Object> variables) {
        this.variables = variables;
    }

    public Map<String, Object> getVariables() {
        return variables;
    }
    
    public void cancel() {
        closeForm();
    }
    
    protected void closeForm() {
        fireViewEvent(new FormClosedEvent(getView()));
    }       
}
