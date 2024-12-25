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
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
public class FileController {

    private FileService fileService;

    private UserService userService;

    private Mapper<FileEntity, FileDto> fileMapper;


    public FileController(FileService fileService, UserService userService, Mapper<FileEntity, FileDto> fileMapper) {
        this.fileService = fileService;
        this.userService = userService;
        this.fileMapper = fileMapper;
    }

    @PostMapping(path = "/files")
    public ResponseEntity<FileDto> uploadFile(@RequestBody FileDto fileDto) {
        FileEntity fileEntity = fileMapper.mapFrom(fileDto);
        FileEntity savedFileEntity = fileService.uploadFile(fileEntity);
        FileDto returnFileDto = fileMapper.mapTo(savedFileEntity);
        return new ResponseEntity<>(returnFileDto, HttpStatus.CREATED);
    }

    @GetMapping(path = "/files")
    public  List<FileEntity> getAllFilesByUser() {
        List<FileEntity> allFiles = fileService.findAll();
        System.out.println(allFiles);
        return allFiles;
    }

    @GetMapping(path = "/files/user/{id}")
    public List<FileDto> getOwnAndSharedFiles(@PathVariable("id") Long id) {
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

    @PatchMapping(path = "/files/{id}")
    public ResponseEntity<FileDto> partialUpdate(
            @PathVariable("id") Long id,
            @RequestBody FileDto fileDto
    ) {
        FileEntity fileEntity = fileMapper.mapFrom(fileDto);
        FileEntity updatedFileEntity = fileService.partialUpdate(id, fileEntity);
        FileDto returnFileDto = fileMapper.mapTo(updatedFileEntity);
        return new ResponseEntity<>(returnFileDto, HttpStatus.OK);
    }

    @PatchMapping(path = "/files/{id}/rename")
    public ResponseEntity<FileDto> renameFile(
            @PathVariable("id") Long id,
            @RequestBody FileDto fileDto
    ) {
        FileEntity fileEntity = fileMapper.mapFrom(fileDto);
        FileEntity updatedFileEntity = fileService.renameFIle(id, fileDto.getName());
        FileDto returnFileDto = fileMapper.mapTo(updatedFileEntity);
        return new ResponseEntity<>(returnFileDto, HttpStatus.OK);
    }

    @PatchMapping(path = "/files/{id}/sharedWith")
    public ResponseEntity<FileDto> updateSharedUsers(
            @PathVariable("id") Long id,
            @RequestBody Set<Long> sharedUsersId
    ) {
        FileEntity updatedFileEntity = fileService.updateSharedUser(id, sharedUsersId);
        FileDto returnFileDto = fileMapper.mapTo(updatedFileEntity);
        return new ResponseEntity<>(returnFileDto, HttpStatus.OK);
    }

    @DeleteMapping(path = "/files/{id}")
    public ResponseEntity<FileDto> deleteFile(@PathVariable("id") Long id) {
        fileService.deleteFile(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }


}
