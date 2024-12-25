package com.example.teedrive.services.impl;

import com.example.teedrive.domain.entity.UserEntity;
import com.example.teedrive.repositories.UserRepository;
import com.example.teedrive.services.UserService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserEntity saveUser(UserEntity userEntity) {
        return userRepository.save(userEntity);
    }

    @Override
    public Optional<UserEntity> findUserById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public List<UserEntity> findAllUsers() {
        return StreamSupport.stream(
                userRepository.findAll().spliterator(),
                false
        ).collect(Collectors.toList());
    }

}
