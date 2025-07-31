package com.main.userservice.controller;

import com.main.userservice.payload.dtos.AddressDTO;
import com.main.userservice.payload.response.AddressResponse;
import com.main.userservice.service.AddressService;
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

    @GetMapping("/user/{id}")
    public ResponseEntity<List<AddressDTO>> getUserAddressHandler(@PathVariable Long id){
        return ResponseEntity.status(HttpStatus.FOUND)
                .body(addressService.getUserAddressList(id));
    }

    @PostMapping
    public ResponseEntity<AddressDTO> saveAddressHandler(@Valid @RequestBody AddressDTO addressDTO) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(addressService.saveAddress(addressDTO));
    }

    @PutMapping
    public ResponseEntity<AddressDTO> updateAddressHandler(@Valid @RequestBody AddressDTO addressDTO) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(addressService.updateAddress(addressDTO));
    }

    @DeleteMapping("/{addressId}")
    public ResponseEntity<AddressDTO> deleteAddressHandler(@PathVariable Long addressId) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(addressService.deleteAddress(addressId));
    }
}
