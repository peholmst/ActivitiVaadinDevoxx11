package org.vaadin.activiti.simpletravel.ui;

import com.github.peholmst.mvp4vaadin.ViewEvent;
import com.github.peholmst.mvp4vaadin.ViewListener;
import com.vaadin.ui.Window;
import org.vaadin.activiti.simpletravel.ui.dashboard.DashboardView;
import org.vaadin.activiti.simpletravel.ui.dashboard.components.DashboardViewComponent;
import org.vaadin.activiti.simpletravel.ui.dashboard.UserLoggedOutEvent;
import org.vaadin.activiti.simpletravel.ui.login.LoginView;
import org.vaadin.activiti.simpletravel.ui.login.components.LoginViewComponent;
import org.vaadin.activiti.simpletravel.ui.login.UserLoggedInEvent;

public class MainWindow extends Window implements ViewListener {

    private LoginView loginView;
    private DashboardView dashboardView;

    public MainWindow() {
        super("Activiti and Vaadin - A Match made in heaven");
        showLoginView();
    }

    @Override
    public void handleViewEvent(ViewEvent event) {
        if (event instanceof UserLoggedInEvent) {
            UserLoggedInEvent userLoggedInEvent = (UserLoggedInEvent) event;
            loginUser(userLoggedInEvent.getUsername());
        } else if (event instanceof UserLoggedOutEvent) {
            logoutUser();
        }
    }

    public void destroy() {
        disposeLoginView();
        disposeDashboardView();
    }

    private void loginUser(String username) {
        getApplication().setUser(username);
        showDashboardView();
        disposeLoginView();
    }

    private void logoutUser() {
        getApplication().close();
    }

    private void showLoginView() {
        loginView = new LoginViewComponent();
        loginView.addListener(this);
        setContent((LoginViewComponent) loginView);
    }

    private void disposeLoginView() {
        if (loginView != null) {
            loginView.removeListener(this);
            loginView = null;
        }
    }

    private void showDashboardView() {
        dashboardView = new DashboardViewComponent();
        dashboardView.addListener(this);
        setContent((DashboardViewComponent) dashboardView);
        dashboardView.startProcessEnginePolling();
    }

    private void disposeDashboardView() {
        if (dashboardView != null) {
            dashboardView.removeListener(this);
            dashboardView.stopProcessEnginePolling();
            dashboardView = null;
        }
    }
}
