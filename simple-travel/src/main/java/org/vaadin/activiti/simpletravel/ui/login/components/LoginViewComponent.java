package org.vaadin.activiti.simpletravel.ui.login.components;

import com.github.peholmst.mvp4vaadin.AbstractViewComponent;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window.Notification;
import com.vaadin.ui.themes.Reindeer;
import org.springframework.beans.factory.annotation.Configurable;
import org.vaadin.activiti.simpletravel.ui.login.LoginPresenter;
import org.vaadin.activiti.simpletravel.ui.login.LoginView;

@Configurable
public class LoginViewComponent extends AbstractViewComponent<LoginView, LoginPresenter> implements LoginView {

    private HorizontalLayout viewLayout;
    private TextField username;
    private PasswordField password;
    private Button login;    
    
    @Override
    protected Component createCompositionRoot() {
        VerticalLayout loginPanel = new VerticalLayout();
        loginPanel.setSpacing(true);
        loginPanel.setWidth("300px");

        Label header = new Label("Please login");
        header.addStyleName(Reindeer.LABEL_H1);
        loginPanel.addComponent(header);

        username = new TextField("Username");
        username.setWidth("100%");
        loginPanel.addComponent(username);

        password = new PasswordField("Password");
        password.setWidth("100%");
        loginPanel.addComponent(password);

        HorizontalLayout buttons = new HorizontalLayout();
        buttons.setSpacing(true);
        loginPanel.addComponent(buttons);
        loginPanel.setComponentAlignment(buttons, Alignment.MIDDLE_RIGHT);

        login = new Button("Login");
        login.setClickShortcut(KeyCode.ENTER);
        login.addStyleName(Reindeer.BUTTON_DEFAULT);
        login.addListener(createLoginButtonListener());
        buttons.addComponent(login);

        viewLayout = new HorizontalLayout();
        viewLayout.addComponent(loginPanel);
        viewLayout.setComponentAlignment(loginPanel, Alignment.MIDDLE_CENTER);
        viewLayout.setSizeFull();
        viewLayout.addStyleName(Reindeer.LAYOUT_BLACK);
        setSizeFull();
        
        username.focus();        

        return viewLayout;
    }

    @SuppressWarnings("serial")
    private Button.ClickListener createLoginButtonListener() {
        return new Button.ClickListener() {

            @Override
            public void buttonClick(Button.ClickEvent event) {
                getPresenter().attemptLogin((String) username.getValue(),
                        (String) password.getValue());
            }
        };
    }

    @Override
    public void showLoginFailed() {
        viewLayout.getWindow().showNotification(
                "Login failed. Please try again.",
                Notification.TYPE_HUMANIZED_MESSAGE);
    }

    @Override
    public void clearForm() {
        username.setValue("");
        password.setValue("");
        username.focus();
    }
}
