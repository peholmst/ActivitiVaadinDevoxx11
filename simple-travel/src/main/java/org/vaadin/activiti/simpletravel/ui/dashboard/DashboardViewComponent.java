package org.vaadin.activiti.simpletravel.ui.dashboard;

import com.github.peholmst.mvp4vaadin.AbstractViewComponent;
import com.vaadin.ui.Accordion;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.HorizontalSplitPanel;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;
import org.springframework.beans.factory.annotation.Configurable;

@Configurable
public class DashboardViewComponent extends AbstractViewComponent<DashboardView, DashboardPresenter> implements DashboardView {

    private VerticalLayout viewLayout;
    private HorizontalLayout header;
    private Button logout;
    private HorizontalSplitPanel splitPanel;
    private Accordion sidebar;
    private Label currentUser;

    @Override
    protected Component createCompositionRoot() {
        viewLayout = new VerticalLayout();
        viewLayout.setSizeFull();
        setSizeFull();

        header = createHeader();
        viewLayout.addComponent(header);

        splitPanel = new HorizontalSplitPanel();
        splitPanel.setSplitPosition(20);
        splitPanel.setSizeFull();
        viewLayout.addComponent(splitPanel);
        viewLayout.setExpandRatio(splitPanel, 1.0f);

        sidebar = createSidebar();
        splitPanel.setFirstComponent(sidebar);

        return viewLayout;
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
        return accordion;
    }

    @Override
    public void setNameOfCurrentUser(String firstName, String lastName) {
        currentUser.setValue(String.format("%s %s", firstName, lastName));
    }
}
