package com.springbank.user.query.api.handlers;

import com.springbank.user.query.api.dto.UserLookupResponse;
import com.springbank.user.query.api.queries.FindAllUsersQuery;
import com.springbank.user.query.api.queries.FindUserByIdQuery;
import com.springbank.user.query.api.queries.SearchUsersQuery;
import com.springbank.user.query.api.repositories.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Slf4j
@Service
public class UserQueryServiceImpl implements UserQueryService {
    private final UserRepository userRepository;

    @Autowired
    public UserQueryServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @QueryHandler
    public UserLookupResponse getUserById(FindUserByIdQuery query) {
        log.info("Handler Find User By Id Query. query={}", query);
        var user = userRepository.findById(query.getId());
        return user.map(UserLookupResponse::new).orElse(null);
    }

    @QueryHandler
    @Override
    public UserLookupResponse searchUsers(SearchUsersQuery query) {
        log.info("Handler Search Users Query. query={}", query);
        var users = new ArrayList<>(userRepository.findByFilterRegex(query.getFilter()));
        return new UserLookupResponse(users);
    }

    @QueryHandler
    @Override
    public UserLookupResponse getAllUsers(FindAllUsersQuery query) {
        log.info("Handler Find All Users Query. query={}", query);
        var users = new ArrayList<>(userRepository.findAll());
        return new UserLookupResponse(users);
    }
}
