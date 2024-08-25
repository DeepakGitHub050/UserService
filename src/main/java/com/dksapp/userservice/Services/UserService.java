package com.dksapp.userservice.Services;

import com.dksapp.userservice.DTOs.*;
import com.dksapp.userservice.Models.*;
import com.dksapp.userservice.Repositories.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenRepository tokenRepository;

    public UserService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder, TokenRepository tokenRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenRepository = tokenRepository;
    }

    public Token login(String username, String password) {
        User usr = userRepository.findByUserName(username);
        if (usr == null) {
            return null;
        }
        if (!passwordEncoder.matches(password, usr.getPassword())) {
            return null;
        }
        Token token = tokenRepository.findByUserAndIsDeletedEquals(usr,0);
        if (token == null) {
            Token newToken = new Token();
            newToken.setUser(usr);
            Date now = new Date();
            Calendar c = Calendar.getInstance();
            c.setTime(now);
            c.add(Calendar.DATE,10);
            newToken.setCreateDate(now);
            newToken.setExpiryDate(c.getTime());
            newToken.setToken(UUID.randomUUID().toString());
            newToken.setIsDeleted(0);
            tokenRepository.save(newToken);
            return newToken;
        }
        return token;
    }

    public void logout(String token) {
        Token t = tokenRepository.findByTokenAndIsDeletedEquals(token,0);
        if (t != null) {
            t.setIsDeleted(1);
            tokenRepository.save(t);
        }
    }

    public boolean validateToken(String token) {
        Token t = tokenRepository.findByTokenAndIsDeletedEquals(token,0);
        if (t == null) {
            return false;
        }
        return t.getIsDeleted() != 1;
    }

    public ResponseDTO userToResponseDTO(User user) {
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setUserName(user.getUserName());
        responseDTO.setEmail(user.getEmail());
        responseDTO.setRole(user.getRole().stream().map(Role::getName).collect(Collectors.toSet()));
        responseDTO.setPhone(user.getPhone());
        responseDTO.setAddress(user.getAddress());
        return responseDTO;
    }

    public List<ResponseDTO> findAll() {
        List<User> users =userRepository.findAll();
        List<ResponseDTO> responseDTOs = new ArrayList<>();
        for (User user : users) {
            responseDTOs.add(userToResponseDTO(user));
        }
        return responseDTOs;
    }

    public ResponseEntity<ResponseDTO> findByUserName(String userName) {
        User user = userRepository.findByUserName(userName);
        if (user == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(userToResponseDTO(user), HttpStatus.OK);
    }

    public User userDetailsDTOToUser(User user,UserDetailsDTO userDetailsDTO) {
        List<Role> roles = new ArrayList<>();
        user.setUserName(userDetailsDTO.getUserName());
        user.setPassword(passwordEncoder.encode(userDetailsDTO.getPassword()));
        user.setEmail(userDetailsDTO.getEmail());
        user.setAddress(userDetailsDTO.getAddress());
        user.setPhone(userDetailsDTO.getPhone());
        for(String r:userDetailsDTO.getRole()){
            Role role = roleRepository.findByName(r);
            if(role != null){
                roles.add(role);
            }
            else{
                Role newRole = new Role();
                newRole.setName(r);
                roles.add(newRole);
                roleRepository.save(newRole);
            }
        }
        user.setRole(roles);
        return user;
    }

    public ResponseEntity<User> addUser(UserDetailsDTO userDetailsDTO) {
        if(userRepository.findByUserName(userDetailsDTO.getUserName())!=null){
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        }
        User user = userDetailsDTOToUser(new User(),userDetailsDTO);
        userRepository.save(user);
        return new ResponseEntity<>(user,HttpStatus.CREATED);
    }

    public ResponseEntity<User> updateUser(String userName,UserDetailsDTO userDetailsDTO) {
        User user = userRepository.findByUserName(userName);
        if(user==null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        User usr = userDetailsDTOToUser(user,userDetailsDTO);
        userRepository.save(usr);
        return new ResponseEntity<>(usr,HttpStatus.OK);
    }

    public ResponseEntity<String> deleteUser(String userName) {
        User user = userRepository.findByUserName(userName);
        if(user==null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        userRepository.delete(user);
        return new ResponseEntity<>(userName,HttpStatus.OK);
    }
}
