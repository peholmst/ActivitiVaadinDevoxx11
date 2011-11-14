package org.vaadin.activiti.simpletravel.ui.dashboard.components;

import com.vaadin.ui.Component;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.activiti.engine.task.Task;
import org.vaadin.activiti.simpletravel.ui.dashboard.DashboardPresenter;

public abstract class TaskList extends Panel {
    
    private DashboardPresenter presenter;
    private List<Task> tasks;
    private Map<String, Component> taskComponents = new HashMap<String, Component>();

    public TaskList(DashboardPresenter presenter) {
        setStyleName(Reindeer.PANEL_LIGHT);
        ((VerticalLayout) getContent()).setSpacing(true);
        ((VerticalLayout) getContent()).setMargin(true);
        this.presenter = presenter;
    }

    protected DashboardPresenter getPresenter() {
        return presenter;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
        updateComponent();
    }

    public List<Task> getTasks() {
        return tasks;
    }

    protected void updateComponent() {
        if (getTasks() == null || getTasks().isEmpty()) {
            removeAllComponents();
            taskComponents.clear();
        } else {
            VerticalLayout layout = (VerticalLayout) getContent();

            Set<String> newTaskIds = new HashSet<String>();
            for (Task task : getTasks()) {
                newTaskIds.add(task.getId());
            }
            
            Set<String> taskIdsToDelete = new HashSet<String>(taskComponents.keySet());
            taskIdsToDelete.removeAll(newTaskIds);            
            for (String taskIdToDelete : taskIdsToDelete) {
                layout.removeComponent(taskComponents.remove(taskIdToDelete));
            }            
            
            int i = 0;
            for (Task task : getTasks()) {
                Component taskComponent = taskComponents.get(task.getId());
                if (taskComponent == null) {
                    taskComponent = createTaskComponent(task);
                    taskComponents.put(task.getId(), taskComponent);
                    layout.addComponent(taskComponent, i);
                } else if (i != layout.getComponentIndex(taskComponent)) {
                    layout.removeComponent(taskComponent);
                    layout.addComponent(taskComponent, i);
                }
                ++i;
            }            
        }
    }

    protected abstract Component createTaskComponent(Task task);
}
