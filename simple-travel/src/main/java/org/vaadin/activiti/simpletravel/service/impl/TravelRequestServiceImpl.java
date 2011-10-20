package org.vaadin.activiti.simpletravel.service.impl;

import javax.annotation.PostConstruct;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.impl.identity.Authentication;
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
        // TODO invoke activiti
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
        final TravelRequestDecision decision = new TravelRequestDecision(Decision.APPROVED, motivation, Authentication.getAuthenticatedUserId());
        setDecisionAndSave(request, decision);
    }

    @Override
    @Transactional
    @RequireGroup(Groups.GROUP_MANAGERS)
    public void denyTravelRequest(TravelRequest request, String motivation) {
        final TravelRequestDecision decision = new TravelRequestDecision(Decision.DENIED, motivation, Authentication.getAuthenticatedUserId());
        setDecisionAndSave(request, decision);
    }

    private void setDecisionAndSave(TravelRequest request, TravelRequestDecision decision) {
        request.setDecision(decision);
        ValidationUtil.validateAndThrow(getValidator(), request);
        repository.save(request);
        // TODO invoke activiti
    }
}
