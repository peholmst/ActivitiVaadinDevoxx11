package org.vaadin.activiti.simpletravel.process.ui.approvetravelrequest;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.activiti.engine.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.vaadin.activiti.simpletravel.ui.forms.TaskFormPresenter;

@Configurable
public class ApproveTravelRequestFormPresenter extends TaskFormPresenter<ApproveTravelRequestFormView> {
    
    @Autowired
    protected transient TaskService taskService;
    
    private String taskId;
    
    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    @Override
    public void setProcessVariables(Map<String, Object> variables) {
        super.setProcessVariables(variables);
        updateView();
    }        
    
    private void updateView() {
        getView().setNameOfTraveler((String) getVariables().get("initiator"));
        getView().setTravelTime((Date) getVariables().get("departureDate"), (Date) getVariables().get("returnDate"));
        getView().setDestination((String) getVariables().get("destinationName"), 
                (String) getVariables().get("destinationCity"), 
                (String) getVariables().get("destinationCountry"), 
                (String) getVariables().get("destinationDescription"));
    }
    
    public void approve(String motivation) {
        taskService.complete(taskId, createVariables(true, motivation));
        closeForm();
    }
    
    public void reject(String motivation) {
        taskService.complete(taskId, createVariables(false, motivation));        
        closeForm();
    }
    
    private Map<String, Object> createVariables(boolean approved, String motivation) {
        HashMap<String, Object> variables = new HashMap<String, Object>();
        variables.put("approveTravel", approved);
        variables.put("approvalMotivation", motivation);
        return variables;
    }
}
