package org.vaadin.activiti.simpletravel.ui.forms;

import java.util.Map;
import org.activiti.engine.form.TaskFormData;

public abstract class TaskFormViewComponent<V extends TaskFormView, P extends TaskFormPresenter<V>> extends AbstractFormViewComponent<V, P> implements TaskFormView {

    @Override
    public void setTaskFormData(TaskFormData taskFormData) {
        getPresenter().setTaskFormData(taskFormData);
    }

    @Override
    public void setProcessVariables(Map<String, Object> variables) {
        getPresenter().setProcessVariables(variables);
    }
}
