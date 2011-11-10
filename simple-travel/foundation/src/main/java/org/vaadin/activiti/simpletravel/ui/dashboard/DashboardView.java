package org.vaadin.activiti.simpletravel.ui.dashboard;

import com.github.peholmst.mvp4vaadin.View;
import java.util.List;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.task.Task;

public interface DashboardView extends View {
    
    void setNameOfCurrentUser(String firstName, String lastName);
    
    void setClaimableTasks(List<Task> tasks);
    
    void setAssignedTasks(List<Task> tasks);
    
    void setAvailableProcesses(List<ProcessDefinition> processes);
    
    void showProcessView(View view);
    
    void hideProcessView();
    
    void showProcessStartedMessage(String processName);
    
    void showTaskCompletedMessage(String taskName);
    
    void showNewClaimableTasksMessage();
    
    void showNewTasksMessage();
    
    void startProcessEnginePolling();
    
    void stopProcessEnginePolling();    
    
    void pushChangesToClient();
    
}
