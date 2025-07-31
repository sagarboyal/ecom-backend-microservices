package com.main.userservice.serviceImpl;

import com.main.userservice.exceptions.custom.ResourceNotFoundException;
import com.main.userservice.model.Address;
import com.main.userservice.model.User;
import com.main.userservice.payload.dtos.AddressDTO;
import com.main.userservice.payload.response.AddressResponse;
import com.main.userservice.repository.AddressRepository;
import com.main.userservice.repository.UserRepository;
import com.main.userservice.service.AddressService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService {
    private final UserRepository userRepository;
    private final AddressRepository addressRepository;
    private final ModelMapper modelMapper;

    @Override
    public AddressDTO saveAddress(AddressDTO addressDTO) {
        User user = validateUser(addressDTO.getUserId());
        Address address = modelMapper.map(addressDTO, Address.class);
        address.setUser(user);
        address = addressRepository.save(address);
        return modelMapper.map(address, AddressDTO.class);
    }

    @Override
    public List<AddressDTO> getAddressList() {
        return addressRepository.findAll().stream()
                .map(address -> modelMapper.map(address, AddressDTO.class))
                .toList();
    }

    @Override
    public List<AddressDTO> getUserAddressList(Long userId) {
        User user = validateUser(userId);
        return user.getAddresses().stream()
                .map( address -> modelMapper.map(address, AddressDTO.class))
                .toList();
    }

    @Override
    public AddressDTO updateAddress(AddressDTO addressDTO) {
        Address address = addressRepository.findById(addressDTO.getAddressId())
                .orElseThrow(() -> new ResourceNotFoundException("Address", "id", addressDTO.getAddressId()));

        Optional.ofNullable(addressDTO.getStreet()).ifPresent(address::setStreet);
        Optional.ofNullable(addressDTO.getBuilding()).ifPresent(address::setBuilding);
        Optional.ofNullable(addressDTO.getCity()).ifPresent(address::setCity);
        Optional.ofNullable(addressDTO.getState()).ifPresent(address::setState);
        Optional.ofNullable(addressDTO.getCountry()).ifPresent(address::setCountry);
        Optional.ofNullable(addressDTO.getZipcode()).ifPresent(address::setZipcode);

        address = addressRepository.save(address);
        return modelMapper.map(address, AddressDTO.class);
    }

    @Override
    public AddressDTO deleteAddress(Long addressId) {
        Address address = addressRepository.findById(addressId)
                .orElseThrow(() -> new ResourceNotFoundException("Address", "id", addressId));

        addressRepository.delete(address);
        return modelMapper.map(address, AddressDTO.class);
    }

    @Override
    public AddressResponse getAddressById(Long addressId) {
        Address address  = addressRepository.findById(addressId)
                .orElseThrow(() -> new ResourceNotFoundException("Address", "id", addressId));
        return AddressResponse.builder()
                .addressId(address.getAddressId())
                .street(address.getStreet())
                .building(address.getBuilding())
                .city(address.getCity())
                .state(address.getState())
                .country(address.getCountry())
                .zipcode(address.getZipcode())
                .build();
    }

    private User validateUser(Long userId) {
        return  userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
    }

}
