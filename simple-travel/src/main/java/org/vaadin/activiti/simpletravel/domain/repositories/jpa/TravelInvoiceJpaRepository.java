package org.vaadin.activiti.simpletravel.domain.repositories.jpa;

import org.springframework.stereotype.Repository;
import org.vaadin.activiti.simpletravel.domain.TravelInvoice;
import org.vaadin.activiti.simpletravel.domain.repositories.TravelInvoiceRepository;

@Repository
public class TravelInvoiceJpaRepository extends AbstractJpaRepository<TravelInvoice> implements TravelInvoiceRepository {

    public TravelInvoiceJpaRepository() {
        super(TravelInvoice.class);
    }
}
