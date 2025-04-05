package com.tiktokus.tiktokus.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Customer {
    private String customName;
    private String phone;
    private String street;
    private String streetTwo;
    private String city;
    private String state;
    private String country;
    private String zipCode;
    private String email;
}
