package com.dksapp.userservice.Models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.Date;

@Entity
@Getter
@Setter
public class Token {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String token;
    private Date expiryDate;
    private Date createDate;
    private int isDeleted;
    @ManyToOne
    private User user;
}
