package org.vaadin.activiti.simpletravel.test;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.activiti.engine.FormService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.form.FormProperty;
import org.activiti.engine.form.StartFormData;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
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
@ContextConfiguration({ "classpath:application-context-process.xml",
		"classpath:test-application-context-process.xml" })
public class TravelProcessTest {

	@Autowired
	private RuntimeService runtimeService;

	@Autowired
	private FormService formService;

	@Autowired
	private TaskService taskService;

	@Autowired
	private RepositoryService repositoryService;

	@Autowired
	@Rule
	public ActivitiRule activitiSpringRule;

	@Test
	@Deployment(resources = "process/simple-travel.bpmn20.xml")
	public void testTravelProcessRejectRequest() {
		// Get hold of the ID of our latest process
		String processDefinitionId = repositoryService
				.createProcessDefinitionQuery()
				.processDefinitionKey("simple-travel").latestVersion()
				.singleResult().getId();

		// Create start-properties
		Map<String, Object> properties = getStartProperties();
		StartFormData startFormData = formService
				.getStartFormData(processDefinitionId);

		// Check if the form-properties match the one's we're about to use
		Assert.assertEquals(properties.size(), startFormData
				.getFormProperties().size());
		for (FormProperty prop : startFormData.getFormProperties()) {
			Assert.assertTrue(properties.containsKey(prop.getId()));
		}

		// Start the actual process
		ProcessInstance processInstance = runtimeService
				.startProcessInstanceByKey("simple-travel");
		Assert.assertNotNull(processInstance);

		// Check if a task is available, queued for a manager
		Task theTask = taskService.createTaskQuery()
				.taskCandidateGroup("management").singleResult();
		Assert.assertNotNull(theTask);
		Assert.assertEquals(processInstance.getId(),
				theTask.getProcessInstanceId());

		// Reject the request
		Map<String, Object> rejectProperties = getRejectProperties();
		// TODO: implement

	}

	protected Map<String, Object> getStartProperties() {
		Map<String, Object> properties = new HashMap<String, Object>();
		properties.put("destinationCountry", "Belgium");
		properties.put("destinationCity", "Antwerp");
		properties.put("destinationName", "Devoxx 2011");
		properties.put("destinationDescription",
				"The best Java-conference in the whole universe.");
		properties.put("departureDate", new Date());
		properties.put("returnDate", new Date());

		return properties;
	}

	protected Map<String, Object> getRejectProperties() {
		Map<String, Object> properties = new HashMap<String, Object>();
		properties.put("travelApproved", false);
		properties.put("approvalMotivation", "Test Rejection");
		return properties;
	}
}