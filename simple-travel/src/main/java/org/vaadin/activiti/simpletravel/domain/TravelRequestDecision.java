package org.vaadin.activiti.simpletravel.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotNull;

@Entity
public class TravelRequestDecision extends AbstractEntity {

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @NotNull(message = "Please approve or deny the request")
    protected Decision decision;
    @Column(nullable = false)
    @NotNull(message = "Please enter a motivation for your decision")
    protected String motivationOfDecision;
    @Column(nullable = false)
    protected String managerUserId;

    protected TravelRequestDecision() {
    }

    public TravelRequestDecision(Decision decision, String motivationOfDecision, String managerUserId) {
        this.decision = decision;
        this.motivationOfDecision = motivationOfDecision;
        this.managerUserId = managerUserId;
    }

    public Decision getDecision() {
        return decision;
    }

    public String getManagerUserId() {
        return managerUserId;
    }

    public String getMotivationOfDecision() {
        return motivationOfDecision;
    }
}
