package com.example.teedrive.services;

import com.example.teedrive.domain.entity.UserEntity;

import java.util.List;
import java.util.Optional;

public interface UserService {
    UserEntity saveUser(UserEntity userEntity);

    Optional<UserEntity> findUserById(Long id);

    List<UserEntity> findAllUsers();
}
