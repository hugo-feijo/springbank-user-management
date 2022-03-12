package com.springbank.user.query.api.handlers;

import com.springbank.user.core.events.UserRegisterEvent;
import com.springbank.user.core.events.UserRemoveEvent;
import com.springbank.user.core.events.UserUpdatedEvent;

public interface UserEventHandler {
    void on(UserRegisterEvent event);
    void on(UserUpdatedEvent event);
    void on(UserRemoveEvent event);
}
