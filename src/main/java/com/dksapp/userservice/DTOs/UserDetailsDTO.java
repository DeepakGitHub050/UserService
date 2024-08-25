package com.dksapp.userservice.DTOs;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserDetailsDTO {
    private String userName;
    private String password;
    private String email;
    private String phone;
    private String address;
    private String[] role;
}
