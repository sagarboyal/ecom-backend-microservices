package com.main.userservice.controller;

import com.main.userservice.model.User;
import com.main.userservice.payload.dtos.AddressDTO;
import com.main.userservice.payload.response.AddressResponse;
import com.main.userservice.service.AddressService;
import com.main.userservice.utils.AuthUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/addresses")
@RequiredArgsConstructor
public class AddressController {
    private final AddressService addressService;
    private AuthUtil authUtil;


    @GetMapping
    public ResponseEntity<List<AddressDTO>> getAddressHandler(){
        return ResponseEntity.status(HttpStatus.FOUND)
                .body(addressService.getAddressList());
    }

    @GetMapping("/{addressId}")
    public ResponseEntity<AddressResponse> getAddressHandler(@PathVariable Long addressId){
        return ResponseEntity.status(HttpStatus.OK)
                .body(addressService.getAddressById(addressId));
    }

    @GetMapping("/user")
    public ResponseEntity<List<AddressDTO>> getUserAddressHandler(){
        User user = authUtil.loggedInUser(null);
        return ResponseEntity.status(HttpStatus.FOUND)
                .body(addressService.getUserAddressList(user));
    }

    @PostMapping
    public ResponseEntity<AddressDTO> saveAddressHandler(@Valid @RequestBody AddressDTO addressDTO) {
        User user = authUtil.loggedInUser(null);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(addressService.saveAddress(addressDTO, user));
    }

    @PutMapping("/{addressId}")
    public ResponseEntity<AddressDTO> updateAddressHandler(@Valid @RequestBody AddressDTO addressDTO,
                                                           @PathVariable Long addressId) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(addressService.updateAddress(addressId, addressDTO));
    }

    @DeleteMapping("/{addressId}")
    public ResponseEntity<AddressDTO> deleteAddressHandler(@PathVariable Long addressId) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(addressService.deleteAddress(addressId));
    }
}
