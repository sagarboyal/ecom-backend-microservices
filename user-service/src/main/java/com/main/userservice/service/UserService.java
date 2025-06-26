package com.main.userservice.service;

import com.main.userservice.model.User;
import com.main.userservice.payload.request.UserRequest;
import com.main.userservice.payload.response.PagedResponse;
import com.main.userservice.payload.response.UserResponse;

public interface UserService {
    UserResponse createUser(UserRequest userRequest);
    UserResponse getUserById(Long id);
    UserResponse updateUser(Long id, UserRequest request);
    void deleteUser(Long id);
    PagedResponse<UserResponse> getAllUser(Integer pageNumber,Integer size);
}
