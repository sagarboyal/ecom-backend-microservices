package com.main.userservice.service;

import com.main.userservice.model.User;
import com.main.userservice.payload.dtos.AddressDTO;

import java.util.List;

public interface AddressService {
    AddressDTO saveAddress(AddressDTO addressDTO, User user);
    List<AddressDTO> getAddressList();
    AddressDTO getAddressList(Long addressId);
    List<AddressDTO> getUserAddressList(User user);
    AddressDTO updateAddress(Long addressId, AddressDTO addressDTO);
    AddressDTO deleteAddress(Long addressId);
}