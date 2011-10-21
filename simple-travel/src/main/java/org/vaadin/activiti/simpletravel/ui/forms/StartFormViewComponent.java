package org.vaadin.activiti.simpletravel.ui.forms;

import org.activiti.engine.form.StartFormData;

public abstract class StartFormViewComponent<V extends StartFormView, P extends StartFormPresenter<V>> extends AbstractFormViewComponent<V, P> implements StartFormView {

    @Override
    public void setStartFormData(StartFormData startFormData) {
        getPresenter().setStartFormData(startFormData);
    }
    
}
