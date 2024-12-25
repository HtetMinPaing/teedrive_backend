package com.example.teedrive.services.impl;

import com.example.teedrive.domain.entity.FileEntity;
import com.example.teedrive.domain.entity.UserEntity;
import com.example.teedrive.repositories.FileRepository;
import com.example.teedrive.repositories.UserRepository;
import com.example.teedrive.services.FileService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class FileServiceImpl implements FileService {

    private FileRepository fileRepository;

    private UserRepository userRepository;

    public FileServiceImpl(FileRepository fileRepository, UserRepository userRepository) {
        this.fileRepository = fileRepository;
        this.userRepository = userRepository;
    }

    @Override
    public FileEntity uploadFile(FileEntity fileEntity) {
        UserEntity owner = userRepository.findById(fileEntity.getOwner().getId())
                .orElseThrow(() -> new RuntimeException("No User found"));
        fileEntity.setOwner(owner);

        Set<UserEntity> sharedUsers = fileEntity.getSharedWith().stream()
                .map(user -> {
                    return userRepository.findById(user.getId())
                            .orElseThrow(() -> new RuntimeException("No User found"));
                }).collect(Collectors.toSet());
        fileEntity.setSharedWith(sharedUsers);
        return fileRepository.save(fileEntity);
    }

    @Override
    public List<FileEntity> findAll() {
        return StreamSupport.stream(
                fileRepository.findAll().spliterator(),
                false
        ).collect(Collectors.toList());
    }

    @Override
    public List<FileEntity> findbyUserId(Long id) {
        return fileRepository.findAllFilesByUser(id);
    }

    @Override
    public FileEntity partialUpdate(Long id, FileEntity fileEntity) {
        fileEntity.setId(id);
        return fileRepository.findById(id).map(existingFile -> {
            Optional.ofNullable(fileEntity.getName()).ifPresent(existingFile::setName);
            Optional.ofNullable(fileEntity.getSize()).ifPresent(existingFile::setSize);
            Optional.ofNullable(fileEntity.getExtension()).ifPresent(existingFile::setExtension);
            Optional.ofNullable(fileEntity.getType()).ifPresent(existingFile::setType);
            Optional.ofNullable(fileEntity.getUrl()).ifPresent(existingFile::setUrl);
            return  fileRepository.save(existingFile);
        }).orElseThrow(() -> new RuntimeException("Cannot update File"));
    }

    @Override
    public Optional<FileEntity> findOne(Long id) {
        return fileRepository.findById(id);
    }

    @Override
    public FileEntity renameFIle(Long id, String name) {
        return fileRepository.findById(id).map(existingFile -> {
            existingFile.setName(name);
            return fileRepository.save(existingFile);
        }).orElseThrow(() -> new RuntimeException("Cannot rename File"));
    }

    @Override
    public FileEntity updateSharedUser(Long id, Set<Long> sharedUsersId) {
        Set<UserEntity> sharedUsers = sharedUsersId.stream()
                .map(userId -> userRepository.findById(userId)
                        .orElseThrow(() -> new RuntimeException("User not found")))
                .collect(Collectors.toSet());
        return fileRepository.findById(id).map(existingFile -> {
            existingFile.setSharedWith(sharedUsers);
            return fileRepository.save(existingFile);
        }).orElseThrow(() -> new RuntimeException("Cannot update Shared Users"));
    }

    @Override
    public void deleteFile(Long id) {
        fileRepository.deleteById(id);
    }
}
