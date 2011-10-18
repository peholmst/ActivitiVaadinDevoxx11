package org.vaadin.activiti.simpletravel.ui.dashboard;

import com.github.peholmst.mvp4vaadin.Presenter;
import org.activiti.engine.IdentityService;
import org.activiti.engine.TaskService;
import org.activiti.engine.identity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Qualifier;

@Configurable
public class DashboardPresenter extends Presenter<DashboardView> {
    
    @Autowired
    protected transient IdentityService identityService;
    
    @Autowired
    protected transient TaskService taskService;

    @Autowired
    @Qualifier("currentUsername")
    protected String currentUsername;
    
    @Override
    public void init() {
        User currentUser = identityService.createUserQuery().userId(currentUsername).singleResult();
        getView().setNameOfCurrentUser(currentUser.getFirstName(), currentUser.getLastName());
    }        
    
    public void logout() {
        fireViewEvent(new UserLoggedOutEvent(getView()));
    }
}
