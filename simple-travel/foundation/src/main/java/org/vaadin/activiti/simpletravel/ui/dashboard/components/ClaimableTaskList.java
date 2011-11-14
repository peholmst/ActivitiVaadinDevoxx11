package org.vaadin.activiti.simpletravel.ui.dashboard.components;

import com.vaadin.ui.Button;
import com.vaadin.ui.Label;
import com.vaadin.ui.PopupView;
import com.vaadin.ui.PopupView.PopupVisibilityEvent;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;
import org.activiti.engine.task.Task;
import org.vaadin.activiti.simpletravel.ui.dashboard.DashboardPresenter;

public class ClaimableTaskList extends TaskList {

    public ClaimableTaskList(DashboardPresenter presenter) {
        super(presenter);
    }

    @Override
    protected PopupView createTaskComponent(Task task) {
        
        final String taskId = task.getId();
        final String taskName = task.getName();
        
        final VerticalLayout layout = new VerticalLayout();
        layout.setMargin(true);
        layout.setSpacing(true);
        layout.setSizeUndefined();

        final Label label = new Label(taskName);
        label.setSizeUndefined();
        layout.addComponent(label);

        final Button assign = new Button("Assign to me");
        assign.setDisableOnClick(true);
        assign.addStyleName(Reindeer.BUTTON_SMALL);
        layout.addComponent(assign);
        
        final Button complete = new Button("Assign to me & Complete");
        complete.setDisableOnClick(true);
        complete.addStyleName(Reindeer.BUTTON_SMALL);
        layout.addComponent(complete);

        final PopupView popup = new PopupView(taskName, layout);
        popup.addListener(new PopupView.PopupVisibilityListener() {

            @Override
            public void popupVisibilityChange(PopupVisibilityEvent event) {
                if (event.isPopupVisible()) {
                    assign.setEnabled(true);
                    complete.setEnabled(true);
                }
            }
        });

        assign.addListener(new Button.ClickListener() {

            @Override
            public void buttonClick(Button.ClickEvent event) {
                getPresenter().assignTaskToCurrentUser(taskId);
                popup.setPopupVisible(false);
            }
        });

        complete.addListener(new Button.ClickListener() {

            @Override
            public void buttonClick(Button.ClickEvent event) {
                getPresenter().assignTaskToCurrentUserAndComplete(taskId);
                popup.setPopupVisible(false);
            }
        });

        return popup;
    }
}
