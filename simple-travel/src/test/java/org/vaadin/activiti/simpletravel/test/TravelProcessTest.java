package org.vaadin.activiti.simpletravel.test;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import org.activiti.engine.FormService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.form.FormProperty;
import org.activiti.engine.form.StartFormData;
import org.activiti.engine.impl.identity.Authentication;
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
import org.subethamail.wiser.Wiser;
import org.subethamail.wiser.WiserMessage;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "classpath:application-context-process.xml",
		"classpath:test-application-context-process.xml" })
public class TravelProcessTest {

	private static final Logger LOGGER = Logger.getLogger(TravelProcessTest.class.getName());
	
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
	
	@Autowired
	public Wiser wiserServer;

	@Test
	@Deployment(resources = "process/simple-travel.bpmn20.xml")
	public void testTravelProcessRejectRequest() throws Exception {
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

		// Start the actual process as a traveler
		Authentication.setAuthenticatedUserId("traveller");
		ProcessInstance processInstance = runtimeService
				.startProcessInstanceByKey("simple-travel", properties);
		Assert.assertNotNull(processInstance);

		// Check if a task is available, queued for a manager
		Task theTask = taskService.createTaskQuery()
				.taskCandidateGroup("management").singleResult();
		Assert.assertNotNull(theTask);
		Assert.assertEquals(processInstance.getId(),
				theTask.getProcessInstanceId());

		// Claim and finish the task as manager
		Authentication.setAuthenticatedUserId("manager");
		taskService.claim(theTask.getId(), "manager");
		
		// Reject the request
		Map<String, Object> rejectProperties = getRejectProperties();
		taskService.complete(theTask.getId(), rejectProperties);
		
		Assert.assertEquals(1, wiserServer.getMessages().size());
		WiserMessage rejectionMail = wiserServer.getMessages().get(0);
		wiserServer.getMessages().clear();
		
		// Check the mail's from and to
		Assert.assertEquals("traveller@vaadin-activiti.org", rejectionMail.getEnvelopeReceiver());
		Assert.assertEquals("no-reply@vaadin-activiti.org", rejectionMail.getEnvelopeSender());
		String data = new String(rejectionMail.getData(), "UTF-8");
		
		LOGGER.info("Recieved mail: \n" + data);
		
		// Check mail body for receiver, rejecting manager and motivation
		Assert.assertTrue(data.contains("Dear traveller"));
		Assert.assertTrue(data.contains("Test Rejection"));
		Assert.assertTrue(data.contains("by manager"));
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