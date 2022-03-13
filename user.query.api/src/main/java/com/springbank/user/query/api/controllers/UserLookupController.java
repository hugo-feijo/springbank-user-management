package com.springbank.user.query.api.controllers;

import com.springbank.user.query.api.dto.UserLookupResponse;
import com.springbank.user.query.api.handlers.UserQueryService;
import com.springbank.user.query.api.queries.FindAllUsersQuery;
import com.springbank.user.query.api.queries.FindUserByIdQuery;
import com.springbank.user.query.api.queries.SearchUsersQuery;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Validated
@RestController
@RequestMapping(path = "/api/v1/user")
public class UserLookupController {
    private final UserQueryService userQueryService;

    public UserLookupController(UserQueryService userQueryService) {
        this.userQueryService = userQueryService;
    }

    @GetMapping()
    public ResponseEntity<UserLookupResponse> getAllUsers() {
        try {
            var query = new FindAllUsersQuery();
            var response = userQueryService.getAllUsers(query);

            if(response == null || response.getUsers() == null)
                return ResponseEntity.noContent().build();

            return ResponseEntity.ok(response);
        }catch (Exception e) {
            var safeErrorMessage = "Failed to complete get all users request";
            log.error(safeErrorMessage + " - stacktrace: {}", e);

            return ResponseEntity.internalServerError().body(new UserLookupResponse(safeErrorMessage));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserLookupResponse> getUserById(@PathVariable String id) {
        try {
            var query = new FindUserByIdQuery(id);
            var response = userQueryService.getUserById(query);

            if(response == null || response.getUsers() == null)
                return ResponseEntity.noContent().build();

            return ResponseEntity.ok(response);
        }catch (Exception e) {
            var safeErrorMessage = "Failed to complete get user by id request. id=" + id;
            log.error(safeErrorMessage + " - stacktrace: {}", e);

            return ResponseEntity.internalServerError().body(new UserLookupResponse(safeErrorMessage));
        }
    }

    @GetMapping("/filter/{filter}")
    public ResponseEntity<UserLookupResponse> searchUserByFilter(@PathVariable String filter) {
        try {
            var query = new SearchUsersQuery(filter);
            var response = userQueryService.searchUsers(query);

            if(response == null || response.getUsers() == null)
                return ResponseEntity.noContent().build();

            return ResponseEntity.ok(response);
        }catch (Exception e) {
            var safeErrorMessage = "Failed to complete search user by filter request. filter=" + filter;
            log.error(safeErrorMessage + " - stacktrace: {}", e);

            return ResponseEntity.internalServerError().body(new UserLookupResponse(safeErrorMessage));
        }
    }
}
