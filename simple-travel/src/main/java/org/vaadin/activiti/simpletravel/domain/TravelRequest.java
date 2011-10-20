package org.vaadin.activiti.simpletravel.domain;

import com.github.peholmst.stuff4vaadin.clone.CloneThis;
import java.util.Date;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

@Entity
public class TravelRequest extends AbstractEntity {

    public static final String PROP_DEPARTURE_DATE = "departureDate";
    public static final String PROP_RETURN_DATE = "returnDate";
    public static final String PROP_COUNTRY = "country";
    public static final String PROP_DESCRIPTION = "description";
    
    // TODO Add custom validation: returnDate cannot be before departureDate
    
    @Column(nullable = false)
    protected String requesterUserId;
    @Temporal(TemporalType.DATE)
    @Column(nullable = false)
    @NotNull(message = "Please provide a departure date")
    protected Date departureDate;
    @Temporal(TemporalType.DATE)
    @Column(nullable = false)
    @NotNull(message = "Please provide a return date")
    protected Date returnDate;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @NotNull(message = "Please select the country you are traveling to")
    protected Country country;
    @Column(nullable = false)
    @NotNull(message = "Please enter a description of your trip")
    protected String description;
    @OneToOne(orphanRemoval = true, cascade = CascadeType.ALL)
    @CloneThis
    protected TravelRequestDecision decision;

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public Date getDepartureDate() {
        return departureDate;
    }

    public void setDepartureDate(Date departureDate) {
        this.departureDate = departureDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRequesterUserId() {
        return requesterUserId;
    }

    public void setRequesterUserId(String requesterUserId) {
        this.requesterUserId = requesterUserId;
    }

    public Date getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(Date returnDate) {
        this.returnDate = returnDate;
    }

    /**
     * Returns a clone of the travel request decision.
     */
    public TravelRequestDecision getDecision() {
        return decision != null ? (TravelRequestDecision) decision.clone() : null;
    }

    public void setDecision(TravelRequestDecision decision) {
        this.decision = decision != null ? (TravelRequestDecision) decision.clone() : null;
    }

    public boolean isDecisionPending() {
        return this.decision == null;
    }
}
