package com.example.teedrive.services.impl;

import com.example.teedrive.domain.entity.FileEntity;
import com.example.teedrive.domain.entity.UserEntity;
import com.example.teedrive.repositories.FileRepository;
import com.example.teedrive.repositories.UserRepository;
import com.example.teedrive.services.FileService;
import org.springframework.stereotype.Service;

import java.util.*;
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
                .orElseThrow(() -> new RuntimeException("No Sign In User. Please Sign In"));
        fileEntity.setOwner(owner);

        Set<UserEntity> sharedUsers = fileEntity.getSharedWith().stream()
                .map(user -> {
                    return userRepository.findByEmail(user.getEmail())
                            .orElseThrow(() -> new RuntimeException("User does not exist: " + user.getEmail()));
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
    public FileEntity updateSharedUser(Long id, Set<String> sharedUserEmails) {
        Set<UserEntity> sharedUsers = sharedUserEmails.stream()
                .map(email -> {
                    return userRepository
                            .findByEmail(email)
                            .orElseThrow(() ->
                                    new RuntimeException("User does not exist: " + email)
                            );
                })
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

    public Map<String, Object> calculateTotalSpaceUsed(Long userId) {
        List<Object[]> spaceUsedData = fileRepository.calculateSpaceUsedByType(userId);
        Long totalUsedSpace = fileRepository.calculateTotalSpaceUsed(userId);
        Long totalSpaceAvailable = 2L * 1024 * 1024 * 1024; // 2GB

        Map<String, Object> totalSpace = new HashMap<>();
        Map<String, Object> fileTypeData = new HashMap<>();

        spaceUsedData.forEach(record -> {
            String type = (String) record[0];
            Long size = (Long) record[1];
            Date latestDate = (Date) record[2];

            Map<String, Object> typeDetails = new HashMap<>();
            typeDetails.put("size", size);
            typeDetails.put("latestDate", latestDate != null ? latestDate.toString() : "");

            fileTypeData.put(type, typeDetails);
        });

        totalSpace.put("fileTypes", fileTypeData);
        totalSpace.put("used", totalUsedSpace != null ? totalUsedSpace : 0);
        totalSpace.put("available", totalSpaceAvailable);

        return totalSpace;
    }
}
