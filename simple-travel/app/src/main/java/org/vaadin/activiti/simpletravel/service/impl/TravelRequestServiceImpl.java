package org.vaadin.activiti.simpletravel.service.impl;

import java.util.Date;
import java.util.HashMap;
import org.activiti.engine.identity.User;
import org.activiti.engine.impl.identity.Authentication;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.vaadin.activiti.simpletravel.domain.Decision;
import org.vaadin.activiti.simpletravel.domain.TravelRequest;
import org.vaadin.activiti.simpletravel.domain.TravelRequestDecision;
import org.vaadin.activiti.simpletravel.domain.repositories.TravelRequestRepository;
import org.vaadin.activiti.simpletravel.domain.validation.ValidationUtil;
import org.vaadin.activiti.simpletravel.identity.Groups;
import org.vaadin.activiti.simpletravel.identity.RequireGroup;
import org.vaadin.activiti.simpletravel.service.TravelRequestService;

@Service
public class TravelRequestServiceImpl extends AbstractServiceImpl implements TravelRequestService {
    
    @Autowired
    protected TravelRequestRepository repository;

    @Override
    @Transactional
    @RequireGroup(Groups.GROUP_EMPLOYEES)
    public TravelRequest submitNewTravelRequest(TravelRequest request) {
        ValidationUtil.validateAndThrow(validator, request);
        request.setRequesterUserId(Authentication.getAuthenticatedUserId());
        request.setRequesterUserName(getFullNameOfUser(request.getRequesterUserId()));
        request = repository.save(request);
        String businessKey = request.getId().toString();
        
        // Also set the request as process-variable
        HashMap<String, Object> variables = new HashMap<String, Object>();
        variables.put("request", request);
        
        runtimeService.startProcessInstanceByKey("simple-travel", businessKey, variables); 
        return request;
    }

    @Override
    @Transactional
    @RequireGroup(Groups.GROUP_EMPLOYEES)
    public TravelRequest findTravelRequestById(long id) {
        return repository.findById(id);
    }
    
    @Override
    @Transactional
    @RequireGroup(Groups.GROUP_MANAGERS)
    public void approveTravelRequest(TravelRequest request, String motivation) {
        final TravelRequestDecision decision = new TravelRequestDecision(Decision.APPROVED, 
                motivation, Authentication.getAuthenticatedUserId(), new Date());
        setDecisionAndSave(request, decision);
    }

    @Override
    @Transactional
    @RequireGroup(Groups.GROUP_MANAGERS)
    public void denyTravelRequest(TravelRequest request, String motivation) {
        final TravelRequestDecision decision = new TravelRequestDecision(Decision.DENIED, 
                motivation, Authentication.getAuthenticatedUserId(), new Date());
        setDecisionAndSave(request, decision);
    }

    private void setDecisionAndSave(TravelRequest request, TravelRequestDecision decision) {
        request.setDecision(decision);
        ValidationUtil.validateAndThrow(validator, decision);
        repository.save(request);
        final ProcessInstance processInstance = getProcessInstanceForRequest(request);
        final Task travelApprovalTask = getTravelApprovalTask(processInstance);
        final HashMap<String, Object> processVariables = new HashMap<String, Object>();
        taskService.complete(travelApprovalTask.getId(), processVariables);
    }
    
    @Override
    @Transactional
    @RequireGroup(Groups.GROUP_EMPLOYEES)
    public TravelRequest findTravelRequestByProcessInstanceId(String processInstanceId) {
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
        if (processInstance == null || processInstance.getBusinessKey() == null) {
            return null;
        }
        return findTravelRequestById(Long.parseLong(processInstance.getBusinessKey()));
    }

    @Override
    @Transactional
    @RequireGroup(Groups.GROUP_SECRETARIES)
    public void bookTickets(TravelRequest request) {
        final ProcessInstance processInstance = getProcessInstanceForRequest(request);
        final Task bookTicketsTask = getBookTicketsTask(processInstance);
        taskService.complete(bookTicketsTask.getId());
    }
    
    private ProcessInstance getProcessInstanceForRequest(TravelRequest request) {
        return runtimeService.createProcessInstanceQuery().
                processInstanceBusinessKey(request.getId().toString(), "simple-travel").
                singleResult();
    }
    
    private Task getTravelApprovalTask(ProcessInstance processInstance) {
        return findTaskByDefinitionKey(processInstance, "usertask1");
    }    
    
    private Task getBookTicketsTask(ProcessInstance processInstance) {
        return findTaskByDefinitionKey(processInstance, "usertask2");
    }
    
}
