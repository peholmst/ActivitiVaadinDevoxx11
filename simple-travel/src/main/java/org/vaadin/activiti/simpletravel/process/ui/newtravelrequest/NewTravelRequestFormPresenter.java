package org.vaadin.activiti.simpletravel.process.ui.newtravelrequest;

import com.github.peholmst.mvp4vaadin.Presenter;
import org.activiti.engine.repository.ProcessDefinition;
import org.springframework.beans.factory.annotation.Configurable;

@Configurable
public class NewTravelRequestFormPresenter extends Presenter<NewTravelRequestFormView> {
    
    public void setProcessDefinition(ProcessDefinition pd) {
        
    }
}
