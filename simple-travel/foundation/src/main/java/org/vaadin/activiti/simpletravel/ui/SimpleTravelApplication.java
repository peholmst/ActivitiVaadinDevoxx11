package org.vaadin.activiti.simpletravel.ui;

import com.vaadin.Application;
import com.vaadin.service.ApplicationContext.TransactionListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.vaadin.activiti.simpletravel.identity.CurrentUserFactoryBean;

@Configurable
public class SimpleTravelApplication extends Application implements TransactionListener {

    @Autowired
    protected transient CurrentUserFactoryBean currentUserFactoryBean;

    @Override
    public void init() {
        setTheme("simpletravel");
        getContext().addTransactionListener(this);
        setMainWindow(new MainWindow());
    }

    @Override
    public void close() {
        setUser(null);
        getContext().removeTransactionListener(this);
        ((MainWindow) getMainWindow()).destroy();
        super.close();
    }

    @Override
    public void transactionStart(Application application, Object transactionData) {
        String username = (String) getUser();
        currentUserFactoryBean.setCurrentUsername(username);
    }

    @Override
    public void transactionEnd(Application application, Object transactionData) {
        currentUserFactoryBean.setCurrentUsername(null);
    }
}
