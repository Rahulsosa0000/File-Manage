package com.file.repo;


import org.springframework.data.jpa.repository.JpaRepository;

import com.file.model.FilesLog;

public interface FilesLogRepo extends JpaRepository<FilesLog, Long> {
}
