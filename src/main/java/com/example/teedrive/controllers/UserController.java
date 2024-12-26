package com.example.teedrive.controllers;

import com.example.teedrive.domain.dto.UserDto;
import com.example.teedrive.domain.entity.UserEntity;
import com.example.teedrive.mappers.Mapper;
import com.example.teedrive.services.UserService;
import org.hibernate.Hibernate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
public class UserController {
    private UserService userService;

    private Mapper<UserEntity, UserDto> userMapper;

    public UserController(UserService userService, Mapper<UserEntity, UserDto> userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
    }

    @GetMapping(path = "/users")
    public List<UserDto> getAllUsers() {
        List<UserEntity> allUsers = userService.findAllUsers();
        return allUsers.stream()
                .map(userMapper::mapTo)
                .collect(Collectors.toList());
    }

    @PostMapping(path = "/users/signin")
    public ResponseEntity<?> signInNewUser(@RequestBody UserDto userDto) {
        try {
            UserEntity userEntity = userMapper.mapFrom(userDto);
            UserEntity savedUserEntity = userService.signInNewUser(userEntity);
            UserDto returnUserDto = userMapper.mapTo(savedUserEntity);
            return new ResponseEntity<>(returnUserDto, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }

    @PostMapping(path = "/users/signup")
    public ResponseEntity<?> signUpUser(@RequestBody UserDto userDto) {
        try {
            UserEntity userEntity = userService.signUpUser(userDto.getEmail(), userDto.getPassword());
            UserDto returnUserDto = userMapper.mapTo(userEntity);
            return new ResponseEntity<>(returnUserDto, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }

    @GetMapping(path = "/users/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable("id") Long id) {
        Optional<UserEntity> foundUser = userService.findUserById(id);
        return foundUser.map(userEntity -> {
            UserDto returnUserDto = userMapper.mapTo(userEntity);
            return new ResponseEntity<>(returnUserDto, HttpStatus.FOUND);
        }).orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

}
