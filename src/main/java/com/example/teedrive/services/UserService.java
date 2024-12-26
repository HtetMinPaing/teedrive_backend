package com.example.teedrive.services;

import com.example.teedrive.domain.entity.UserEntity;

import java.util.List;
import java.util.Optional;

public interface UserService {
    UserEntity signInNewUser(UserEntity userEntity);

    UserEntity signUpUser(String email, String password);

    Optional<UserEntity> findUserByEmail(String email);

    Optional<UserEntity> findUserById(Long id);

    List<UserEntity> findAllUsers();

    UserEntity updateUser(Long id, UserEntity userEntity);

    void deleteUser(Long id);
}
