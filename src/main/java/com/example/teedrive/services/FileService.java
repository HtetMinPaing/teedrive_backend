package com.example.teedrive.services;

import com.example.teedrive.domain.entity.FileEntity;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public interface FileService {

    List<FileEntity> findAll();

    FileEntity uploadFile(FileEntity fileEntity);

    Optional<FileEntity> findOne(Long id);

    List<FileEntity> findbyUserId(Long id);

    FileEntity renameFIle(Long id, String name);

    FileEntity updateSharedUser(Long id, Set<Long> sharedUsers);

    void deleteFile(Long id);

    public Map<String, Object> calculateTotalSpaceUsed(Long userId);
}
