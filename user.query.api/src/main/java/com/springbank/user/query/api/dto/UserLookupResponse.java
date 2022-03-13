package com.springbank.user.query.api.dto;

import com.springbank.user.core.dto.BaseResponse;
import com.springbank.user.core.models.User;

import java.util.ArrayList;
import java.util.List;

public class UserLookupResponse extends BaseResponse {
    private List<User> users;

    public UserLookupResponse() {
        super(null);
    }

    public  UserLookupResponse(String message) {
        super(message);
    }
    public  UserLookupResponse(User user, String message) {
        super(message);
        this.users = new ArrayList<>();
        this.users.add(user);
    }
    public  UserLookupResponse(User user) {
        super(null);
        this.users = new ArrayList<>();
        this.users.add(user);
    }
    public  UserLookupResponse(List<User> users) {
        super(null);
        this.users = users;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }
}
