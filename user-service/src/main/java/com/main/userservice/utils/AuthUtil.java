package com.main.userservice.utils;

import com.main.userservice.exceptions.custom.APIException;
import com.main.userservice.model.User;
import com.main.userservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthUtil {
    private final UserRepository userRepository;

//    public String loggedInEmail() {
//        return loggedInUser().getEmail();
//    }

    public User loggedInUser(String username) {
       // Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return userRepository.findByUserName(username)
                .orElseThrow(() -> new APIException("User Not Found"));
    }
}
