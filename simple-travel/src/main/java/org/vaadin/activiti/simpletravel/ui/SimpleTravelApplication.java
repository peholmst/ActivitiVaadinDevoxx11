package org.vaadin.activiti.simpletravel.ui;

import com.vaadin.Application;
import com.vaadin.service.ApplicationContext.TransactionListener;
import org.activiti.engine.IdentityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;


@Configurable
public class SimpleTravelApplication extends Application implements TransactionListener {

    @Autowired
    protected transient IdentityService identityService;
    
    @Override
    public void init() {
        getContext().addTransactionListener(this);
        setMainWindow(new MainWindow());
    }

    @Override
    public void close() {
        setUser(null);
        getContext().removeTransactionListener(this);
        super.close();
    }   
    
    @Override
    public void transactionStart(Application application, Object transactionData) {
        identityService.setAuthenticatedUserId((String) getUser());
    }

    @Override
    public void transactionEnd(Application application, Object transactionData) {
        identityService.setAuthenticatedUserId(null);
    }
    
}
