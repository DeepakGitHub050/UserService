package com.dksapp.userservice.Repositories;

import com.dksapp.userservice.Models.Token;
import com.dksapp.userservice.Models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TokenRepository extends JpaRepository<Token, Long> {
    Token findByTokenAndIsDeletedEquals(String token,int isDeleted);
    Token findByUserAndIsDeletedEquals(User user, int isDeleted);
}
