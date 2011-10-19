package org.vaadin.activiti.simpletravel.process.ui.newtravelrequest;

import com.github.peholmst.mvp4vaadin.Presenter;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.repository.ProcessDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.vaadin.activiti.simpletravel.ui.forms.FormClosedEvent;

@Configurable
public class NewTravelRequestFormPresenter extends Presenter<NewTravelRequestFormView> {
    
    @Autowired
    protected transient RuntimeService runtimeService;
    
    private String processDefinitionKey;
    
    public void setProcessDefinition(ProcessDefinition pd) {
        processDefinitionKey = pd.getKey();
    }
    
    public void startProcess(NewTravelRequest request) {
        runtimeService.startProcessInstanceByKey(processDefinitionKey, request.getValuesAsMap());
        closeForm();
    }
    
    public void cancel() {
        closeForm();
    }
    
    private void closeForm() {
        fireViewEvent(new FormClosedEvent(getView()));        
    }
}
