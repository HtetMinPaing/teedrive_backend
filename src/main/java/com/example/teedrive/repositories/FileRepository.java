package com.example.teedrive.repositories;

import com.example.teedrive.domain.entity.FileEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FileRepository extends CrudRepository<FileEntity, Long> {
    @Query("SELECT f FROM FileEntity f " +
            "LEFT JOIN f.sharedWith shared " +
            "WHERE f.owner.id = :userId OR shared.id = :userId")
    List<FileEntity> findAllFilesByUser(@Param("userId") Long userId);
}
