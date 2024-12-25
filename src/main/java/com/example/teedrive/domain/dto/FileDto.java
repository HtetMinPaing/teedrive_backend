package com.example.teedrive.domain.dto;

import com.example.teedrive.domain.entity.UserEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FileDto {
    private Long id;

    private String name;

    private String url;

    private String type;

    private String extension;

    private String size;

    private UserEntity owner;

    private Set<UserEntity> sharedWith;
}
