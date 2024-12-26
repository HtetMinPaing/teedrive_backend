package com.example.teedrive.controllers;

import com.example.teedrive.domain.dto.FileDto;
import com.example.teedrive.domain.entity.FileEntity;
import com.example.teedrive.domain.entity.UserEntity;
import com.example.teedrive.mappers.Mapper;
import com.example.teedrive.services.FileService;
import com.example.teedrive.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
public class FileController {

    private FileService fileService;

    private Mapper<FileEntity, FileDto> fileMapper;


    public FileController(FileService fileService, Mapper<FileEntity, FileDto> fileMapper) {
        this.fileService = fileService;
        this.fileMapper = fileMapper;
    }

    @GetMapping(path = "/files")
    public  List<FileEntity> getAllFiles() {
        List<FileEntity> allFiles = fileService.findAll();
        System.out.println(allFiles);
        return allFiles;
    }

    @PostMapping(path = "/files")
    public ResponseEntity<?> uploadFile(@RequestBody FileDto fileDto) {
        try {
            FileEntity fileEntity = fileMapper.mapFrom(fileDto);
            FileEntity savedFileEntity = fileService.uploadFile(fileEntity);
            FileDto returnFileDto = fileMapper.mapTo(savedFileEntity);
            return new ResponseEntity<>(returnFileDto, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(path = "/{userId}/files")
    public List<FileDto> getOwnAndSharedFiles(@PathVariable("userId") Long id) {
        List<FileEntity> allFiles = fileService.findbyUserId(id);
        return allFiles.stream()
                .map(fileMapper::mapTo)
                .collect(Collectors.toList());
    }

    @GetMapping(path = "/files/{id}")
    public ResponseEntity<FileDto> getFile(@PathVariable("id") Long id) {
        Optional<FileEntity> foundFileEntity = fileService.findOne(id);
        return foundFileEntity.map(fileEntity -> {
            FileDto returnFileDto = fileMapper.mapTo(fileEntity);
            return new ResponseEntity<>(returnFileDto, HttpStatus.FOUND);
        }).orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping(path = "/files/{id}")
    public ResponseEntity<FileDto> deleteFile(@PathVariable("id") Long id) {
        fileService.deleteFile(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PatchMapping(path = "/files/{id}/rename")
    public ResponseEntity<?> renameFile(
            @PathVariable("id") Long id,
            @RequestBody FileDto fileDto
    ) {
        try {
            FileEntity fileEntity = fileMapper.mapFrom(fileDto);
            FileEntity updatedFileEntity = fileService.renameFIle(id, fileDto.getName());
            FileDto returnFileDto = fileMapper.mapTo(updatedFileEntity);
            return new ResponseEntity<>(returnFileDto, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PatchMapping(path = "/files/{id}/sharedWith")
    public ResponseEntity<?> updateSharedUsers(
            @PathVariable("id") Long id,
            @RequestBody Set<String> sharedUserEmails
    ) {
        try {
            FileEntity updatedFileEntity = fileService.updateSharedUser(id, sharedUserEmails);
            FileDto returnFileDto = fileMapper.mapTo(updatedFileEntity);
            return new ResponseEntity<>(returnFileDto, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(path = "{userId}/space")
    public ResponseEntity<Map<String, Object>> calculateTotalSpaceUsed(
            @PathVariable("userId") Long userId
    ) {
        Map<String, Object> totalSpaceDetails = fileService.calculateTotalSpaceUsed(userId);
        return new ResponseEntity<>(totalSpaceDetails, HttpStatus.OK);
    }
}
