package com.example.teedrive.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "users")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_id_seq")
    private Long id;

    private String fullName;

    private String email;

    private String avatar;

    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL)
    private Set<FileEntity> ownedFiles;

    @ManyToMany(mappedBy = "sharedWith")
    private Set<FileEntity> sharedFiles;

}
