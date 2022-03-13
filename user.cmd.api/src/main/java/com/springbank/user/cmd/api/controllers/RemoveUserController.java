package com.springbank.user.cmd.api.controllers;

import com.springbank.user.cmd.api.commands.RemoveUserCommand;
import com.springbank.user.cmd.api.dto.BaseResponse;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Validated
@RestController
@RequestMapping(path = "/api/v1/user")
public class RemoveUserController {
    private final CommandGateway commandGateway;

    @Autowired
    public RemoveUserController(CommandGateway commandGateway) {
        this.commandGateway = commandGateway;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<BaseResponse> removeUser(@PathVariable String id) {
        log.info("Receiving Remove User request. id={}", id);
        try{
            commandGateway.send(new RemoveUserCommand(id));

            return new ResponseEntity<>(new BaseResponse("User successfully removed"), HttpStatus.OK);
        } catch (Exception e) {
            var safeErrorMessage = "Error while processing remove user request for id = " + id;
            log.error(safeErrorMessage + " - Stacktrace: {}", e);

            return ResponseEntity.internalServerError().body(new BaseResponse(safeErrorMessage));

        }
    }
}
