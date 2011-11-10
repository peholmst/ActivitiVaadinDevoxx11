package org.vaadin.activiti.simpletravel.identity.aspects;

import java.util.List;
import org.activiti.engine.IdentityService;
import org.activiti.engine.identity.Group;
import org.activiti.engine.impl.identity.Authentication;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.vaadin.activiti.simpletravel.identity.AccessDeniedException;
import org.vaadin.activiti.simpletravel.identity.RequireGroup;

@Aspect
@Configurable
public class RequireGroupAspect {

    @Autowired
    private IdentityService identityService;
    
    private static final Logger LOGGER = LoggerFactory.getLogger(RequireGroupAspect.class);

    @Before("@annotation(requireGroup)")
    public void checkUserGroup(RequireGroup requireGroup) {
        final String userId = Authentication.getAuthenticatedUserId();
        if (userId == null) {
            LOGGER.warn("Cannot check user groups as no user information is available");
            throw new AccessDeniedException("No user information available");
        }
        final List<Group> userGroups = identityService.createGroupQuery().groupMember(userId).list();
        for (String requiredGroup : requireGroup.value()) {
            if (userIsMemberOf(requiredGroup, userGroups)) {
                LOGGER.info("User {} is member of required group {}", userId, requiredGroup);
                return;
            }
        }
        LOGGER.warn("User {} was not a member of any of the required groups", userId);
        throw new AccessDeniedException("User is not member of required group");
    }

    private boolean userIsMemberOf(String requiredGroup, List<Group> userGroups) {
        for (Group g : userGroups) {
            if (requiredGroup.equals(g.getId())) {
                return true;
            }
        }
        return false;
    }
}
