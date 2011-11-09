package org.vaadin.activiti.simpletravel.service.impl;

import java.util.Date;
import java.util.HashMap;
import javax.annotation.PostConstruct;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import org.activiti.engine.IdentityService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.identity.User;
import org.activiti.engine.impl.identity.Authentication;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.vaadin.activiti.simpletravel.domain.Decision;
import org.vaadin.activiti.simpletravel.domain.TravelRequest;
import org.vaadin.activiti.simpletravel.domain.TravelRequestDecision;
import org.vaadin.activiti.simpletravel.domain.repositories.TravelRequestRepository;
import org.vaadin.activiti.simpletravel.identity.Groups;
import org.vaadin.activiti.simpletravel.identity.RequireGroup;
import org.vaadin.activiti.simpletravel.service.TravelRequestService;

@Component
public class TravelRequestServiceImpl implements TravelRequestService {

    private ValidatorFactory validatorFactory;
    @Autowired
    protected TravelRequestRepository repository;
    @Autowired
    protected RuntimeService runtimeService;
    @Autowired
    protected TaskService taskService;
    @Autowired
    protected IdentityService identityService;

    @PostConstruct
    public void init() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
    }

    protected Validator getValidator() {
        return validatorFactory.getValidator();
    }

    @Override
    @Transactional
    @RequireGroup(Groups.GROUP_EMPLOYEES)
    public TravelRequest submitNewTravelRequest(TravelRequest request) {
        ValidationUtil.validateAndThrow(getValidator(), request);
        request.setRequesterUserId(Authentication.getAuthenticatedUserId());
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
        TravelRequest request = repository.findById(id);
        if (request != null) {
            request.setRequesterUserName(getFullNameOfUser(request.getRequesterUserId()));
        }
        return request;
    }
    
    private String getFullNameOfUser(String userId) {
        User user = identityService.createUserQuery().userId(userId).singleResult();
        if (user == null) {
            return null;
        } else {
            return String.format("%s %s", user.getFirstName(), user.getLastName());
        }
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
        ValidationUtil.validateAndThrow(getValidator(), decision);
        repository.save(request);
        final ProcessInstance processInstance = getProcessInstanceForRequest(request);
        final Task travelApprovalTask = getTravelApprovalTask(processInstance);
        final HashMap<String, Object> processVariables = new HashMap<String, Object>();
        taskService.complete(travelApprovalTask.getId(), processVariables);
    }
    
    private ProcessInstance getProcessInstanceForRequest(TravelRequest request) {
        return runtimeService.createProcessInstanceQuery().
                processInstanceBusinessKey(request.getId().toString(), "simple-travel").
                singleResult();
    }
    
    private Task getTravelApprovalTask(ProcessInstance processInstance) {
        return taskService.createTaskQuery().
                processInstanceId(processInstance.getId()).
                taskDefinitionKey("usertask1").
                singleResult();
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
    
    private Task getBookTicketsTask(ProcessInstance processInstance) {
        return taskService.createTaskQuery().
                processInstanceId(processInstance.getId()).
                taskDefinitionKey("usertask2").
                singleResult();
    }
    
}
