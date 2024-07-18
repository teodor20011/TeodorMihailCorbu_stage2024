package com.triphippie.userService;

import com.triphippie.userService.model.user.User;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Component;

@Component
public class HibernateUtil {

    public void initializeMessages(User sender, User receiver) {
        Hibernate.initialize(sender.getMessagesSender());
        Hibernate.initialize(receiver.getMessagesReceiver());
    }
}