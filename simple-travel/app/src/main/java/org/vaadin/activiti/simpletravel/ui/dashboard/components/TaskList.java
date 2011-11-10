package org.vaadin.activiti.simpletravel.ui.dashboard.components;

import com.vaadin.ui.Panel;
import com.vaadin.ui.PopupView;
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
    private Map<String, PopupView> taskPopupViews = new HashMap<String, PopupView>();

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
            taskPopupViews.clear();
        } else {
            VerticalLayout layout = (VerticalLayout) getContent();

            Set<String> newTaskIds = new HashSet<String>();
            for (Task task : getTasks()) {
                newTaskIds.add(task.getId());
            }
            
            Set<String> taskIdsToDelete = new HashSet<String>(taskPopupViews.keySet());
            taskIdsToDelete.removeAll(newTaskIds);            
            for (String taskIdToDelete : taskIdsToDelete) {
                layout.removeComponent(taskPopupViews.remove(taskIdToDelete));
            }            
            
            int i = 0;
            for (Task task : getTasks()) {
                PopupView taskComponent = taskPopupViews.get(task.getId());
                if (taskComponent == null) {
                    taskComponent = createTaskPopupView(task);
                    taskPopupViews.put(task.getId(), taskComponent);
                    layout.addComponent(taskComponent, i);
                } else if (i != layout.getComponentIndex(taskComponent)) {
                    layout.removeComponent(taskComponent);
                    layout.addComponent(taskComponent, i);
                }
                ++i;
            }            
        }
    }

    protected abstract PopupView createTaskPopupView(Task task);
}
