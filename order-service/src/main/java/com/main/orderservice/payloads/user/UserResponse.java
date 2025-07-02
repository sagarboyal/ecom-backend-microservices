package com.main.orderservice.payloads.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponse {
    private Long userId;
    private String userName;
    private String email;
    private Set<String> roles;
    private List<AddressResponse> addresses;
}

