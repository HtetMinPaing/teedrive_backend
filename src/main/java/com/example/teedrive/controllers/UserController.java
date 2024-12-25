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

    @PostMapping(path = "/users")
    public ResponseEntity<UserDto> saveUser(@RequestBody UserDto userDto) {
        UserEntity userEntity = userMapper.mapFrom(userDto);
        UserEntity savedUserEntity = userService.saveUser(userEntity);
        UserDto returnUserDto = userMapper.mapTo(savedUserEntity);
        return new ResponseEntity<>(returnUserDto, HttpStatus.CREATED);
    }

    @GetMapping(path = "/users")
    public List<UserDto> getAllUsers() {
        List<UserEntity> allUsers = userService.findAllUsers();
        return allUsers.stream()
                .map(userMapper::mapTo)
                .collect(Collectors.toList());
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
