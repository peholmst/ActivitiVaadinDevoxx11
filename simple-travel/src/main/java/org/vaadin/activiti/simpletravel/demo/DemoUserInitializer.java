package org.vaadin.activiti.simpletravel.demo;

import javax.annotation.PostConstruct;
import org.activiti.engine.IdentityService;
import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DemoUserInitializer {
    
    @Autowired
    private IdentityService identityService;
    
    private final Logger logger = LoggerFactory.getLogger(getClass());
    
    private static final String GROUP_TRAVELERS = "travelers";
    private static final String GROUP_SECRETARIES = "secretaries";
    private static final String GROUP_MANAGERS = "managers";
    private static final String GROUP_PAYROLLADMINS = "payrolladmins";
    
    @PostConstruct
    public void setUp() {
        setUpGroups();
        setUpUsers();
    }
    
    
    public void setUpUsers() {
        logger.info("Creating users for Activiti");
        addUser("traveler", "Tom", "Traveler", "tom@foobar.net", GROUP_TRAVELERS);
        addUser("manager", "Mike", "Manager", "mike@foobar.net", GROUP_MANAGERS);
        addUser("secretary", "Steven", "Secretary", "steven@foobar.net", GROUP_SECRETARIES);
        addUser("payroll", "Patrick", "Payroll", "patrick@foobar.net", GROUP_PAYROLLADMINS);
    }
    
    private void addUser(String username, String firstName, String lastName, String email, String group) {
        logger.info("Adding user {} (firstName = {}, lastName = {}, email = {}, group = {})", new String[] {username, firstName, lastName, email, group});
        User user = identityService.newUser(username);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);
        user.setPassword("p");
        identityService.saveUser(user);
        identityService.createMembership(username, group);
    }
    
    private void setUpGroups() {
        logger.info("Creating user groups for Activiti");
        addGroup(GROUP_TRAVELERS, "Travelers");
        addGroup(GROUP_SECRETARIES, "Secretaries");
        addGroup(GROUP_MANAGERS, "Managers");
        addGroup(GROUP_PAYROLLADMINS, "Payroll Administrators");
    }
    
    private void addGroup(String groupId, String name) {
        logger.info("Adding group {} (name = {})", new String[] {groupId, name});
        Group group = identityService.newGroup(groupId);
        group.setName(name);
        identityService.saveGroup(group);
    }
}
