package com.main.userservice.service;

import com.main.userservice.payload.dtos.AddressDTO;
import com.main.userservice.payload.response.AddressResponse;

import java.util.List;

public interface AddressService {
    AddressDTO saveAddress(AddressDTO addressDTO);
    List<AddressDTO> getAddressList();
    List<AddressDTO> getUserAddressList(Long userId);
    AddressDTO updateAddress(AddressDTO addressDTO);
    AddressDTO deleteAddress(Long addressId);
    AddressResponse getAddressById(Long addressId);
}