package org.vaadin.activiti.simpletravel.service.impl;

import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.vaadin.activiti.simpletravel.domain.TravelInvoice;
import org.vaadin.activiti.simpletravel.domain.repositories.TravelInvoiceRepository;
import org.vaadin.activiti.simpletravel.domain.validation.ValidationUtil;
import org.vaadin.activiti.simpletravel.identity.Groups;
import org.vaadin.activiti.simpletravel.identity.RequireGroup;
import org.vaadin.activiti.simpletravel.service.TravelInvoiceService;

@Service
public class TravelInvoiceServiceImpl extends AbstractServiceImpl implements TravelInvoiceService {

    @Autowired
    protected TravelInvoiceRepository repository;    
    
    
    @Override
    @Transactional
    @RequireGroup(Groups.GROUP_EMPLOYEES)
    public TravelInvoice submitNewTravelInvoice(TravelInvoice invoice) {
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
    public TravelInvoice updateRejectedTravelInvoice(TravelInvoice invoice) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    @Transactional
    @RequireGroup(Groups.GROUP_EMPLOYEES)
    public TravelInvoice findTravelInvoiceById(long id) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    @Transactional
    @RequireGroup(Groups.GROUP_EMPLOYEES)
    public TravelInvoice findTravelInvoiceByProcessInstanceId(String processInstanceId) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    @Transactional
    @RequireGroup(Groups.GROUP_MANAGERS)
    public void approveTravelInvoice(TravelInvoice invoice, String motivation) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    @Transactional
    @RequireGroup(Groups.GROUP_MANAGERS)
    public void denyTravelInvoice(TravelInvoice invoice, String motivation) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    @Transactional
    @RequireGroup(Groups.GROUP_PAYROLLADMINS)
    public void payExpences(TravelInvoice invoice) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    private ProcessInstance getProcessInstanceForInvoice(TravelInvoice invoice) {
        return runtimeService.createProcessInstanceQuery().
                processInstanceBusinessKey(invoice.getRequest().getId().toString(), "simple-travel").
                singleResult();
    }
    
    private Task getEnterExpencesTask(ProcessInstance processInstance) {
        return findTaskByDefinitionKey(processInstance, "enterExpenses");
    }    

    private Task getApproveExpencesTask(ProcessInstance processInstance) {
        return findTaskByDefinitionKey(processInstance, "approveExpenses");
    }    
    
    private Task getPayExpencesTask(ProcessInstance processInstance) {
        return findTaskByDefinitionKey(processInstance, "payout");
    }        
    
}
