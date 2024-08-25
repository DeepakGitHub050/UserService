package com.dksapp.userservice.Controllers;

import com.dksapp.userservice.DTOs.LoginRequestDto;
import com.dksapp.userservice.DTOs.ResponseDTO;
import com.dksapp.userservice.DTOs.UserDetailsDTO;
import com.dksapp.userservice.Models.Token;
import com.dksapp.userservice.Models.User;
import com.dksapp.userservice.Services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<ResponseDTO>> getAllUsers() {
        return ResponseEntity.ok(userService.findAll());
    }

    @GetMapping("/{userName}")
    public ResponseEntity<ResponseDTO> getUserById(@PathVariable String userName) {
        return userService.findByUserName(userName);
    }

    @PostMapping("/signUp")
    public ResponseEntity<User> addUser(@RequestBody UserDetailsDTO user) {
        return userService.addUser(user);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequestDto loginRequest) {
        String pswd = loginRequest.getPassword();
        String userName = loginRequest.getUserName();
        Token token = userService.login(userName, pswd);
        if (token != null) {
            return ResponseEntity.ok(token.getToken());
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @PostMapping("/logOut")
    public ResponseEntity<Void> logout(@RequestParam("token") String token) {
        userService.logout(token);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/validate/{token}")
    public ResponseEntity<Boolean> validateToken(@PathVariable String token) {
        return new ResponseEntity<>(userService.validateToken(token), HttpStatus.OK);
    }

    @PutMapping("/{userName}")
    public ResponseEntity<User> updateUser(@PathVariable("userName") String userName, @RequestBody UserDetailsDTO user) {
        return userService.updateUser(userName, user);
    }

    @DeleteMapping("/{userName}")
    public ResponseEntity<String> deleteUser(@PathVariable("userName") String userName) {
        return userService.deleteUser(userName);
    }
}
