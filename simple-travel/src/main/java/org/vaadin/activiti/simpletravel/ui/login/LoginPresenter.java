package org.vaadin.activiti.simpletravel.ui.login;

import com.github.peholmst.mvp4vaadin.Presenter;
import org.activiti.engine.IdentityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.vaadin.activiti.simpletravel.identity.CurrentUserFactoryBean;

@Configurable
public class LoginPresenter extends Presenter<LoginView> {
 
    @Autowired
    protected transient IdentityService identityService;
    
    @Autowired
    protected transient CurrentUserFactoryBean currentUserFactoryBean;    
    
    public void attemptLogin(String username, String password) {
        if (identityService.checkPassword(username, password)) {
            currentUserFactoryBean.setCurrentUsername(username);
            fireViewEvent(new UserLoggedInEvent(getView(), username));
        } else {
            getView().clearForm();
            getView().showLoginFailed();
        }
    }
    
}
