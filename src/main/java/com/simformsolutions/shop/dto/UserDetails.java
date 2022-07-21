package com.simformsolutions.shop.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDetails {

    private int userId;
    private String name;
    private String email;
    private String password;
    private String contact;
    private String address;
}
