package org.vaadin.activiti.simpletravel.ui.dashboard;

import com.github.peholmst.mvp4vaadin.View;

public interface DashboardView extends View {
    
    void setNameOfCurrentUser(String firstName, String lastName);
    
}
