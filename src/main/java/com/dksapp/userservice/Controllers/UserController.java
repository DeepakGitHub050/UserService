package com.dksapp.userservice.Controllers;

import com.dksapp.userservice.DTOs.ResponseDTO;
import com.dksapp.userservice.DTOs.UserDetailsDTO;
import com.dksapp.userservice.Models.User;
import com.dksapp.userservice.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private final UserService userService;
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<ResponseDTO>> getAllUsers() {
        return ResponseEntity.ok(userService.findAll());
    }

    @PostMapping
    public ResponseEntity<User> addUser(@RequestBody UserDetailsDTO user) {
        User usr = userService.addUser(user);
        return ResponseEntity.ok(usr);
    }
}
