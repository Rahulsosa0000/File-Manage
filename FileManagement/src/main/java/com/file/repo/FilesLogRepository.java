package com.file.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.file.entity.FilesLog;

public interface FilesLogRepository extends JpaRepository<FilesLog, Long> {
}
