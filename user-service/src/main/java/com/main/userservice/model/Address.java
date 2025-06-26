package com.main.userservice.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long addressId;

    @NotBlank
    @Size(min = 5,
            message = "Street Name Must be At least 5 Characters!")
    private String street;

    @NotBlank
    @Size(min = 5,
            message = "Building Name Must be At least 5 Characters!")
    private String building;

    @NotBlank
    @Size(min = 4,
            message = "City Name Must be At least 4 Characters!")
    private String city;

    @NotBlank
    @Size(min = 2,
            message = "State Name Must be At least 2 Characters!")
    private String state;

    @NotBlank
    @Size(min = 2,
            message = "Country Name Must be At least 2 Characters!")
    private String country;

    @NotBlank
    @Size(min = 4,
            message = "Zip code Must be At least 4 Characters!")
    private String zipcode;

    public Address(String street, String building, String city, String state, String country, String zipcode) {
        this.street = street;
        this.building = building;
        this.city = city;
        this.state = state;
        this.country = country;
        this.zipcode = zipcode;
    }

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
