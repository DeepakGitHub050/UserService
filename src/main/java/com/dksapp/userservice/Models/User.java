package com.dksapp.userservice.Models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Entity
@Getter
@Setter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String userName;
    private String password;
    private String email;
    private String phone;
    private String address;
    @ManyToMany(fetch = FetchType.EAGER)
    private List<Role> role;
}
