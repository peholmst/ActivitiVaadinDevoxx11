package org.vaadin.activiti.simpletravel.ui.dashboard.components;

import com.github.peholmst.mvp4vaadin.AbstractViewComponent;
import com.github.peholmst.mvp4vaadin.VaadinView;
import com.github.peholmst.mvp4vaadin.View;
import com.vaadin.ui.Accordion;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.HorizontalSplitPanel;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window.Notification;
import com.vaadin.ui.themes.Reindeer;
import java.util.List;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Configurable;
import org.vaadin.activiti.simpletravel.ui.dashboard.DashboardPresenter;
import org.vaadin.activiti.simpletravel.ui.dashboard.DashboardView;
import org.vaadin.artur.icepush.ICEPush;

@Configurable
public class DashboardViewComponent extends AbstractViewComponent<DashboardView, DashboardPresenter> implements DashboardView {

    private VerticalLayout viewLayout;
    private HorizontalLayout header;
    private Button logout;
    private HorizontalSplitPanel splitPanel;
    private Accordion sidebar;
    private Label currentUser;
    private ProcessDefinitionList availableProcesses;
    private TaskList claimableTasks;
    private TaskList assignedTasks;
    private ICEPush pusher;

    @Override
    protected Component createCompositionRoot() {
        viewLayout = new VerticalLayout();
        viewLayout.setSizeFull();
        
        pusher = new ICEPush();
        viewLayout.addComponent(pusher);
        
        setSizeFull();
        return viewLayout;
    }

    @Override
    public void initView() {
        header = createHeader();
        viewLayout.addComponent(header);

        splitPanel = new HorizontalSplitPanel();
        splitPanel.setSplitPosition(20);
        splitPanel.setSizeFull();
        viewLayout.addComponent(splitPanel);
        viewLayout.setExpandRatio(splitPanel, 1.0f);

        sidebar = createSidebar();
        splitPanel.setFirstComponent(sidebar);
    }

    private HorizontalLayout createHeader() {
        final HorizontalLayout layout = new HorizontalLayout();
        layout.setWidth("100%");
        layout.setMargin(true);
        layout.setSpacing(true);
        layout.addStyleName(Reindeer.LAYOUT_BLACK);
        final Label title = new Label("Activiti + Vaadin - A Match Made in Heaven");
        title.addStyleName(Reindeer.LABEL_H1);
        layout.addComponent(title);
        layout.setExpandRatio(title, 1.0f);

        currentUser = new Label();
        currentUser.setSizeUndefined();
        layout.addComponent(currentUser);
        layout.setComponentAlignment(currentUser, Alignment.MIDDLE_RIGHT);

        logout = new Button("Logout");
        logout.addStyleName(Reindeer.BUTTON_SMALL);
        logout.addListener(createLogoutButtonListener());
        layout.addComponent(logout);
        layout.setComponentAlignment(logout, Alignment.MIDDLE_RIGHT);

        return layout;
    }

    private Button.ClickListener createLogoutButtonListener() {
        return new Button.ClickListener() {

            @Override
            public void buttonClick(Button.ClickEvent event) {
                getPresenter().logout();
            }
        };
    }

    private Accordion createSidebar() {
        final Accordion accordion = new Accordion();
        accordion.setSizeFull();

        availableProcesses = new ProcessDefinitionList(getPresenter());
        claimableTasks = new TaskList();
        assignedTasks = new TaskList();

        accordion.addTab(availableProcesses, "Start New Process");
        accordion.addTab(claimableTasks);
        accordion.addTab(assignedTasks);

        return accordion;
    }

    @Override
    public void setNameOfCurrentUser(String firstName, String lastName) {
        currentUser.setValue(String.format("%s %s", firstName, lastName));
    }

    @Override
    public void setClaimableTasks(List<Task> tasks) {
        sidebar.getTab(claimableTasks).setCaption(String.format("Unassigned Tasks (%d)", tasks.size()));
        // TODO implement me
        pushChanges();
    }

    @Override
    public void setAssignedTasks(List<Task> tasks) {
        sidebar.getTab(assignedTasks).setCaption(String.format("My Tasks (%d)", tasks.size()));
        // TODO implement me
        pushChanges();
    }

    @Override
    public void setAvailableProcesses(List<ProcessDefinition> processes) {
        availableProcesses.setProcessDefinitions(processes);
    }

    @Override
    public void showProcessView(View view) {
        if (view instanceof VaadinView) {
            splitPanel.setSecondComponent(((VaadinView) view).getViewComponent());
        }
    }

    @Override
    public void hideProcessView() {
        VerticalLayout layout = new VerticalLayout();
        layout.setMargin(true);
        layout.addComponent(new Label("No task selected."));
        splitPanel.setSecondComponent(layout);
    }

    @Override
    public void showProcessStartedMessage(String processName) {
        getWindow().showNotification(processName + " started");
    }

    @Override
    public void startProcessEnginePolling() {
        getPresenter().startProcessEnginePolling();
    }

    @Override
    public void stopProcessEnginePolling() {
        getPresenter().stopProcessEnginePolling();
    }
    
    private void pushChanges() {
        if (getApplication() != null) {
            pusher.push();
        }
    }

    @Override
    public void showNewClaimableTasksMessage() {
        getWindow().showNotification("There are new unassigned tasks", Notification.TYPE_TRAY_NOTIFICATION);
    }

    @Override
    public void showNewTasksMessage() {
        getWindow().showNotification("You have new tasks", Notification.TYPE_TRAY_NOTIFICATION);
    }
}
