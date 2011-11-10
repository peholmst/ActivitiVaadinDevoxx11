package org.vaadin.activiti.simpletravel.domain.repositories.jpa;

import org.springframework.stereotype.Repository;
import org.vaadin.activiti.simpletravel.domain.TravelRequest;
import org.vaadin.activiti.simpletravel.domain.repositories.TravelRequestRepository;

@Repository
public class TravelRequestJpaRepository extends AbstractJpaRepository<TravelRequest> implements TravelRequestRepository {

    public TravelRequestJpaRepository() {
        super(TravelRequest.class);
    }
}
