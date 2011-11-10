package org.vaadin.activiti.simpletravel.test;

import java.util.Calendar;
import java.util.logging.Logger;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
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
import org.springframework.transaction.annotation.Transactional;
import org.subethamail.wiser.Wiser;
import org.subethamail.wiser.WiserMessage;
import org.vaadin.activiti.simpletravel.domain.Country;
import org.vaadin.activiti.simpletravel.domain.TravelRequest;
import org.vaadin.activiti.simpletravel.service.TravelRequestService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "classpath:application-context-process.xml",
		"classpath:test-application-context-process.xml" })
public class TravelProcessTest {

	private static final Logger LOGGER = Logger.getLogger(TravelProcessTest.class.getName());
	
	@Autowired
	private RuntimeService runtimeService;

	@Autowired
	private TaskService taskService;

	@Autowired
	private RepositoryService repositoryService;
	
	@Autowired
	private TravelRequestService travelRequestService;

	@Autowired
	@Rule
	public
	ActivitiRule activitiSpringRule;
	
	@Autowired
	private Wiser wiserServer;
	
	@Test
	@Deployment(resources = "process/simple-travel.bpmn20.xml")
	@Transactional
	public void testTravelProcessRejectRequest() throws Exception {
		// Start the actual process as a traveler
		Authentication.setAuthenticatedUserId("traveller");
		TravelRequest newRequest = fillTravelRequest();
		
		newRequest = travelRequestService.submitNewTravelRequest(newRequest);
		
		// Check if the process is started
		ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
				.processInstanceBusinessKey(newRequest.getId().toString())
				.singleResult();
		Assert.assertNotNull(processInstance);
		
		// Check if the travel request JPA-entity is available
		TravelRequest travelRequestFromVariable = (TravelRequest) runtimeService.getVariable(processInstance.getId(), "request");
		Assert.assertNotNull(travelRequestFromVariable);
		
		// Check if a task is available, queued for a manager
		Task theTask = taskService.createTaskQuery()
				.taskCandidateGroup("management").singleResult();
		Assert.assertNotNull(theTask);
		Assert.assertEquals(processInstance.getId(),
				theTask.getProcessInstanceId());

		// Claim the task as manager
		Authentication.setAuthenticatedUserId("manager");
		taskService.claim(theTask.getId(), "manager");
		
		// Reject the task
		travelRequestService.denyTravelRequest(newRequest, "No more money left");
		
		// Mail should have been sent, with the rejection details
		Assert.assertEquals(1, wiserServer.getMessages().size());
		WiserMessage rejectionMail = wiserServer.getMessages().get(0);
		wiserServer.getMessages().clear();
		
		// Check the mail's from and to
		Assert.assertEquals("traveller@vaadin-activiti.org", rejectionMail.getEnvelopeReceiver());
		Assert.assertEquals("no-reply@vaadin-activiti.org", rejectionMail.getEnvelopeSender());
		String data = new String(rejectionMail.getData(), "UTF-8");
		
		LOGGER.info(data);
		
		// Check mail body for receiver, rejecting manager and motivation
		Assert.assertTrue(data.contains("Dear traveller"));
		Assert.assertTrue(data.contains("No more money left"));
		Assert.assertTrue(data.contains("by manager"));
	}
	
	@Test
	@Deployment(resources = "process/simple-travel.bpmn20.xml")
	@Transactional
	public void testTravelProcessApproveRequest() throws Exception {
		// Start the actual process as a traveler
		Authentication.setAuthenticatedUserId("traveller");
		TravelRequest newRequest = fillTravelRequest();

		newRequest = travelRequestService.submitNewTravelRequest(newRequest);

		// Check if the process is started
		ProcessInstance processInstance = runtimeService
				.createProcessInstanceQuery()
				.processInstanceBusinessKey(newRequest.getId().toString())
				.singleResult();
		Assert.assertNotNull(processInstance);

		// Check if the travel request JPA-entity is available
		TravelRequest travelRequestFromVariable = (TravelRequest) runtimeService
				.getVariable(processInstance.getId(), "request");
		Assert.assertNotNull(travelRequestFromVariable);

		// Check if a task is available, queued for a manager
		Task theTask = taskService.createTaskQuery()
				.taskCandidateGroup("management").singleResult();
		Assert.assertNotNull(theTask);
		Assert.assertEquals(processInstance.getId(),
				theTask.getProcessInstanceId());

		// Claim the task as manager
		Authentication.setAuthenticatedUserId("manager");
		taskService.claim(theTask.getId(), "manager");

		// Approve the task
		travelRequestService.approveTravelRequest(newRequest, "Have fun!");
		
		// Check if a "book tickets" task is available
		Task bookTicketsTask = taskService.createTaskQuery()
				.processInstanceId(processInstance.getId())
				.taskCandidateGroup("secretary")
				.singleResult();
		Assert.assertNotNull(bookTicketsTask);
		Assert.assertEquals("Book tickets for trip to BELGIUM by traveller", bookTicketsTask.getDescription());
		
		// Claim task
		taskService.claim(bookTicketsTask.getId(), "secretary");
	}
	
	

	/**
	 * Create a travel request that passes validation.
	 * 
	 * @return a valid {@link TravelRequest} instance.
	 */
	private TravelRequest fillTravelRequest() {
		TravelRequest newRequest = new TravelRequest(); 
		newRequest.setCountry(Country.BELGIUM);
		newRequest.setRequesterUserId("traveller");
		newRequest.setRequesterUserName("Traveller");
		newRequest.setDepartureDate(Calendar.getInstance().getTime());
		newRequest.setReturnDate(Calendar.getInstance().getTime());
		newRequest.setDescription("Going to Devoxx in Antwerp");
		return newRequest;
	}
}