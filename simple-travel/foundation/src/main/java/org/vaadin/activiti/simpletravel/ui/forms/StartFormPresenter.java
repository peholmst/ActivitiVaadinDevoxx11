package org.vaadin.activiti.simpletravel.ui.forms;

import com.github.peholmst.mvp4vaadin.Presenter;
import org.activiti.engine.form.StartFormData;

public abstract class StartFormPresenter<V extends StartFormView> extends Presenter<V> {

    private StartFormData startFormData;
    
    public void setStartFormData(StartFormData startFormData) {
        this.startFormData = startFormData;
    }

    public StartFormData getStartFormData() {
        return startFormData;
    }       

    public void cancel() {
        closeForm();
    }
    
    protected void closeForm() {
        fireViewEvent(new FormClosedEvent(getView()));
    }       
    
}
