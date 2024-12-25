package com.example.teedrive.services;

import com.example.teedrive.domain.entity.FileEntity;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface FileService {
    FileEntity uploadFile(FileEntity fileEntity);

    List<FileEntity> findAll();

    List<FileEntity> findbyUserId(Long id);

    FileEntity partialUpdate(Long id, FileEntity fileEntity);

    Optional<FileEntity> findOne(Long id);

    FileEntity renameFIle(Long id, String name);

    FileEntity updateSharedUser(Long id, Set<Long> sharedUsers);

    void deleteFile(Long id);
}
