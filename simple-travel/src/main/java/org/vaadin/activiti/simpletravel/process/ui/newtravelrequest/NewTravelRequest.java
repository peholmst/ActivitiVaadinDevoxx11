package org.vaadin.activiti.simpletravel.process.ui.newtravelrequest;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class NewTravelRequest implements java.io.Serializable {

    public static final String PROP_DESTINATION_NAME = "destinationName";
    public static final String PROP_DESTINATION_COUNTRY = "destinationCountry";
    public static final String PROP_DESTINATION_DESCRIPTION = "destinationDescription";
    public static final String PROP_DEPARTURE_DATE = "departureDate";
    public static final String PROP_RETURN_DATE = "returnDate";
    public static final String PROP_DESTINATION_CITY = "destinationCity";
    
    private String destinationName;
    private String destinationCountry;
    private String destinationDescription;
    private Date departureDate;
    private Date returnDate;
    private String destinationCity;

    public Date getDepartureDate() {
        return departureDate;
    }

    public void setDepartureDate(Date departureDate) {
        this.departureDate = departureDate;
    }

    public String getDestinationCity() {
        return destinationCity;
    }

    public void setDestinationCity(String destinationCity) {
        this.destinationCity = destinationCity;
    }

    public String getDestinationCountry() {
        return destinationCountry;
    }

    public void setDestinationCountry(String destinationCountry) {
        this.destinationCountry = destinationCountry;
    }

    public String getDestinationDescription() {
        return destinationDescription;
    }

    public void setDestinationDescription(String destinationDescription) {
        this.destinationDescription = destinationDescription;
    }

    public String getDestinationName() {
        return destinationName;
    }

    public void setDestinationName(String destinationName) {
        this.destinationName = destinationName;
    }

    public Date getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(Date returnDate) {
        this.returnDate = returnDate;
    }
    
    public Map<String, Object> getValuesAsMap() {
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put(PROP_DESTINATION_NAME, destinationName);
        map.put(PROP_DESTINATION_COUNTRY, destinationCountry);
        map.put(PROP_DESTINATION_DESCRIPTION, destinationDescription);
        map.put(PROP_DEPARTURE_DATE, departureDate);
        map.put(PROP_RETURN_DATE, returnDate);
        map.put(PROP_DESTINATION_CITY, destinationCity);
        return map;
    }
}
