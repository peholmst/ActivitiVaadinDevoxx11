package org.vaadin.activiti.simpletravel.domain.repositories;

import org.vaadin.activiti.simpletravel.domain.AbstractEntity;

public interface Repository<E extends AbstractEntity> {
    
    E findById(long id);
    
    E save(E entity);
    
    void delete(E entity);
    
}
