package com.sde.project.fileservice.repositories;

import com.sde.project.fileservice.models.tables.File;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface FileRepository extends JpaRepository<File, UUID> {
   @Query("SELECT f FROM File f WHERE f.sessionId = ?1")
   List<File> findAllBySessionId(UUID uuid);
}
