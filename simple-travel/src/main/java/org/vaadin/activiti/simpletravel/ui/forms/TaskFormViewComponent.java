package org.vaadin.activiti.simpletravel.ui.forms;

import com.github.peholmst.mvp4vaadin.AbstractViewComponent;
import java.util.Map;
import org.activiti.engine.form.TaskFormData;

public abstract class TaskFormViewComponent<V extends TaskFormView, P extends TaskFormPresenter<V>> extends AbstractViewComponent<V, P> implements TaskFormView {

    @Override
    public void setTaskFormData(TaskFormData taskFormData) {
        getPresenter().setTaskFormData(taskFormData);
    }

    @Override
    public void setProcessVariables(Map<String, Object> variables) {
        getPresenter().setProcessVariables(variables);
    }
}
