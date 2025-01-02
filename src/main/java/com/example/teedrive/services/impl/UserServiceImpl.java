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
    public UserEntity signUpNewUser(UserEntity userEntity) {
//        String hashedPassword = passwordEncoder.encode(userEntity.getPassword());
//        userEntity.setPassword(hashedPassword);
        if(userRepository.findByEmail(userEntity.getEmail()).isPresent()) {
            throw new RuntimeException("Email Already taken");
        }
        return userRepository.save(userEntity);
    }

    @Override
    public UserEntity signInUser(String email, String password) {
        UserEntity foundUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("No user found with this email"));
        if (password.equals(foundUser.getPassword())) {
            return foundUser;
        } else {
            throw new RuntimeException(("Password Incorrect"));
        }
    }

    @Override
    public Optional<UserEntity> findUserByEmail(String email) {
        return userRepository.findByEmail(email);
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

    @Override
    public UserEntity updateUser(Long id, UserEntity userEntity) {
        return null;
    }

    @Override
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}
