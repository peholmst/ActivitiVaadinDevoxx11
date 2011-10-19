package org.vaadin.activiti.simpletravel.process.ui.newtravelrequest;

import com.github.peholmst.mvp4vaadin.Presenter;
import java.util.Date;
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

    @Override
    public void init() {
        NewTravelRequest request = new NewTravelRequest();
        request.setDepartureDate(new Date());
        getView().setRequest(request);
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
