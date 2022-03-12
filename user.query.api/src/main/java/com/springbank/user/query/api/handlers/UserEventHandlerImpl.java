package com.springbank.user.query.api.handlers;

import com.springbank.user.core.events.UserRegisterEvent;
import com.springbank.user.core.events.UserRemoveEvent;
import com.springbank.user.core.events.UserUpdatedEvent;
import com.springbank.user.query.api.repositories.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@ProcessingGroup("user-group")
public class UserEventHandlerImpl implements UserEventHandler {
    private final UserRepository userRepository;

    @Autowired
    public UserEventHandlerImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @EventHandler
    @Override
    public void on(UserRegisterEvent event) {
        log.info("Event Handler Registrando usuário: {}", event);
        userRepository.save(event.getUser());
    }

    @EventHandler
    @Override
    public void on(UserUpdatedEvent event) {
        userRepository.save(event.getUser());

    }

    @EventHandler
    @Override
    public void on(UserRemoveEvent event) {
        userRepository.deleteById(event.getId());
    }
}
