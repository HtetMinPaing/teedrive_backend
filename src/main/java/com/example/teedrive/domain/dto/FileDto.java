package com.example.teedrive.domain.dto;

import com.example.teedrive.domain.entity.UserEntity;
import jakarta.persistence.*;

import java.util.Set;

public class FileDto {
    private Long id;

    private String name;

    private String url;

    private String type;

    private String extension;

    private String size;

    private String users;

    private UserEntity owner;

    private Set<UserEntity> sharedWith;
}
