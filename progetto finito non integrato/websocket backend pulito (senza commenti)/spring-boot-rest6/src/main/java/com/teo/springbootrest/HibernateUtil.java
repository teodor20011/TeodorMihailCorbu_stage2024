package com.teo.springbootrest;

import com.teo.springbootrest.model.User;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Component;

@Component
public class HibernateUtil {

    public void initializeMessages(User sender, User receiver) {
        Hibernate.initialize(sender.getMessagesSender());
        Hibernate.initialize(receiver.getMessagesReceiver());
    }
}