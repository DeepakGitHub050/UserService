package com.dksapp.userservice.Services;

import com.dksapp.userservice.DTOs.ResponseDTO;
import com.dksapp.userservice.DTOs.UserDetailsDTO;
import com.dksapp.userservice.Models.Role;
import com.dksapp.userservice.Models.User;
import com.dksapp.userservice.Repositories.RoleRepository;
import com.dksapp.userservice.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private final UserRepository userRepository;
    @Autowired
    private final RoleRepository roleRepository;

    public UserService(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    public List<ResponseDTO> findAll() {
        List<User> users =userRepository.findAll();
        List<ResponseDTO> responseDTOs = new ArrayList<>();
        for (User user : users) {
            ResponseDTO responseDTO = new ResponseDTO();
            responseDTO.setUserName(user.getUserName());
            responseDTO.setEmail(user.getEmail());
            responseDTO.setRole(user.getRole().stream().map(Role::getName).collect(Collectors.toSet()));
            responseDTO.setPhone(user.getPhone());
            responseDTO.setAddress(user.getAddress());
            responseDTOs.add(responseDTO);
        }
        return responseDTOs;
    }

    public User addUser(UserDetailsDTO userDetailsDTO) {
        User user = new User();
        List<Role> roles = new ArrayList<>();
        user.setUserName(userDetailsDTO.getUserName());
        user.setPassword(userDetailsDTO.getPassword());
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
        userRepository.save(user);
        return user;
    }

}
