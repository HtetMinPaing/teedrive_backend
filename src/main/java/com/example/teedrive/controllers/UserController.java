package com.example.teedrive.controllers;

import com.example.teedrive.domain.dto.UserDto;
import com.example.teedrive.domain.entity.UserEntity;
import com.example.teedrive.mappers.Mapper;
import com.example.teedrive.services.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.hibernate.Hibernate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
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

    @PostMapping(path = "/users/signup")
    public ResponseEntity<?> signUpNewUser(@RequestBody UserDto userDto) {
        try {
            UserEntity userEntity = userMapper.mapFrom(userDto);
            UserEntity savedUserEntity = userService.signUpNewUser(userEntity);
            UserDto returnUserDto = userMapper.mapTo(savedUserEntity);
//            session.setAttribute("teedrive", returnUserDto);
            return new ResponseEntity<>(returnUserDto, HttpStatus.CREATED);
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
        }
    }

    @PostMapping(path = "/users/signin")
    public ResponseEntity<?> signInUser(@RequestBody UserDto userDto) {
        try {
            UserEntity userEntity = userService.signInUser(userDto.getEmail(), userDto.getPassword());
            UserDto returnUserDto = userMapper.mapTo(userEntity);

//            session.setAttribute("teedrive", returnUserDto);
//
//            // Set a secure cookie
//            Cookie sessionCookie = new Cookie("JSESSIONID", session.getId());
//            sessionCookie.setHttpOnly(true);
//            sessionCookie.setSecure(true); // Use secure cookies in production
//            sessionCookie.setPath("/");
//            response.addCookie(sessionCookie);
//
//            System.out.println("Session attribute set: " + session.getId());
//            System.out.println("Session attribute set: " + session.getAttribute("teedrive"));
            return new ResponseEntity<>(returnUserDto, HttpStatus.OK);
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
        }
    }

    @GetMapping(path = "/users/me")
    public ResponseEntity<?> getCurrentUser(HttpSession session) {
        try {
            UserDto user = (UserDto) session.getAttribute("teedrive");
            System.out.println("Get Current User detail: " + session.getId());
            System.out.println("Get Current User detail: " + user);
            System.out.println("Get Current User detail: " + session.getAttribute("teedrive"));
            if (user == null) {
                throw new RuntimeException("Unauthorized");
            }
            return new ResponseEntity<>(user, HttpStatus.OK);
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
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
