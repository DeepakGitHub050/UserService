package com.dksapp.userservice.DTOs;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class ResponseDTO {
    private String userName;
    private String email;
    private String phone;
    private String address;
    private Set<String> role;
}
