package org.vaadin.activiti.simpletravel.ui.forms;

import com.github.peholmst.mvp4vaadin.View;
import org.vaadin.activiti.simpletravel.service.ValidationException;

public interface FormView extends View {
    
    void setValidationError(ValidationException error);    

    void clearValidationError();
    
    void showOptimisticLockingError();
    
}
