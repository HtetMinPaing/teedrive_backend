package com.example.teedrive.repositories;

import com.example.teedrive.domain.entity.FileEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface FileRepository extends CrudRepository<FileEntity, Long> {
    @Query("SELECT f FROM FileEntity f " +
            "LEFT JOIN f.sharedWith shared " +
            "WHERE f.owner.id = :userId OR shared.id = :userId")
    List<FileEntity> findAllFilesByUser(@Param("userId") Long userId);

    @Query("SELECT f.type AS type, SUM(f.size) AS totalSize, MAX(f.updatedAt) AS latestDate " +
            "FROM FileEntity f " +
            "WHERE f.owner.id = :userId " +
            "GROUP BY f.type")
    List<Object[]> calculateSpaceUsedByType(@Param("userId") Long userId);

    @Query("SELECT SUM(f.size) FROM FileEntity f WHERE f.owner.id = :userId")
    Long calculateTotalSpaceUsed(@Param("userId") Long userId);
}
