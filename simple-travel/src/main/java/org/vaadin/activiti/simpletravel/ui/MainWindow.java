package org.vaadin.activiti.simpletravel.ui;

import com.github.peholmst.mvp4vaadin.ViewEvent;
import com.github.peholmst.mvp4vaadin.ViewListener;
import com.vaadin.ui.Window;
import org.vaadin.activiti.simpletravel.ui.login.LoginView;
import org.vaadin.activiti.simpletravel.ui.login.LoginViewComponent;
import org.vaadin.activiti.simpletravel.ui.login.UserLoggedInEvent;

/**
 *
 * @author peholmst
 */
public class MainWindow extends Window implements ViewListener {

    private LoginView loginView;
    
    public MainWindow() {
        super("Activiti and Vaadin - A Match made in heaven");
        loginView = new LoginViewComponent();
        loginView.addListener(this);
        setContent((LoginViewComponent) loginView);
    }

    @Override
    public void handleViewEvent(ViewEvent event) {
        if (event instanceof UserLoggedInEvent) {
            UserLoggedInEvent userLoggedInEvent = (UserLoggedInEvent) event;
            getApplication().setUser(userLoggedInEvent.getUsername());
        }
    }

    
    
}
