package com.example.teedrive.domain.dto;

import com.example.teedrive.domain.entity.FileEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDto {
    private Long id;

    private String fullName;

    private String email;

    private String avatar;

//    private Set<FileEntity> ownedFiles;
//
//    private Set<FileEntity> sharedFiles;
}
