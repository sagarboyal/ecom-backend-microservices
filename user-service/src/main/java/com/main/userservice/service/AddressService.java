package com.main.userservice.service;

import com.main.userservice.model.User;
import com.main.userservice.payload.dtos.AddressDTO;
import com.main.userservice.payload.response.AddressResponse;

import java.util.List;

public interface AddressService {
    AddressDTO saveAddress(AddressDTO addressDTO, User user);
    List<AddressDTO> getAddressList();
    List<AddressDTO> getUserAddressList(User user);
    AddressDTO updateAddress(Long addressId, AddressDTO addressDTO);
    AddressDTO deleteAddress(Long addressId);
    AddressResponse getAddressById(Long addressId);
}