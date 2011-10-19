package org.vaadin.activiti.simpletravel.ui.dashboard;

import com.github.peholmst.mvp4vaadin.Presenter;
import com.github.peholmst.mvp4vaadin.View;
import com.github.peholmst.mvp4vaadin.ViewEvent;
import com.github.peholmst.mvp4vaadin.ViewListener;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import org.activiti.engine.IdentityService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.User;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Qualifier;
import org.vaadin.activiti.simpletravel.ui.forms.FormClosedEvent;
import org.vaadin.activiti.simpletravel.ui.forms.FormViewService;

@Configurable
public class DashboardPresenter extends Presenter<DashboardView> {
    
    @Autowired
    protected transient IdentityService identityService;
    
    @Autowired
    protected transient TaskService taskService;     
    
    @Autowired
    protected transient RepositoryService repositoryService;
    
    @Autowired
    protected transient FormViewService formViewService;
    
    @Autowired
    protected transient RuntimeService runtimeService;

    @Autowired
    protected transient ScheduledExecutorService executorService;    
    
    protected ViewListener formCloseListener = new ViewListener() {

        @Override
        public void handleViewEvent(ViewEvent event) {
            if (event instanceof FormClosedEvent && event.getSource() == getCurrentFormView()) {
                hideFormView();
            }
        }
        
    };
    
    protected final Logger logger = LoggerFactory.getLogger(getClass());
    
    @Autowired
    @Qualifier("currentUsername")
    protected String currentUsername;
    
    protected View currentFormView;
    
    protected ScheduledFuture<?> taskRefresherJob;
    
    protected int currentNumberOfAssignedTasks;
    
    protected int currentNumberOfClaimableTasks;
    
    @Override
    public void init() {
        User currentUser = identityService.createUserQuery().userId(currentUsername).singleResult();
        getView().setNameOfCurrentUser(currentUser.getFirstName(), currentUser.getLastName());
        updateTaskListsInView();
        getView().setAvailableProcesses(getAvailableProcesses());        
        hideFormView();
    }                
    
    public void startProcessEnginePolling() {
        logger.info("Starting process engine polling");
        taskRefresherJob = executorService.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                updateTaskListsInView();
            }
        }, 0, 1000, TimeUnit.MILLISECONDS);        
    }
    
    public void stopProcessEnginePolling() {
        logger.info("Stopping process engine polling");
        if (taskRefresherJob != null) {
            taskRefresherJob.cancel(true);
        }
    }
    
    public void logout() {
        fireViewEvent(new UserLoggedOutEvent(getView()));
    }
    
    public void startProcessInstance(String processDefinitionKey) {
        final ProcessDefinition pd = getProcessDefinitionByKey(processDefinitionKey);
        if (formViewService.hasStartFormView(pd)) {
            View formView = formViewService.getStartFormView(pd);
            showFormView(formView);
        } else {
            runtimeService.startProcessInstanceByKey(processDefinitionKey);            
            getView().showProcessStartedMessage(pd.getName());
        }
    }        

    protected View getCurrentFormView() {
        return currentFormView;
    }            
    
    protected void showFormView(View view) {
       currentFormView = view;
       currentFormView.addListener(formCloseListener);
       getView().showProcessView(view);
    }
    
    protected void hideFormView() {
        if (currentFormView != null) {
            currentFormView.removeListener(formCloseListener);
        }
        currentFormView = null;
        getView().hideProcessView();
    }
    
    protected void updateTaskListsInView() {
        final List<Task> assignedTasks = getAssignedTasks();
        if (currentNumberOfAssignedTasks != assignedTasks.size()) {
            getView().showNewTasksMessage();
        }
        currentNumberOfAssignedTasks = assignedTasks.size();
        getView().setAssignedTasks(assignedTasks);
        
        final List<Task> claimableTasks = getClaimableTasks();
        if (currentNumberOfClaimableTasks != claimableTasks.size()) {
            getView().showNewClaimableTasksMessage();
        }
        currentNumberOfClaimableTasks = claimableTasks.size();
        getView().setClaimableTasks(claimableTasks);
    }
    
    protected List<Task> getClaimableTasks() {
        final TaskQuery query = taskService.createTaskQuery();        
        
        for (Group group : getGroupsOfCurrentUser()) {
            query.taskCandidateGroup(group.getId());
        }
        
        return query.list();
    }
    
    protected List<Task> getAssignedTasks() {
        return taskService.createTaskQuery().taskAssignee(currentUsername).list();
    }
    
    protected List<Group> getGroupsOfCurrentUser() {
        return identityService.createGroupQuery().groupMember(currentUsername).list();
    }
    
    protected List<ProcessDefinition> getAvailableProcesses() {
        return repositoryService.createProcessDefinitionQuery().orderByProcessDefinitionName().asc().list();
    }
    
    protected ProcessDefinition getProcessDefinitionByKey(String key) {
        return repositoryService.createProcessDefinitionQuery().processDefinitionKey(key).latestVersion().singleResult();
    }
}
