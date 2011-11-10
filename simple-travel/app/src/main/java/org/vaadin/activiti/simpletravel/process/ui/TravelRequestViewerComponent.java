package org.vaadin.activiti.simpletravel.process.ui;

import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Layout;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import org.apache.commons.lang.time.DateUtils;
import org.vaadin.activiti.simpletravel.domain.TravelRequest;

public class TravelRequestViewerComponent extends CustomComponent {

    private FormLayout layout;
    private Label requester;
    private Label time;
    private Label destination;
    private Label description;

    public TravelRequestViewerComponent() {
        layout = new FormLayout();
        setCompositionRoot(layout);

        requester = createLabelAndAddToLayout("Requester:", layout);
        time = createLabelAndAddToLayout("Time:", layout);
        destination = createLabelAndAddToLayout("Destination:", layout);
        description = createLabelAndAddToLayout("Description:", layout);
    }

    private Label createLabelAndAddToLayout(String caption, Layout layout) {
        Label lbl = new Label();
        lbl.setCaption(caption);
        layout.addComponent(lbl);
        return lbl;
    }
    
    public void setRequest(TravelRequest request) {
        requester.setValue(String.format("%s (ID: %s)", request.getRequesterFullName(), request.getRequesterUserId()));
        final DateFormat dateFormat = SimpleDateFormat.getDateInstance();
        if (DateUtils.isSameDay(request.getReturnDate(), request.getDepartureDate())) {
            time.setValue(dateFormat.format(request.getReturnDate()));
        } else {
            time.setValue(String.format("%s - %s",
                    dateFormat.format(request.getDepartureDate()), dateFormat.format(request.getReturnDate())));
        }
        destination.setValue(request.getCountry().getDisplayName());
        description.setValue(request.getDescription());
    }
    
}
