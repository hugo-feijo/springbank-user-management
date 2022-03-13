package com.springbank.user.cmd.api.controllers;

import com.springbank.user.cmd.api.commands.UpdateUserCommand;
import com.springbank.user.cmd.api.dto.BaseResponse;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@Validated
@RestController
@RequestMapping(path = "/api/v1/user")
public class UpdateUserController {
    private final CommandGateway commandGateway;

    @Autowired
    public UpdateUserController(CommandGateway commandGateway) {
        this.commandGateway = commandGateway;
    }

    @PutMapping("/{id}")
    public ResponseEntity<BaseResponse> registerUser(@PathVariable String id,
                                                     @Valid @RequestBody UpdateUserCommand command) {
        command.setId(id);
        log.info("Receiving Updating User request. id={}, command={}", id, command);
        try{
            commandGateway.send(command);

            return new ResponseEntity<>(new BaseResponse("User successfully updated"), HttpStatus.OK);
        } catch (Exception e) {
            var safeErrorMessage = "Error while processing update user request for id = " + id;
            log.error(safeErrorMessage + " - Stacktrace: {}", e);

            return ResponseEntity.internalServerError().body(new BaseResponse(safeErrorMessage));

        }
    }
}
