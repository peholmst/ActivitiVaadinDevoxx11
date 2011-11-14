package org.vaadin.activiti.simpletravel.ui.dashboard.components;

import com.vaadin.ui.Button;
import com.vaadin.ui.themes.Reindeer;
import org.activiti.engine.task.Task;
import org.vaadin.activiti.simpletravel.ui.dashboard.DashboardPresenter;

public class AssignedTaskList extends TaskList {

    public AssignedTaskList(DashboardPresenter presenter) {
        super(presenter);
    }

    @Override
    protected Button createTaskComponent(Task task) {
        
        final String taskId = task.getId();
        final String taskName = task.getName();
        
        final Button complete = new Button(taskName);
        complete.addStyleName(Reindeer.BUTTON_LINK);

        complete.addListener(new Button.ClickListener() {

            @Override
            public void buttonClick(Button.ClickEvent event) {
                getPresenter().completeTask(taskId);
            }
        });

        return complete;
    }
}
