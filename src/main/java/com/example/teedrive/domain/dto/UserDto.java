package com.example.teedrive.domain.dto;

import com.example.teedrive.domain.entity.FileEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;

import java.util.Set;

public class UserDto {
    private Long id;

    private String fullName;

    private String email;

    private String avatar;

    private Set<FileEntity> ownedFiles;

    private Set<FileEntity> sharedFiles;
}
