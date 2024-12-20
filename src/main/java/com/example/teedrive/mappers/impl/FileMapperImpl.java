package com.example.teedrive.mappers.impl;

import com.example.teedrive.domain.dto.FileDto;
import com.example.teedrive.domain.entity.FileEntity;
import com.example.teedrive.mappers.Mapper;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class FileMapperImpl implements Mapper<FileEntity, FileDto> {

    private ModelMapper modelMapper;

    public FileMapperImpl(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public FileDto mapTo(FileEntity fileEntity) {
        return modelMapper.map(fileEntity, FileDto.class);
    }

    @Override
    public FileEntity mapFrom(FileDto fileDto) {
        return modelMapper.map(fileDto, FileEntity.class);
    }
}
