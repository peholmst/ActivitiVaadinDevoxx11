package org.vaadin.activiti.simpletravel.ui.login;

import com.github.peholmst.mvp4vaadin.View;

public interface LoginView extends View {

    void showLoginFailed();
    
    void clearForm();
    
}
