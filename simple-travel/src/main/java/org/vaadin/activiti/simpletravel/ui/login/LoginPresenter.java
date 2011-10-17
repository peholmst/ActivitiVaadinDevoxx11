package org.vaadin.activiti.simpletravel.ui.login;

import com.github.peholmst.mvp4vaadin.Presenter;
import org.activiti.engine.IdentityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;

@Configurable
public class LoginPresenter extends Presenter<LoginView> {
 
    @Autowired
    protected transient IdentityService identityService;
    
    public void attemptLogin(String username, String password) {
        if (identityService.checkPassword(username, password)) {
            identityService.setAuthenticatedUserId(username);
            fireViewEvent(new UserLoggedInEvent(getView(), username));
        } else {
            getView().clearForm();
            getView().showLoginFailed();
        }
    }
    
}
