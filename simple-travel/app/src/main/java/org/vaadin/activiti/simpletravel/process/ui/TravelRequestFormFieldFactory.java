package org.vaadin.activiti.simpletravel.process.ui;

import com.vaadin.data.Item;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.ui.AbstractTextField;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.DateField;
import com.vaadin.ui.DefaultFieldFactory;
import com.vaadin.ui.Field;
import com.vaadin.ui.TextArea;
import java.util.Arrays;
import org.vaadin.activiti.simpletravel.domain.Country;
import org.vaadin.activiti.simpletravel.domain.TravelRequest;

public class TravelRequestFormFieldFactory extends DefaultFieldFactory {

    @Override
    public Field createField(Item item, Object propertyId, Component uiContext) {
        Field f;
        if (propertyId.equals(TravelRequest.PROP_COUNTRY)) {
            ComboBox c = new ComboBox("Country", new BeanItemContainer<Country>(Country.class, Arrays.asList(Country.values())));
            c.setItemCaptionPropertyId("displayName");
            c.setNullSelectionAllowed(false);
            c.setInputPrompt("Please select...");
            f = c;
        } else if (propertyId.equals(TravelRequest.PROP_DESCRIPTION)) {
            TextArea t = new TextArea("Description");
            t.setWidth("300px");
            t.setRows(10);
            f = t;
        } else {
            f = super.createField(item, propertyId, uiContext);
        }
        if (propertyId.equals(TravelRequest.PROP_DEPARTURE_DATE) || propertyId.equals(TravelRequest.PROP_RETURN_DATE)) {
            ((DateField) f).setResolution(DateField.RESOLUTION_DAY);
        }
        if (f instanceof AbstractTextField) {
            ((AbstractTextField) f).setNullRepresentation("");
        }
        return f;
    }
    
}
