package org.vaadin.activiti.simpletravel.identity;

import org.activiti.engine.IdentityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
public class CurrentUserFactoryBean {
    
    private final InheritableThreadLocal<String> username = new InheritableThreadLocal<String>();
    
    @Autowired
    protected transient IdentityService identityService;
    
    
    @Bean
    @Qualifier("currentUsername")
    @Scope("prototype")
    public String getCurrentUsername() {
        return username.get();
    }
    
    public void setCurrentUsername(String username) {
        if (username == null) {
            this.username.remove();
        } else {
            this.username.set(username);
        }
        identityService.setAuthenticatedUserId(username);
    }
}
