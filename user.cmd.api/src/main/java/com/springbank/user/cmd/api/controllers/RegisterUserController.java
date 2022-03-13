package com.springbank.user.cmd.api.controllers;

import com.springbank.user.cmd.api.commands.RegisterUserCommand;
import com.springbank.user.cmd.api.dto.RegisterUserResponse;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.UUID;

@Slf4j
@Validated
@RestController
@RequestMapping(path = "/api/v1/user")
public class RegisterUserController {
    private final CommandGateway commandGateway;

    @Autowired
    public RegisterUserController(CommandGateway commandGateway) {
        this.commandGateway = commandGateway;
    }

    @PostMapping
    public ResponseEntity<RegisterUserResponse> registerUser(@Valid @RequestBody RegisterUserCommand command) {
        var id = UUID.randomUUID().toString();
        command.setId(id);
        log.info("Receiving Register User request. command={}", command);
        try{
            commandGateway.send(command);

            return ResponseEntity.status(HttpStatus.CREATED).body(new RegisterUserResponse(id, "User successfully registered!"));
        } catch (Exception e) {
            var safeErrorMessage = "Error while processing register user request for id = " + command.getId();
            log.error(safeErrorMessage + " - Stacktrace: {}", e);

            return ResponseEntity.internalServerError().body(new RegisterUserResponse(id, safeErrorMessage));
        }
    }
}
