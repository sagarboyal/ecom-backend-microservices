package com.main.orderservice.client.userservice;

import com.main.orderservice.payloads.user.AddressResponse;
import com.main.orderservice.payloads.user.UserResponse;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

@HttpExchange
public interface UserHttpInterfaceProvider {
    @GetExchange("/api/user/email/{email}")
    UserResponse getUserByEmailId(@PathVariable("email") String email);
    @GetExchange("/api/address/{addressId}")
    AddressResponse getAddressById(@PathVariable("addressId") Long addressId);
    @GetExchange("/api/users/id/{id}")
    UserResponse getUserById(@PathVariable("id") Long id);
}
