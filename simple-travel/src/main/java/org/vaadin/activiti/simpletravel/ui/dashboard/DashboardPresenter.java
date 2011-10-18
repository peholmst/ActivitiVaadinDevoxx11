package org.vaadin.activiti.simpletravel.ui.dashboard;

import com.github.peholmst.mvp4vaadin.Presenter;
import com.github.peholmst.mvp4vaadin.View;
import java.util.List;
import org.activiti.engine.IdentityService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.User;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Qualifier;
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
    @Qualifier("currentUsername")
    protected String currentUsername;
    
    @Override
    public void init() {
        User currentUser = identityService.createUserQuery().userId(currentUsername).singleResult();
        getView().setNameOfCurrentUser(currentUser.getFirstName(), currentUser.getLastName());
        updateTaskListsInView();
        getView().hideProcessView();
    }        
    
    public void logout() {
        fireViewEvent(new UserLoggedOutEvent(getView()));
    }
    
    public void startProcessInstance(ProcessDefinition pd) {
        if (formViewService.hasStartFormView(pd)) {
            View formView = formViewService.getStartFormView(pd);
            getView().showProcessView(formView);
        } else {
            runtimeService.startProcessInstanceById(pd.getId());
        }
    }
    
    protected void updateTaskListsInView() {
        getView().setAssignedTasks(getAssignableTasks());
        getView().setClaimableTasks(getClaimableTasks());
        getView().setAvailableProcesses(getAvailableProcesses());
    }
    
    protected List<Task> getClaimableTasks() {
        final TaskQuery query = taskService.createTaskQuery();        
        
        for (Group group : getGroupsOfCurrentUser()) {
            query.taskCandidateGroup(group.getId());
        }
        
        return query.list();
    }
    
    protected List<Task> getAssignableTasks() {
        return taskService.createTaskQuery().taskAssignee(currentUsername).list();
    }
    
    protected List<Group> getGroupsOfCurrentUser() {
        return identityService.createGroupQuery().groupMember(currentUsername).list();
    }
    
    protected List<ProcessDefinition> getAvailableProcesses() {
        return repositoryService.createProcessDefinitionQuery().orderByProcessDefinitionName().asc().list();
    }
}
