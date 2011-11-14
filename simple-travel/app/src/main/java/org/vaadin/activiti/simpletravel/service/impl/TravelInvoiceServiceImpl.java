package org.vaadin.activiti.simpletravel.service.impl;

import java.util.Date;
import java.util.HashMap;
import org.activiti.engine.impl.identity.Authentication;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.vaadin.activiti.simpletravel.domain.Decision;
import org.vaadin.activiti.simpletravel.domain.TravelInvoice;
import org.vaadin.activiti.simpletravel.domain.TravelInvoiceDecision;
import org.vaadin.activiti.simpletravel.domain.TravelRequest;
import org.vaadin.activiti.simpletravel.domain.repositories.TravelInvoiceRepository;
import org.vaadin.activiti.simpletravel.domain.validation.ValidationUtil;
import org.vaadin.activiti.simpletravel.identity.Groups;
import org.vaadin.activiti.simpletravel.identity.RequireGroup;
import org.vaadin.activiti.simpletravel.service.TravelInvoiceService;
import org.vaadin.activiti.simpletravel.service.TravelRequestService;

@Service
public class TravelInvoiceServiceImpl extends AbstractServiceImpl implements TravelInvoiceService {

    @Autowired
    protected TravelInvoiceRepository repository;
    @Autowired
    protected TravelRequestService requestService;

    @Override
    @Transactional
    @RequireGroup(Groups.GROUP_EMPLOYEES)
    public TravelInvoice findInvoiceForRequest(TravelRequest request) {
        return repository.findByTravelRequest(request);
    }

    @Override
    @Transactional
    @RequireGroup(Groups.GROUP_EMPLOYEES)
    public TravelInvoice saveTravelInvoice(TravelInvoice invoice) {
        ValidationUtil.validateAndThrow(validator, invoice);
        invoice = repository.save(invoice);
        final ProcessInstance processInstance = getProcessInstanceForInvoice(invoice);
        final Task task = getEnterExpencesTask(processInstance);
        taskService.complete(task.getId());
        return invoice;
    }

    @Override
    @Transactional
    @RequireGroup(Groups.GROUP_EMPLOYEES)
    public TravelInvoice findTravelInvoiceByProcessInstanceId(String processInstanceId) {
        TravelRequest request = requestService.findTravelRequestByProcessInstanceId(processInstanceId);
        if (request == null) {
            return null;
        } else {
            return findInvoiceForRequest(request);
        }
    }

    @Override
    @Transactional
    @RequireGroup(Groups.GROUP_PAYROLLADMINS)
    public void payExpences(TravelInvoice invoice) {
        ValidationUtil.validateAndThrow(validator, invoice);
        final ProcessInstance processInstance = getProcessInstanceForInvoice(invoice);
        final Task task = getPayExpencesTask(processInstance);
        invoice.setPaid(true);
        repository.save(invoice);
        taskService.complete(task.getId());
    }

    private ProcessInstance getProcessInstanceForInvoice(TravelInvoice invoice) {
        return runtimeService.createProcessInstanceQuery().
                processInstanceBusinessKey(invoice.getRequest().getId().toString(), "simple-travel").
                singleResult();
    }

    private Task getEnterExpencesTask(ProcessInstance processInstance) {
        return findTaskByDefinitionKey(processInstance, "enterExpenses");
    }

    private Task getPayExpencesTask(ProcessInstance processInstance) {
        return findTaskByDefinitionKey(processInstance, "payout");
    }    
    
    @Override
    @Transactional
    @RequireGroup(Groups.GROUP_MANAGERS)
    public void approveTravelInvoice(TravelInvoice invoice, String motivation) {
        final TravelInvoiceDecision decision = new TravelInvoiceDecision(Decision.APPROVED, motivation, Authentication.getAuthenticatedUserId(), new Date());
        setDecisionAndSave(invoice, decision);
    }

    @Override
    @Transactional
    @RequireGroup(Groups.GROUP_MANAGERS)
    public void rejectTravelInvoice(TravelInvoice invoice, String motivation) {
        final TravelInvoiceDecision decision = new TravelInvoiceDecision(Decision.DENIED, motivation, Authentication.getAuthenticatedUserId(), new Date());
        setDecisionAndSave(invoice, decision);
    }
    
    private void setDecisionAndSave(TravelInvoice invoice, TravelInvoiceDecision decision) {
        invoice.setDecision(decision);
        ValidationUtil.validateAndThrow(validator, decision);
        repository.save(invoice);
        final ProcessInstance processInstance = getProcessInstanceForInvoice(invoice);
        final Task expensesApprovalTask = getExpensesApprovalTask(processInstance);
        final HashMap<String, Object> processVariables = new HashMap<String, Object>();
        taskService.complete(expensesApprovalTask.getId(), processVariables);
    }    
    
    private Task getExpensesApprovalTask(ProcessInstance processInstance) {
        return findTaskByDefinitionKey(processInstance, "approveExpenses");
    }    
    
}
