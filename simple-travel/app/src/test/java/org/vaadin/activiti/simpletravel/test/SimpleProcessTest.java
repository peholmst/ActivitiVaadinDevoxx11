package org.vaadin.activiti.simpletravel.test;

import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.test.ActivitiRule;
import org.activiti.engine.test.Deployment;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:application-context-process.xml", "classpath:test-application-context-process.xml"})
public class SimpleProcessTest {
  
  @Autowired
  private RuntimeService runtimeService;
  
  @Autowired
  private TaskService taskService;
  
  @Autowired
  @Rule
  public ActivitiRule activitiSpringRule;
  
  @Test
  @Deployment(resources="process/oneTaskProcess.bpmn20.xml")
  public void simpleProcessTest() {
    ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("oneTaskProcess");
    Assert.assertNotNull(processInstance);
    
    // Check if a task is available for the given process
    Assert.assertEquals(1, taskService.createTaskQuery().processInstanceId(processInstance.getId()).count());
  }
}      