package org.vaadin.activiti.simpletravel.ui.forms;

import com.github.peholmst.mvp4vaadin.View;
import org.activiti.engine.form.StartFormData;
import org.activiti.engine.repository.ProcessDefinition;

public interface StartFormView extends View {
        
    void setStartFormData(StartFormData startFormData);
}
