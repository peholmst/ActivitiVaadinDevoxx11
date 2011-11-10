package org.vaadin.activiti.simpletravel.service.impl;

import javax.validation.Validator;
import org.activiti.engine.IdentityService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class AbstractServiceImpl {

    @Autowired
    protected Validator validator;
    @Autowired
    protected RuntimeService runtimeService;
    @Autowired
    protected TaskService taskService;
    @Autowired
    protected IdentityService identityService;

    protected Task findTaskByDefinitionKey(ProcessInstance processInstance, String definitionKey) {
        return taskService.createTaskQuery().
                processInstanceId(processInstance.getId()).
                taskDefinitionKey(definitionKey).
                singleResult();
    }
}
