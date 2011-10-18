package org.vaadin.activiti.simpletravel.process.ui.newtravelrequest.components;

import com.github.peholmst.mvp4vaadin.AbstractViewComponent;
import com.vaadin.ui.Component;
import org.activiti.engine.repository.ProcessDefinition;
import org.springframework.beans.factory.annotation.Configurable;
import org.vaadin.activiti.simpletravel.process.ui.newtravelrequest.NewTravelRequestFormPresenter;
import org.vaadin.activiti.simpletravel.process.ui.newtravelrequest.NewTravelRequestFormView;
import org.vaadin.activiti.simpletravel.ui.forms.StartForm;

@StartForm(processDefinitionId = "simple-travel")
@Configurable
public class NewTravelRequestFormViewComponent extends AbstractViewComponent<NewTravelRequestFormView, NewTravelRequestFormPresenter> implements NewTravelRequestFormView {

    @Override
    protected Component createCompositionRoot() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setProcessDefinition(ProcessDefinition processDefinition) {
        getPresenter().setProcessDefinition(processDefinition);
    }
}
