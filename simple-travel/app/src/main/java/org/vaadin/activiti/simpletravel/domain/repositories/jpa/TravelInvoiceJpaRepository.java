package org.vaadin.activiti.simpletravel.domain.repositories.jpa;

import java.util.List;
import javax.persistence.TypedQuery;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.vaadin.activiti.simpletravel.domain.TravelInvoice;
import org.vaadin.activiti.simpletravel.domain.TravelRequest;
import org.vaadin.activiti.simpletravel.domain.repositories.TravelInvoiceRepository;

@Repository
public class TravelInvoiceJpaRepository extends AbstractJpaRepository<TravelInvoice> implements TravelInvoiceRepository {

    public TravelInvoiceJpaRepository() {
        super(TravelInvoice.class);
    }

    @Override
    @Transactional(propagation = Propagation.MANDATORY, readOnly = true)
    public TravelInvoice findByTravelRequest(TravelRequest request) {
        TypedQuery<TravelInvoice> query = entityManager.createQuery("SELECT ti FROM TravelInvoice ti WHERE ti.request = :request", TravelInvoice.class);
        query.setParameter("request", request);
        List<TravelInvoice> result = query.getResultList();
        if (result.isEmpty()) {
            return null;
        } else {
            return result.get(0);
        }
    }
}
