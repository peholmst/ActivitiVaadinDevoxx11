package org.vaadin.activiti.simpletravel.process.ui.booktickets.components;

import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Layout;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Configurable;
import org.vaadin.activiti.simpletravel.domain.TravelRequest;
import org.vaadin.activiti.simpletravel.process.ui.booktickets.BookTicketsFormPresenter;
import org.vaadin.activiti.simpletravel.process.ui.booktickets.BookTicketsFormView;
import org.vaadin.activiti.simpletravel.ui.forms.TaskForm;
import org.vaadin.activiti.simpletravel.ui.forms.TaskFormViewComponent;

@TaskForm(formKey = "bookTickets")
@Configurable
public class BookTicketsFormViewComponent extends TaskFormViewComponent<BookTicketsFormView, BookTicketsFormPresenter> implements BookTicketsFormView {

    private VerticalLayout layout;
    private Label requester;
    private Label time;
    private Label destination;
    private Label description;
    private Button ticketsBooked;
    private Button cancel;

    @Override
    protected Component createCompositionRoot() {
        layout = new VerticalLayout();
        layout.setMargin(true);
        layout.setSpacing(true);

        final Label header = new Label("Book Tickets");
        header.addStyleName(Reindeer.LABEL_H1);
        layout.addComponent(header);

        FormLayout requestLayout = new FormLayout();
        layout.addComponent(requestLayout);

        requester = createLabelAndAddToLayout("Requester:", requestLayout);
        time = createLabelAndAddToLayout("Time:", requestLayout);
        destination = createLabelAndAddToLayout("Destination:", requestLayout);
        description = createLabelAndAddToLayout("Description:", requestLayout);

        HorizontalLayout buttons = new HorizontalLayout();
        buttons.setSpacing(true);

        ticketsBooked = new Button("Tickets have been booked", new Button.ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {
                ticketsBookedClick();
            }
        });
        ticketsBooked.setDisableOnClick(true);
        buttons.addComponent(ticketsBooked);

        cancel = new Button("Cancel", new Button.ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {
                cancelClick();
            }
        });
        cancel.addStyleName(Reindeer.BUTTON_LINK);
        cancel.setDisableOnClick(true);
        buttons.addComponent(cancel);

        layout.addComponent(buttons);

        return layout;
    }

    private Label createLabelAndAddToLayout(String caption, Layout layout) {
        Label lbl = new Label();
        lbl.setCaption(caption);
        layout.addComponent(lbl);
        return lbl;
    }

    @Override
    public void setRequest(TravelRequest request) {
        requester.setValue(request.getRequesterFullName());
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

    private void cancelClick() {
        getPresenter().cancel();
    }

    private void ticketsBookedClick() {
        getPresenter().bookTickets();;
    }
}
