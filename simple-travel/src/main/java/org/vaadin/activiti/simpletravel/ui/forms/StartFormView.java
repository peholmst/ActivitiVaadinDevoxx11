package org.vaadin.activiti.simpletravel.ui.forms;

import com.github.peholmst.mvp4vaadin.View;
import org.activiti.engine.form.StartFormData;

public interface StartFormView extends View {
        
    void setStartFormData(StartFormData startFormData);
}
