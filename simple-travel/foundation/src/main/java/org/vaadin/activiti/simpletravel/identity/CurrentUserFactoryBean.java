package org.vaadin.activiti.simpletravel.identity;

import org.activiti.engine.impl.identity.Authentication;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
public class CurrentUserFactoryBean {
        
    @Bean
    @Qualifier("currentUsername")
    @Scope("prototype")
    public String getCurrentUsername() {
        return Authentication.getAuthenticatedUserId();
    }
    
    public void setCurrentUsername(String username) {
        Authentication.setAuthenticatedUserId(username);
    }
}
