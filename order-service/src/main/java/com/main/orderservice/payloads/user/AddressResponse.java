package com.main.orderservice.payloads.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddressResponse {
    private Long addressId;
    private String street;
    private String building;
    private String city;
    private String state;
    private String country;
    private String zipcode;
}
